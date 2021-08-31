package som;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import som.chat.Chat;
import som.player.Mage;
import som.task.TaskManager;
import som.team.Team;
import som.team.TeamName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {
    final int MAX_TEAMS = Math.min(2, TeamName.values().length);
    final boolean TEAM_DAMAGE = false;
    final Chat chat = new Chat ();
    final TaskManager taskManager;
    @Getter
    final int MAX_SCORE = 20;
    @NotNull @Getter
    List<Team> teams;

    public Game () {
        this.teams = new ArrayList<>();
        for (int teamIndex = 0; teamIndex <= MAX_TEAMS; teamIndex ++) {
            teams.add(new Team(getUniqueTeamName().get()));
        }
        this.taskManager = new TaskManager(this);
        this.taskManager.start();
    }



    public Mage getOrCreateMage(@NotNull UUID playerID) {
        Optional<Mage> existingMage = teams.stream()
                .flatMap(team -> team.getMages().stream())
                .filter(mage -> mage.equals(playerID))
                .findFirst();
        return existingMage.orElseGet(() -> initializeMage(playerID));
    }

    public String joinTeamMessage (final Team team) {
        return "You have joined team " + team;
    }

    public Mage initializeMage (@NotNull UUID playerID) {
        Team team = nextTeam();
        Mage mage = new Mage(team, playerID);
        team.addMage(mage);
        mage.clearInventory();
        mage.giveStartingItems();
        chat.commandMessage(playerID, joinTeamMessage(team));
        return mage;
    }

    public Team nextTeam () {
        teams.sort(Team.GET_SIZE_COMPARE());
        Team nextTeam = teams.get(0);
        Collections.sort(teams);
        return nextTeam;
    }

    private Set<TeamName> getTeamNames () {
        return teams.stream().map(Team::getName).collect(Collectors.toSet());
    }
    private Optional<TeamName> getUniqueTeamName () {
        Set<TeamName> existingNames = getTeamNames();
        return Stream.of(TeamName.values())
                .filter(name -> !existingNames.contains(name))
                .findAny();
    }
}
