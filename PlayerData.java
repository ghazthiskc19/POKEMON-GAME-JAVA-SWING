
import java.util.prefs.Preferences;

public class PlayerData {
    private static final String PLAYER_NAME_KEY = "player_name";
    private static final String COINS_KEY = "player_coins";
    private static Preferences prefs = Preferences.userNodeForPackage(PlayerData.class);

    // Default coins jika belum pernah diset
    private static final int DEFAULT_COINS = 100;

    public static void savePlayerName(String name) {
        prefs.put(PLAYER_NAME_KEY, name);
    }

    public static String getPlayerName() {
        return prefs.get(PLAYER_NAME_KEY, null);
    }

    public static boolean hasPlayerName() {
        return getPlayerName() != null;
    }

    public static void clearPlayerName() {
        prefs.remove(PLAYER_NAME_KEY);
    }

    // Koin
    public static int getCoins() {
        return prefs.getInt(COINS_KEY, DEFAULT_COINS);
    }

    public static void setCoins(int coins) {
        prefs.putInt(COINS_KEY, coins);
    }

    public static void spendCoins(int amount) {
        int coins = getCoins();
        if (coins >= amount) {
            setCoins(coins - amount);
        }
    }

    public static void addCoins(int amount) {
        setCoins(getCoins() + amount);
    }
}