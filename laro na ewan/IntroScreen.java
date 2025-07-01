// File: IntroScreen.java
import javax.swing.*;
import java.awt.*;

public class IntroScreen extends JPanel {
    private boolean finished = false;

    public IntroScreen() {
        setPreferredSize(new Dimension(640, 480));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                finished = true;
            }
        });
    }

    public boolean isFinished() {
        return finished;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.drawString("EchoWorld: The Fragmented Realms", 120, 100);
        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g.drawString("You are the soul of resilience, cast into a world of broken realms.", 60, 160);
        g.drawString("Forest. Ice. Fire. Each protected by a boss. Each gate locked by fate.", 60, 190);
        g.drawString("Recover the fragments. Unlock the truth.", 60, 220);
        g.drawString("Press any key to begin your journey...", 60, 300);
    }
}