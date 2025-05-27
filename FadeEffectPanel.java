import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FadeEffectPanel extends JPanel {
    private float currentAlpha = 0.0f; // Alpha saat ini
    private float targetAlpha = 0.0f; // Alpha tujuan
    private float alphaIncrement = 0.0f; // Seberapa besar perubahan alpha per step
    private Timer animationTimer;
    private Runnable onFadeComplete;
    private Color fadeColor = Color.BLACK; // Warna untuk fade (default hitam)

    private final int FADE_STEP_MS = 25; // Frekuensi update animasi (ms)

    public FadeEffectPanel() {
        setOpaque(false); // Panel utama transparan, kita gambar manual
        // Inisialisasi alpha agar tidak langsung terlihat jika belum di-fade
        // Untuk fade-in awal game, kita akan set alpha ke 1.0f (opaque) secara manual
        // sebelum dimulai.
    }

    /**
     * Mengatur warna yang digunakan untuk efek fade.
     * 
     * @param color Warna yang diinginkan.
     */
    public void setFadeColor(Color color) {
        this.fadeColor = color;
    }

    /**
     * Mengatur alpha awal panel secara manual. Berguna untuk fade-in pertama.
     * 
     * @param alpha Nilai alpha awal (0.0f transparan, 1.0f opaque).
     */
    public void setCurrentAlpha(float alpha) {
        this.currentAlpha = Math.max(0.0f, Math.min(1.0f, alpha));
        repaint(); // Update tampilan jika diperlukan
    }

    /**
     * Memulai animasi fade ke target alpha tertentu.
     * 
     * @param toAlpha    Target alpha (0.0f untuk fade-in konten / fade-out overlay,
     *                   1.0f untuk fade-out konten / fade-in overlay).
     * @param durationMs Durasi animasi dalam milidetik.
     * @param onComplete Aksi yang dijalankan setelah fade selesai.
     */
    public void startFade(float toAlpha, int durationMs, Runnable onComplete) {
        this.targetAlpha = Math.max(0.0f, Math.min(1.0f, toAlpha));
        this.onFadeComplete = onComplete;

        if (durationMs <= 0) { // Jika durasi tidak valid, langsung ke state akhir
            this.currentAlpha = this.targetAlpha;
            repaint();
            if (this.onFadeComplete != null) {
                this.onFadeComplete.run();
            }
            return;
        }

        // Hitung increment alpha per step
        float totalAlphaChange = this.targetAlpha - this.currentAlpha;
        int numSteps = durationMs / FADE_STEP_MS;
        if (numSteps <= 0)
            numSteps = 1; // minimal 1 step
        this.alphaIncrement = totalAlphaChange / numSteps;

        // Pastikan panel terlihat untuk memulai efek
        if (!isVisible()) {
            setVisible(true);
        }

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        // Jika sudah di target alpha, jalankan callback
        if (Math.abs(currentAlpha - targetAlpha) < Math.abs(alphaIncrement / 2)) { // Toleransi kecil
            currentAlpha = targetAlpha; // Snap ke target
            repaint();
            if (onFadeComplete != null) {
                onFadeComplete.run();
            }
            return;
        }

        animationTimer = new Timer(FADE_STEP_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentAlpha += alphaIncrement;

                boolean finished = false;
                if (alphaIncrement > 0) { // Fading towards opaque
                    if (currentAlpha >= targetAlpha) {
                        currentAlpha = targetAlpha;
                        finished = true;
                    }
                } else { // Fading towards transparent (alphaIncrement is negative)
                    if (currentAlpha <= targetAlpha) {
                        currentAlpha = targetAlpha;
                        finished = true;
                    }
                }

                repaint();

                if (finished) {
                    ((Timer) e.getSource()).stop();
                    if (onFadeComplete != null) {
                        onFadeComplete.run();
                    }
                }
            }
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentAlpha > 0.0f) { // Hanya gambar jika ada tingkat kepekatan
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentAlpha));
            g2d.setColor(this.fadeColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }
}