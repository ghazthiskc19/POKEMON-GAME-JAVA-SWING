import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class SFXPlayer {
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
                Clip clip = AudioSystem.getClip();

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        Clip c = (Clip) event.getLine();
                        c.close();
                    }
                });
                clip.open(audioStream);
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