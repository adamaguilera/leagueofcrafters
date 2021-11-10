package leagueofcrafters.utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import leagueofcrafters.game.GameConfig;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;

@Data
public class ConfigLoader {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private GameConfig gameConfig;

    public void loadAll() {
        gameConfig = loadGameConfig();
    }

    private GameConfig loadGameConfig() {
        try {
            String json = new String(Files.readAllBytes(GameConfig.PATH));
            // validateConfig ()
            return GSON.fromJson(json, GameConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Gson getGson () { return GSON; }
}

