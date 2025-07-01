// File: FadeTransition.java
import javax.swing.*;
import java.awt.*;

public class FadeTransition extends JPanel {
    private float alpha = 1.0f;
    private Runnable onFinish;

    public FadeTransition() {
        Timer timer = new Timer(16, e -> repaint());
        timer.start();
    }

    public void fadeOut(Runnable onFinish) {
        this.onFinish = onFinish;
        new Thread(() -> {
            for (int i = 0; i <= 25; i++) {
                alpha = 1.0f - i / 25.0f;
                repaint();
                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
            }
            onFinish.run();
        }).start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, Math.min(255, (int)(255 * alpha))));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}