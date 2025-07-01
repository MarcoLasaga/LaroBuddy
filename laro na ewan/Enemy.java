// File: Enemy.java
import java.awt.*;
import java.io.Serializable;

public class Enemy extends Rectangle implements Serializable {
    private int type;
    private boolean miniBoss;

    public Enemy(int x, int y, int type, boolean miniBoss) {
        super(x, y, 30, 30);
        this.type = type;
        this.miniBoss = miniBoss;
    }

    public Color getColor() {
        return switch (type) {
            case 1 -> Color.CYAN;
            case 2 -> Color.ORANGE;
            case 3 -> Color.GREEN;
            case 4 -> Color.MAGENTA;
            default -> Color.RED;
        };
    }

    public String getPersonality() {
        return switch (type) {
            case 1 -> "Shivers but stands its ground.";
            case 2 -> "Radiates heat and rage!";
            case 3 -> "Smiles peacefully.";
            case 4 -> "Glows with terrifying power.";
            default -> "Seems confused but hostile.";
        };
    }

    public boolean isHealer() { return type == 3; }
    public boolean isMiniBoss() { return miniBoss; }
    public int getEnemyType() { return type; }
}
