package leagueofcrafters.team.config;

import leagueofcrafters.team.TeamName;
import leagueofcrafters.team.Tower;
import leagueofcrafters.utils.config.Coordinate;
import leagueofcrafters.utils.config.RGB;
import leagueofcrafters.utils.config.Space;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

@Data
public class TowerConfig {
    final Coordinate center;
    final Space region;
    final RGB dust;
    final int maxHealth;
    final String towerName;

    public Location getCenter() {
        return this.center.toLocation();
    }

    public Tower toTower (@NotNull final TeamName teamName) { return new Tower(teamName, this, 1); }
}
