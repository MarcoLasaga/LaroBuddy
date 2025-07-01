// File: Player.java
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Player implements Serializable {
    private int x, y, speed, hp, maxHp, xp, level;
    private boolean left, right, up, down, sprinting;
    private String direction = "down";
    private List<Rectangle> obstacles;
    private List<String> inventory;

    public Player(List<Rectangle> obstacles) {
        x = 0; y = 0; speed = 4;
        this.obstacles = obstacles;
        this.maxHp = 10;
        this.hp = 10;
        this.level = 1;
        this.xp = 0;
        this.inventory = new ArrayList<>();
    }

    public void update() {
        int actualSpeed = sprinting ? speed * 2 : speed;
        int nextX = x + (right ? actualSpeed : 0) - (left ? actualSpeed : 0);
        int nextY = y + (down ? actualSpeed : 0) - (up ? actualSpeed : 0);

        if (up) direction = "up";
        if (down) direction = "down";
        if (left) direction = "left";
        if (right) direction = "right";

        Rectangle next = new Rectangle(nextX, nextY, 20, 20);
        for (Rectangle o : obstacles) {
            if (o.intersects(next)) return;
        }
        x = nextX;
        y = nextY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, 20, 20);
        g.setColor(Color.YELLOW);
        g.drawString("Facing: " + direction, x - 10, y - 5);
        g.drawString("LVL: " + level + " XP: " + xp, x - 10, y - 20);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_UP, KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_SHIFT -> sprinting = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_UP, KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_SHIFT -> sprinting = false;
        }
    }

    public void addXP(int value) {
        xp += value;
        if (xp >= level * 10) {
            xp = 0;
            level++;
            maxHp += 2;
            hp = maxHp;
        }
    }

    public void addItem(String item) { inventory.add(item); }

    public List<String> getInventory() { return inventory; }
    public Rectangle getBounds() { return new Rectangle(x, y, 20, 20); }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHP() { return hp; }
    public void setHP(int value) { hp = Math.max(0, Math.min(maxHp, value)); }
    public void resetHealth() { hp = maxHp; }
    public int getLevel() { return level; }

    // 🛠 Emergency fix to validate XP/Level logic & healing bug
    public void debugPrint() {
        System.out.println("XP: " + xp + ", LVL: " + level + ", HP: " + hp);
    }
}
