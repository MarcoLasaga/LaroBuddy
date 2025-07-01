// File: InventoryPanel.java
import java.awt.*;
import java.util.List;

public class InventoryPanel {
    private boolean visible = false;
    private List<String> items;

    public void toggle(List<String> items) {
        this.items = items;
        visible = !visible;
    }

    public void draw(Graphics g) {
        if (!visible || items == null) return;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(100, 100, 440, 280);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Inventory", 280, 130);
        for (int i = 0; i < items.size(); i++) {
            g.drawString("- " + items.get(i), 140, 160 + i * 20);
        }
    }
}