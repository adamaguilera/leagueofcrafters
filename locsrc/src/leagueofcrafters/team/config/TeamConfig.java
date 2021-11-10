package leagueofcrafters.team.config;

import leagueofcrafters.team.Team;
import leagueofcrafters.team.TeamName;
import leagueofcrafters.utils.config.Coordinate;
import lombok.Data;

@Data
public class TeamConfig {
    final TeamName teamName;
    final Coordinate spawn;
    final TowerConfig towerConfig;

    public Team toTeam () {
        return new Team(teamName, towerConfig.toTower(teamName), spawn.toLocation());
    }
}
