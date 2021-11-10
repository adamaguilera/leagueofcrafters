package leagueofcrafters.game;


import leagueofcrafters.team.Team;
import leagueofcrafters.team.config.TeamConfig;
import leagueofcrafters.utils.config.ConfigLoader;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GameConfig {
    public static final Path PATH = Paths.get("./plugins/LeagueofCrafters/game.json");
    private final List<TeamConfig> teamConfigs;

    public void saveConfig () {
        try {
            String json = ConfigLoader.getGson().toJson(this);
            Files.write(PATH, json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Team> createTeams () {
        return teamConfigs.stream().map(TeamConfig::toTeam).collect(Collectors.toList());
    }
}
