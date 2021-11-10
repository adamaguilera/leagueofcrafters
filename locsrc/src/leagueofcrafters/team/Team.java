package leagueofcrafters.team;

import leagueofcrafters.user.User;
import leagueofcrafters.utils.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class Team implements Comparable<Team>, Task {
    @Getter
    final TeamName name;
    @Getter
    final Tower tower;
    @Getter
    final Location spawn;

    @Getter
    List<User> users = new ArrayList<>();
    @Getter
    Set<UUID> mobs = new HashSet<>();
    @Getter @Setter
    Team enemy;

    @Override
    public void onTick () {
        this.tower.onTick();
        Scoreboard teamBoard = getScoreboard();
        users.forEach(user -> user.getPlayer().ifPresent(player -> player.setScoreboard(teamBoard)));
    }

    public int size () { return users.size();}
    public Optional<User> getUser (@NotNull UUID id) { return users.stream().filter(user -> user.getId().equals(id)).findAny(); }
    public Optional<User> getUser (@NotNull User user) { return getUser(user.getId()); }
    public boolean onTeam (@NotNull UUID id) { return getUser(id).isPresent() || mobs.contains(id); }

    public String notifyJoin(@NotNull User joiningUser) { return "" + joiningUser + " has joined your team!"; }
    public void addUser (@NotNull User user) {
        users.forEach(ally -> ally.command(notifyJoin(user)));
        users.add(user);
        user.command("You have joined team " + this + "!");
    }

    public Scoreboard getScoreboard () {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("team", "dummy", toString());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        int score = 2;
        obj.getScore(enemy.getTower().getObjectiveName()).setScore(score--);
        obj.getScore(enemy.getTower().getProgressBar()).setScore(score--);
        return board;
    }

    @Override
    public String toString () {
        String res = name.toString();
        return Character.toUpperCase(res.charAt(0)) + res.substring(1);
    }

    @Override
    public int compareTo(@NotNull Team o) {
        if (getName().equals(o.name)) return 0;
        return Integer.compare(getUsers().size(), o.getUsers().size());
    }

    public void onDisable () {
        this.tower.onDisable();
    }
}
