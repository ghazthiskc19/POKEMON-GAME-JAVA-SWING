import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class MusicPlayer {

    public enum MusicType {
        START_MENU,
        MAIN_MENU,
        ARENA
    }

    private int currentVolumePercent = 70; // Default volume percent
    private Clip currentClip;
    private Thread musicThread;
    private volatile boolean isPlaying = false;
    private String currentMusicPath = null;

    private final String startMenuMusicPath;
    private final String mainMenuMusicPath;
    private final String arenaMusicPath;

    public MusicPlayer(String startMenuMusicPath, String mainMenuMusicPath, String arenaMusicPath) {
        this.startMenuMusicPath = startMenuMusicPath;
        this.mainMenuMusicPath = mainMenuMusicPath;
        this.arenaMusicPath = arenaMusicPath;
    }

    public synchronized void playMusic(MusicType type) {
        String path = null;
        switch (type) {
            case START_MENU:
                path = startMenuMusicPath;
                break;
            case MAIN_MENU:
                path = mainMenuMusicPath;
                break;
            case ARENA:
                path = arenaMusicPath;
                break;
            default:
                System.out.println("Tipe musik tidak dikenal.");
                return;
        }

        if (isPlaying && path != null && path.equals(currentMusicPath)) {
            return;
        }

        stopMusic();

        final String musicFilePath = path;
        currentMusicPath = musicFilePath; // Simpan path musik saat ini

        if (musicFilePath == null || musicFilePath.isEmpty()) {
            System.out.println("Path musik kosong untuk tipe: " + type);
            return;
        }

        musicThread = new Thread(() -> {
            try {
                isPlaying = true;
                File audioFile = new File(musicFilePath);
                if (!audioFile.exists()) {
                    System.out.println("File musik tidak ditemukan: " + musicFilePath);
                    isPlaying = false;
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                currentClip = AudioSystem.getClip();
                currentClip.open(audioStream);
                setVolumePercent(currentVolumePercent);
                currentClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop musik
                //setVolume(currentVolumePercent); // Setel volume awal
                currentClip.start();

                while (isPlaying) {
                    Thread.sleep(100); // Cek periodik
                }
                if (currentClip != null && currentClip.isRunning()) {
                    currentClip.stop();
                }
                if (currentClip != null) {
                    currentClip.close();
                }

            } catch (UnsupportedAudioFileException e) {
                System.err.println("Format audio tidak didukung: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error I/O saat memutar musik: " + e.getMessage());
            } catch (LineUnavailableException e) {
                System.err.println("Line audio tidak tersedia: " + e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("Pemutaran musik dihentikan (interupsi).");
                Thread.currentThread().interrupt(); // Setel kembali status interupsi
            } finally {
                if (currentClip != null && currentClip.isOpen()) {
                    if (currentClip.isRunning()) {
                        currentClip.stop();
                    }
                    currentClip.close();
                }
                currentClip = null;
                isPlaying = false;
                System.out.println("Thread musik selesai untuk: " + musicFilePath);
            }
        });
        musicThread.setName("MusicPlayerThread-" + type);
        musicThread.setDaemon(true); // Agar thread tidak mencegah aplikasi keluar
        musicThread.start();
    }

    public synchronized void stopMusic() {
        isPlaying = false; // Ini akan menghentikan loop di dalam thread musik
        currentMusicPath = null; // Reset path musik saat ini

        if (musicThread != null && musicThread.isAlive()) {
            musicThread.interrupt(); // Interupsi thread jika sedang sleep atau menunggu
            try {
                musicThread.join(1000); // Tunggu thread selesai (dengan timeout)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Gagal menunggu thread musik berhenti: " + e.getMessage());
            }
        }
        if (currentClip != null) {
            if (currentClip.isRunning()) {
                currentClip.stop();
            }
            if (currentClip.isOpen()) {
                currentClip.close();
            }
            currentClip = null;
        }
        System.out.println("Fungsi stopMusic selesai.");
    }

    public void setVolumePercent(int percent) {
        currentVolumePercent = percent; // Simpan volume saat ini
        if (currentClip != null) {
            FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            float gain;
            if (percent == 0) {
                gain = min;
            } else {
                gain = (float) (Math.log10(percent / 100.0) * 20.0);
                if (gain < min)
                    gain = min;
                if (gain > max)
                    gain = max;
            }
            gainControl.setValue(gain);
        }
    }

    // Example helper method (implement this if not present)
    private void setVolume(int volume) {
        // Implementation depends on your audio system
        // For example, using FloatControl:

        if (currentClip != null) {
            FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            float gain = min + (max - min) * volume;
            gainControl.setValue(gain);
        }
    }

    public int getVolumePercent() {
        return currentVolumePercent;
    }

    public boolean isVolumeControlSupported() {
        return true;
    }

}