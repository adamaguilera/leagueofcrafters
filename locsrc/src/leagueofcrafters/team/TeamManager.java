package leagueofcrafters.team;

import leagueofcrafters.user.User;
import leagueofcrafters.utils.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TeamManager {
    @Getter
    final List<Team> teams;


    public List<Task> getTasks () { return teams.stream().map(team -> (Task) team).collect(Collectors.toList()); }
    public void randomizeTeamOrder () { Collections.shuffle(this.teams); }
    public Team nextTeam () {
        randomizeTeamOrder();
        return teams.stream().sorted().findFirst().get();
    }
    public void spawnTowers () { teams.forEach(team -> team.getTower().spawn()); }
    public void assignEnemies () { teams.forEach(team -> assignEnemy(team).ifPresent(team::setEnemy)); }
    public Optional<Team> assignEnemy (@NotNull final Team team) {
        randomizeTeamOrder();
        return this.teams.stream().filter(enemy -> !enemy.equals(team)).findFirst();
    }

    public Optional<Team> getTeam (@NotNull final User user) {
        return teams.stream().filter(team -> team.getUser(user).isPresent()).findFirst();
    }

    public Optional<Team> getTeam (@NotNull final UUID id) {
        return teams.stream().filter(team -> team.onTeam(id)).findFirst();
    }

    public Optional<Team> getTeam (@NotNull final TeamName name) {
        return teams.stream().filter(team -> team.getName().equals(name)).findFirst();
    }

    public void assignPlayers (final List<User> users) {
        users.stream()
                .filter(user -> getTeam(user).isEmpty())
                .forEach(user -> nextTeam().addUser(user));
    }

    public void onDisable () { this.teams.forEach(Team::onDisable); }
}
