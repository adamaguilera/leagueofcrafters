package oldloc.game.managers;

import leagueofcrafters.team.TeamName;
import oldloc.Main;
import oldloc.game.Game;
import oldloc.game.Team;
import oldloc.messager.Chat;
import oldloc.player.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TeamManager {
    final String NOT_IN_LOBBY = "You can only do this in the lobby!";
    final Chat chat;

    @NotNull @Getter
    List<Team> teams;


    public TeamManager () {
        this.chat = new Chat();
        this.teams = new ArrayList<>();
        this.teams.add(new Team(Team.TeamName.RADIANT));
        this.teams.add(new Team(Team.TeamName.DIRE));
    }

    public Game getGame () { return Main.GET_GAME(); }

    public Team getNextTeam () {
        Collections.sort(this.teams);
        return this.teams.get(0);
    }

    public void switchTeam (@NotNull final Team team, @NotNull final User user) {
        if (getGame().getState() == Game.GameState.LOBBY) {
            user.getTeam().removeUser(user);
            user.setTeam(team);
            team.addUser(user);
        } else {
            chat.command(user, NOT_IN_LOBBY);
        }
    }
}
