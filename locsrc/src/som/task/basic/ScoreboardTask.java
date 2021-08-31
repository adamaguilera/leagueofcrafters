package som.task.basic;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;
import som.player.Mage;
import som.task.Task;
import som.team.Team;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ScoreboardTask implements Task {
    @NotNull
    final List<Team> teams;
    final int MAX_SCORE;
    Team winner;

    public String getWinMessage (@NotNull final Team team) {
        return ChatColor.RED + "" + team + " has been declared the winner! GG!";
    }

    public void win (@NotNull final Team winner) {
        Bukkit.broadcastMessage(getWinMessage(winner));
        teams.stream()
                .filter(team -> team != winner)
                .flatMap(team -> team.getMages().stream())
                .map(Mage::getPlayer)
                .forEach(mage ->
                        mage.ifPresent(player ->
                                player.setGameMode(GameMode.SPECTATOR)));
    }

    @Override
    public void onTick() {
        if (winner == null) {
            teams.sort(Collections.reverseOrder());
            teams.stream().filter(team -> team.getSize() > 0).forEach(Team::updateTeamScoreboards);
            if (teams.size() > 0 && teams.get(0).getHighestScore() >= MAX_SCORE) {
                this.winner = teams.get(0);
                win (this.winner);
            }
        }
    }
}
