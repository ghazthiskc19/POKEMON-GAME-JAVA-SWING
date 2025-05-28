import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class SFXPlayer {
    private static int volume = 70; // Default volume

    public static void setVolume(int newVolume) {
        volume = Math.max(0, Math.min(100, newVolume)); // Clamp between 0 and 100
    }

    public static void playSound(String soundFilePath) {
        if (soundFilePath == null || soundFilePath.isEmpty()) {
            System.err.println("Peringatan SFXPlayer: Path file suara kosong atau null.");
            return;
        }

        // Print debug info
        System.out.println("Attempting to play sound: " + soundFilePath);
        System.out.println("Current volume: " + volume);

        new Thread(() -> {
            try {
                // Try .wav first, then .mp3 if .wav doesn't exist
                File audioFile = new File(soundFilePath);
                if (!audioFile.exists()) {
                    // Try with .mp3 extension
                    String mp3Path = soundFilePath.replace(".wav", ".mp3");
                    audioFile = new File(mp3Path);
                    if (!audioFile.exists()) {
                        System.err.println("Error SFXPlayer: File suara tidak ditemukan di -> " + soundFilePath
                                + " atau " + mp3Path);
                        return;
                    }
                }

                System.out.println("File exists, size: " + audioFile.length() + " bytes");

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat format = audioStream.getFormat();
                System.out.println("Audio format: " + format);

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                // Get the volume control
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                // Convert volume percentage to decibels
                float min = gainControl.getMinimum(); // Usually around -80 dB
                float max = gainControl.getMaximum(); // Usually 0 dB
                float range = max - min;
                float gain = min + (range * (volume / 100.0f));

                // Set the volume
                gainControl.setValue(gain);
                System.out.println("Volume set to: " + gain + " dB");

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        Clip c = (Clip) event.getLine();
                        c.close();
                        System.out.println("Sound playback completed");
                    }
                });

                clip.start();
                System.out.println("Sound playback started");

            } catch (UnsupportedAudioFileException e) {
                System.err.println("Error SFXPlayer: Format audio tidak didukung untuk -> " + soundFilePath
                        + ". Pesan: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Error SFXPlayer: Terjadi kesalahan I/O untuk -> " + soundFilePath + ". Pesan: "
                        + e.getMessage());
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                System.err.println("Error SFXPlayer: Line audio tidak tersedia untuk -> " + soundFilePath + ". Pesan: "
                        + e.getMessage());
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.err.println(
                        "Error SFXPlayer: Volume control tidak tersedia untuk -> " + soundFilePath + ". Pesan: "
                                + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error SFXPlayer: Unexpected error for -> " + soundFilePath + ". Pesan: "
                        + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}