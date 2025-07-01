// File: GamePanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running;
    private Player player;
    private ArrayList<Rectangle> terrain;
    private ArrayList<Enemy> enemies;
    private ArrayList<Rectangle> gates;
    private InventoryPanel inventoryPanel;
    private BattleScene battleScene;
    private boolean inBattle = false;
    private boolean isDead = false;
    private int camX, camY;
    private Random rand = new Random();
    private int encounterCount = 0;
    private String biomeName = "Start Zone";

    public GamePanel() {
        setPreferredSize(new Dimension(640, 480));
        setFocusable(true);
        SwingUtilities.invokeLater(this::requestFocusInWindow);
        addKeyListener(this);

        terrain = new ArrayList<>();
        enemies = new ArrayList<>();
        gates = new ArrayList<>();
        inventoryPanel = new InventoryPanel();

        generateTerrain();
        generateEnemies();
        generateGates();

        player = new Player(terrain);
    }

    public void addNotify() {
        super.addNotify();
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void run() {
        running = true;
        while (running) {
            update();
            repaint();
            try { Thread.sleep(16); } catch (Exception ignored) {}
        }
    }

    private void update() {
        if (player == null || isDead) return;
        if (player.getHP() <= 0) {
            isDead = true;
            return;
        }

        if (inBattle) {
            battleScene.update();
            if (battleScene.isFinished()) {
                player.setHP(battleScene.getPlayerHP());
                inBattle = false;
                generateEnemies();
            }
        } else {
            player.update();
            camX = player.getX() - 320;
            camY = player.getY() - 240;

            updateBiome();

            for (Rectangle gate : gates) {
                if (!inBattle && player.getBounds().intersects(gate) && !player.getInventory().contains("Key")) {
                    player.setHP(player.getHP() - 1);
                }
            }

            for (Enemy enemy : enemies) {
                if (player.getBounds().intersects(enemy)) {
                    inBattle = true;
                    boolean isHealer = ++encounterCount % 10 == 0;
                    battleScene = new BattleScene(player, new Enemy(enemy.x, enemy.y, enemy.getEnemyType(), enemy.isMiniBoss() || isHealer));
                    break;
                }
            }

            if (enemies.size() < 10) generateEnemies();
            if (terrain.size() < 50) generateTerrain();
        }
    }

    private void updateBiome() {
        int px = player.getX();
        int py = player.getY();
        if (px > 900) biomeName = "Forest Biome";
        else if (px < -900) biomeName = "Ice Biome";
        else if (py > 900) biomeName = "Fire Biome";
        else biomeName = "Start Zone";
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(-camX, -camY);

        g.setColor(Color.BLACK);
        g.fillRect(camX, camY, 640, 480);

        for (Rectangle t : terrain) {
            g.setColor(Color.GRAY);
            g.fillRect(t.x, t.y, t.width, t.height);
        }

        for (Enemy e : enemies) {
            g.setColor(e.getColor());
            g.fillRect(e.x, e.y, e.width, e.height);
        }

        for (Rectangle gate : gates) {
            g.setColor(Color.MAGENTA);
            g.fillRect(gate.x, gate.y, gate.width, gate.height);
        }

        if (!inBattle && player != null && !isDead) player.draw(g);
        g2d.translate(camX, camY);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("HP: " + (player != null ? player.getHP() : 0), 10, 20);
        g.setColor(switch (biomeName) {
            case "Forest Biome" -> Color.GREEN;
            case "Ice Biome" -> Color.CYAN;
            case "Fire Biome" -> Color.ORANGE;
            default -> Color.WHITE;
        });
        g.drawString("Zone: " + biomeName, 10, 40);

        if (!inBattle && inventoryPanel != null && !isDead) inventoryPanel.draw(g);
        if (inBattle && battleScene != null) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
            battleScene.draw(g);
        }

        if (isDead) {
            g.setColor(new Color(0, 0, 0, 220));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED);
            g.setFont(new Font("Serif", Font.BOLD, 32));
            g.drawString("YOU DIED", 240, 200);
            g.setFont(new Font("SansSerif", Font.PLAIN, 18));
            g.drawString("Press R to Restart or Q to Quit", 180, 250);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (isDead) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                player = new Player(terrain);
                isDead = false;
            } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                topFrame.setContentPane(new IntroScreen());
                topFrame.revalidate();
            }
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_I) {
            inventoryPanel.toggle(player.getInventory());
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save.dat"))) {
                out.writeObject(player);
                System.out.println("Game Saved");
            } catch (IOException ex) { ex.printStackTrace(); }
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("save.dat"))) {
                Player loaded = (Player) in.readObject();
                if (loaded != null) player = loaded;
                System.out.println("Game Loaded");
            } catch (Exception ex) { ex.printStackTrace(); }
        }

        if (!inBattle && player != null) {
            player.keyPressed(e);
        } else if (inBattle) {
            battleScene.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (!inBattle && player != null && !isDead) {
            player.keyReleased(e);
        }
    }

    public void keyTyped(KeyEvent e) {}

    private void generateTerrain() {
        for (int i = 0; i < 20; i++) {
            terrain.add(new Rectangle(rand.nextInt(4000) - 2000, rand.nextInt(4000) - 2000, 40, 40));
        }
    }

    private void generateEnemies() {
        for (int i = 0; i < 5; i++) {
            int type = rand.nextInt(3);
            boolean isMini = rand.nextDouble() < 0.1;
            enemies.add(new Enemy(rand.nextInt(4000) - 2000, rand.nextInt(4000) - 2000, type, isMini));
        }
    }

    private void generateGates() {
        gates.add(new Rectangle(1000, 0, 40, 100));
        gates.add(new Rectangle(-1000, 0, 40, 100));
        gates.add(new Rectangle(0, 1000, 100, 40));
    }
}
