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
        new Thread(() -> {
            try {
                File audioFile = new File(soundFilePath);
                if (!audioFile.exists()) {
                    System.err.println("Error SFXPlayer: File suara tidak ditemukan di -> " + soundFilePath);
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat format = audioStream.getFormat();

                // Create a new audio format with adjusted sample rate based on volume
                float sampleRate = format.getSampleRate() * (volume / 100.0f);
                AudioFormat newFormat = new AudioFormat(
                        format.getEncoding(),
                        sampleRate,
                        format.getSampleSizeInBits(),
                        format.getChannels(),
                        format.getFrameSize(),
                        sampleRate,
                        format.isBigEndian());

                // Convert the audio stream to the new format
                AudioInputStream volumeAdjustedStream = AudioSystem.getAudioInputStream(newFormat, audioStream);

                Clip clip = AudioSystem.getClip();
                clip.open(volumeAdjustedStream);

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        Clip c = (Clip) event.getLine();
                        c.close();
                    }
                });

                clip.start();

            } catch (UnsupportedAudioFileException e) {
                System.err.println("Error SFXPlayer: Format audio tidak didukung untuk -> " + soundFilePath
                        + ". Pesan: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error SFXPlayer: Terjadi kesalahan I/O untuk -> " + soundFilePath + ". Pesan: "
                        + e.getMessage());
            } catch (LineUnavailableException e) {
                System.err.println("Error SFXPlayer: Line audio tidak tersedia untuk -> " + soundFilePath + ". Pesan: "
                        + e.getMessage());
            }
        }).start();
    }
}