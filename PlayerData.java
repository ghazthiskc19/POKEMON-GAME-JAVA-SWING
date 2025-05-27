// PlayerData.java (File baru)
import java.util.prefs.Preferences;

public class PlayerData {
    private static final String PLAYER_NAME_KEY = "player_name";
    private static Preferences prefs = Preferences.userNodeForPackage(PlayerData.class);

    public static void savePlayerName(String name) {
        prefs.put(PLAYER_NAME_KEY, name);
    }

    public static String getPlayerName() {
        return prefs.get(PLAYER_NAME_KEY, null);
    }

    public static boolean hasPlayerName() {
        return getPlayerName() != null;
    }
}