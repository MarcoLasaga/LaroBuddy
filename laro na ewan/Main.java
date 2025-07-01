// File: Main.java
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("EchoWorld Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        while (true) {
            SplashScreen splash = new SplashScreen();
            frame.setContentPane(splash);
            frame.setVisible(true);
            while (!splash.isFinished()) {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }

            String choice = splash.getChoice();
            if (choice.equals("Quit")) System.exit(0);

            if (choice.equals("Settings")) {
                SettingsMenu settings = new SettingsMenu();
                frame.setContentPane(settings);
                settings.requestFocusInWindow();
                while (!settings.isFinished()) {
                    try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                }
                continue; // go back to splash
            }

            if (choice.equals("Play")) {
                GamePanel game = new GamePanel();
                frame.setContentPane(game);
                frame.revalidate();
                frame.repaint();
                break; // run game
            }
        }
    }
}
