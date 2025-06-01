import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

public class PlayerData {
    private static final String PLAYER_NAME_KEY = "player_name";
    private static final String COINS_KEY = "player_coins";
    private static final String OWNED_POKEMON_KEY = "owned_pokemon";
    private static final String CURRENT_POKEMON_KEY = "current_pokemon";
    private static final String MUSIC_VOLUME_KEY = "music_volume";
    private static final String SFX_VOLUME_KEY = "sfx_volume";
    private static final String POKEMON_USAGE_KEY = "pokemon_usage";
    private static Preferences prefs = Preferences.userNodeForPackage(PlayerData.class);

    // Default coins jika belum pernah diset
    private static final int DEFAULT_COINS = 50;
    private static final int DEFAULT_VOLUME = 70;

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

    // Pokemon Data
    public static void saveOwnedPokemon(List<String> pokemonNames) {
        String pokemonList = String.join(",", pokemonNames);
        prefs.put(OWNED_POKEMON_KEY, pokemonList);
    }

    public static List<String> getOwnedPokemon() {
        String pokemonList = prefs.get(OWNED_POKEMON_KEY, "");
        if (pokemonList.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(pokemonList.split(",")));
    }

    public static void saveCurrentPokemon(String pokemonName) {
        prefs.put(CURRENT_POKEMON_KEY, pokemonName);
    }

    public static String getCurrentPokemon() {
        return prefs.get(CURRENT_POKEMON_KEY, "");
    }

    public static void clearPokemonData() {
        prefs.remove(OWNED_POKEMON_KEY);
        prefs.remove(CURRENT_POKEMON_KEY);
    }

    // Volume Settings
    public static void saveMusicVolume(int volume) {
        prefs.putInt(MUSIC_VOLUME_KEY, volume);
    }

    public static int getMusicVolume() {
        return prefs.getInt(MUSIC_VOLUME_KEY, DEFAULT_VOLUME);
    }

    public static void saveSFXVolume(int volume) {
        prefs.putInt(SFX_VOLUME_KEY, volume);
    }

    public static int getSFXVolume() {
        return prefs.getInt(SFX_VOLUME_KEY, DEFAULT_VOLUME);
    }

    // Pokemon Usage Tracking
    public static void incrementPokemonUsage(String pokemonName) {
        String usageData = prefs.get(POKEMON_USAGE_KEY, "");
        String[] usagePairs = usageData.split(",");
        boolean found = false;
        StringBuilder newUsageData = new StringBuilder();

        // Update existing usage count or add new entry
        for (String pair : usagePairs) {
            if (!pair.isEmpty()) {
                String[] parts = pair.split(":");
                if (parts[0].equals(pokemonName)) {
                    int count = Integer.parseInt(parts[1]) + 1;
                    if (newUsageData.length() > 0)
                        newUsageData.append(",");
                    newUsageData.append(pokemonName).append(":").append(count);
                    found = true;
                } else {
                    if (newUsageData.length() > 0)
                        newUsageData.append(",");
                    newUsageData.append(pair);
                }
            }
        }

        // If Pokemon wasn't found, add new entry
        if (!found) {
            if (newUsageData.length() > 0)
                newUsageData.append(",");
            newUsageData.append(pokemonName).append(":1");
        }

        prefs.put(POKEMON_USAGE_KEY, newUsageData.toString());
    }

    public static String getMostUsedPokemon() {
        String usageData = prefs.get(POKEMON_USAGE_KEY, "");
        if (usageData.isEmpty())
            return null;

        String[] usagePairs = usageData.split(",");
        String mostUsedPokemon = null;
        int maxUsage = 0;

        for (String pair : usagePairs) {
            if (!pair.isEmpty()) {
                String[] parts = pair.split(":");
                int usage = Integer.parseInt(parts[1]);
                if (usage > maxUsage) {
                    maxUsage = usage;
                    mostUsedPokemon = parts[0];
                }
            }
        }

        return mostUsedPokemon;
    }

    public static void clearAllData() {
        // Clear all preferences
        try {
            prefs.clear();
        } catch (Exception e) {
            // If clear() fails, remove each key individually
            prefs.remove(PLAYER_NAME_KEY);
            prefs.remove(COINS_KEY);
            prefs.remove(OWNED_POKEMON_KEY);
            prefs.remove(CURRENT_POKEMON_KEY);
            prefs.remove(MUSIC_VOLUME_KEY);
            prefs.remove(SFX_VOLUME_KEY);
            prefs.remove(POKEMON_USAGE_KEY);
        }

        // Reset to default values
        setCoins(DEFAULT_COINS);
        saveMusicVolume(DEFAULT_VOLUME);
        saveSFXVolume(DEFAULT_VOLUME);
    }
}