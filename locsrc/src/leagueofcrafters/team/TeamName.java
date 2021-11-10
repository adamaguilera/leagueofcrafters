package leagueofcrafters.team;

import leagueofcrafters.Main;

import java.util.Optional;

public enum TeamName {
    BLUE,
    RED;

    public Optional<Team> toTeam () {
        return Main.GET_TEAM(this);
    }
}
