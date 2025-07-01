// File: SplashScreen.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SplashScreen extends JPanel {
    private final String[] options = {"Play", "Settings", "Quit"};
    private int selected = 0;
    private boolean finished = false;
    private String choice = "";

    public SplashScreen() {
        setPreferredSize(new Dimension(640, 480));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    selected = (selected + options.length - 1) % options.length;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    selected = (selected + 1) % options.length;
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    choice = options[selected];
                    finished = true;
                }
                repaint();
            }
        });
    }

    public boolean isFinished() {
        return finished;
    }

    public String getChoice() {
        return choice;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 30));
        g.drawString("EchoWorld", 240, 120);

        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == selected ? Color.YELLOW : Color.GRAY);
            g.drawString(options[i], 280, 200 + i * 40);
        }
    }
}
