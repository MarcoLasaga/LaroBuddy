import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "save.dat";

    public static void save(Player player) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Player load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (Player) in.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
