// File: SettingsMenu.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SettingsMenu extends JPanel {
    private final String[] options = {"Audio: ON", "Difficulty: ENJOY", "Back"};
    private boolean audioOn = true;
    private String difficulty = "ENJOY";
    private int selected = 0;
    private boolean finished = false;

    public SettingsMenu() {
        setPreferredSize(new Dimension(640, 480));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    selected = (selected + options.length - 1) % options.length;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    selected = (selected + 1) % options.length;
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    switch (selected) {
                        case 0 -> {
                            audioOn = !audioOn;
                            options[0] = "Audio: " + (audioOn ? "ON" : "OFF");
                        }
                        case 1 -> {
                            difficulty = switch (difficulty) {
                                case "EASY" -> "ENJOY";
                                case "ENJOY" -> "HARDCORE";
                                default -> "EASY";
                            };
                            options[1] = "Difficulty: " + difficulty;
                        }
                        case 2 -> finished = true;
                    }
                }
                repaint();
            }
        });
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isAudioOn() {
        return audioOn;
    }

    public String getDifficulty() {
        return difficulty;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 28));
        g.drawString("Settings", 250, 120);
        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == selected ? Color.YELLOW : Color.LIGHT_GRAY);
            g.drawString(options[i], 220, 200 + i * 40);
        }
    }
}
