package som.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import som.chat.Chat;
import som.player.Mage;
import som.task.basic.LootDropper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Team implements Comparable<Team>{
    @NotNull @Getter
    final TeamName name;
    @Getter
    final List<Mage> mages = new ArrayList<>();
    final Chat chat = new Chat();
    final int MAX_SCORE = 20;
    final int MAX_TIER = 3;
    final int TIER_3 = 13;
    final int TIER_2 = 6;
    int kills = 0;
    int penalties = 0;

    public boolean addMage (final Mage mage) {
        return mages.add(mage);
    }
    public boolean removeMage (final Mage mage) {return mages.remove(mage); }

    public boolean onTeam (final Mage player) {
        return mages.contains(player);
    }

    public int getTier () {
        return getHighestScore() >= TIER_3 ? 3 :
                getHighestScore() >= TIER_2 ? 2 : 1;
    }

    public int getSize () {
        return mages.size();
    }

    public void whisper (@NotNull final String message) {
        mages.forEach(mage ->
                mage.getPlayer().ifPresent(player ->
                        chat.commandMessage(player.getUniqueId(), message)));
    }

    @Override
    public String toString () {
        return name.toString().replace("_", " ");
    }

    public List<String> getScoreboardText () {
        return List.of(
                "Tier - (" + this.getTier() + "/" + MAX_TIER + ")",
                "Kills - (" + this.getKills() + "/" + (MAX_SCORE + penalties) + ")",
                "Emeralds - (" + getEmeralds() + "/" + (MAX_SCORE + penalties) + ")",
                "Penalties - " + this.penalties,
                "Next Drop in " + LootDropper.getInstance().nextDrop() + "s");
    }

    public Scoreboard getScoreboard () {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("som", "dummy", toString());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        List<String> text = getScoreboardText();
        for (int index = 0; index < text.size(); index++) {
            obj.getScore(text.get(index)).setScore(text.size() - index);
        }
        return board;
    }
    public void updateTeamScoreboards() {
        this.mages.forEach(mage -> {
            mage.getPlayer().ifPresent(player ->
                    player.setScoreboard(getScoreboard()));
        });
    }

    public int incPenalty () { return ++this.penalties; }
    public int getKillScore () {
        return this.getKills() - this.penalties;
    }
    public int getKills () {
        return mages.stream().mapToInt(Mage::getKills).sum();
    }
    public int getEmeralds () {
        return mages.stream().mapToInt(mage -> mage.getPlayer().isPresent() ?
             Stream.of(mage.getPlayer().get().getInventory().getContents())
                    .filter(item -> item != null &&
                            item.getType() == Material.EMERALD)
                    .mapToInt(ItemStack::getAmount)
                     .sum()
            : 0).sum();
    }
    public int getEmeraldScore () {
        return getEmeralds() - this.penalties;
    }
    public int getHighestScore () {
        return Math.max(getKillScore(), getEmeraldScore());
    }
    public boolean hasWon () {
        return getHighestScore() >= MAX_SCORE;
    }

    @Override
    public int compareTo(@NotNull Team o) {
        return this.getHighestScore() != o.getHighestScore() ?
                Integer.compare(this.getHighestScore(), o.getHighestScore()) :
                this.name.toString().compareTo(o.name.toString());
    }

    public static Comparator<Team> GET_SIZE_COMPARE () {
        return (o1, o2) -> o1.getSize() != o2.getSize() ?
                Integer.compare(o1.getSize(), o2.getSize()) :
                o1.name.toString().compareTo(o2.name.toString());
    }
}
