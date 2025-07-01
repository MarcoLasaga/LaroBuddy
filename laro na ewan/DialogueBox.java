import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;

public class DialogueBox {
    private ArrayList<String> lines;
    private int currentLine;
    private boolean active;

    public DialogueBox(String path) {
        lines = new ArrayList<>();
        currentLine = 0;
        active = true;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            lines.add("[Missing dialogue file]");
        }
    }

    public void draw(Graphics g) {
        if (!active) return;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(50, 350, 540, 100);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(lines.get(currentLine), 60, 390);
    }

    public void keyPressed(KeyEvent e) {
        if (!active) return;
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            currentLine++;
            if (currentLine >= lines.size()) active = false;
        }
    }

    public boolean isActive() {
        return active;
    }
}
