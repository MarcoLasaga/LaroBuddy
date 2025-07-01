// File: BattleScene.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BattleScene {
    private final String[] options = {"Attack", "Talk", "Spare"};
    private int selected = 0, enemyHP, playerHP;
    private final boolean healer, miniBoss;
    private boolean finished = false, playerTurn = true, showEffect = false, correctRiddle = false;
    private int effectTimer = 0;
    private final Player player;
    private final Enemy enemy;
    private final Random rand = new Random();

    public BattleScene(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.healer = enemy.isHealer();
        this.miniBoss = enemy.isMiniBoss();
        this.playerHP = player.getHP();
        this.enemyHP = miniBoss ? 12 : healer ? 1 : 6;
    }

    public void update() {
        enemyHP = Math.max(0, enemyHP);

        if (showEffect && --effectTimer <= 0) showEffect = false;

        // Ensure battle finishes as soon as enemyHP <= 0
        if (!finished && enemyHP <= 0) {
            finished = true;
            if (healer && correctRiddle) player.setHP(player.getHP() + 5);
            player.addXP(miniBoss ? 10 : 3);
            if (miniBoss) player.addItem("Key");
        } else if (playerHP <= 0 && !finished) {
            finished = true;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 640, 480);
        g.setColor(enemy.getColor());
        g.fillRect(280, 180, 80, 80);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier New", Font.BOLD, 18));
        g.drawString("Enemy HP: " + Math.max(0, enemyHP), 240, 150);
        g.drawString("Your HP: " + Math.max(0, playerHP), 240, 130);
        g.drawString(enemy.getPersonality(), 200, 100);
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == selected ? Color.YELLOW : Color.LIGHT_GRAY);
            g.drawString(options[i], 180 + i * 120, 400);
        }
        if (showEffect) {
            g.setColor(Color.ORANGE);
            g.fillRect(290, 190, 60, 60);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (!playerTurn || finished) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> selected = (selected + options.length - 1) % options.length;
            case KeyEvent.VK_RIGHT -> selected = (selected + 1) % options.length;
            case KeyEvent.VK_1 -> { if (player.getLevel() >= 2) enemyHP -= 2; }
            case KeyEvent.VK_2 -> { if (player.getLevel() >= 3) playerHP += 2; }
            case KeyEvent.VK_3 -> { if (player.getLevel() >= 4) enemyHP = 1; }
            case KeyEvent.VK_Z -> {
                if (enemyHP <= 0) return;
                if (enemyHP > 0 && healer && !correctRiddle) {
                    correctRiddle = askRiddle();
                    return;
                }
                if (options[selected].equals("Attack")) {
                    int damage = rand.nextInt(3) + 1;
                    if (rand.nextDouble() < 0.2) damage += 2;
                    enemyHP -= damage;
                    showEffect = true;
                    effectTimer = 10;
                } else if (options[selected].equals("Talk")) {
                    enemyHP--;
                } else if (options[selected].equals("Spare")) {
                    enemyHP = 0;
                }
                playerTurn = false;
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    public void run() {
                        if (enemyHP > 0 && !finished) playerHP -= rand.nextInt(2) + 1;
                        playerTurn = true;
                    }
                }, 500);
            }
        }
    }

    private boolean askRiddle() {
        String answer = JOptionPane.showInputDialog("Riddle: What has keys but can't open locks?");
        return answer != null && answer.toLowerCase().contains("piano");
    }

    public boolean isFinished() { return finished; }
    public int getPlayerHP() { return Math.max(playerHP, 0); }
}
