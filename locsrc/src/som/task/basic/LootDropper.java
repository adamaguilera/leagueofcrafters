package som.task.basic;

import lombok.Getter;
import som.chat.Chat;
import som.player.Mage;
import som.task.Task;
import som.team.Team;

import java.util.List;

public class LootDropper implements Task {
    @Getter
    static LootDropper instance;
    final List<Team> teams;
    final Chat chat = new Chat();
    final int INITIAL_DELAY = 120*1000;
    final int BUFFER_DELAY = 60 * 1000;
    final int CRATE_DELAY = 120*1000;
    final int SCROLL_DELAY = 240*1000;
    final String SCROLL_DROP_MSG = "All players have been dropped a spell scroll!";
    final String CRATE_DROP_MSG = "All players have been dropped a crate!";
    long nextCrateDrop = System.currentTimeMillis() + INITIAL_DELAY;
    long nextScrollDrop = System.currentTimeMillis() + INITIAL_DELAY + BUFFER_DELAY + SCROLL_DELAY;


    public LootDropper (final List<Team> teams) {
        instance = this;
        this.teams = teams;
    }

    public int nextDrop () {
        long nextDrop = Math.min(nextScrollDrop, nextCrateDrop);
        return (int) ((nextDrop - System.currentTimeMillis()) / 1000);
    }

    private boolean shouldDropScroll () {
        return System.currentTimeMillis() >= nextScrollDrop;
    }

    private boolean shouldDropCrate () {
        return System.currentTimeMillis() >= nextCrateDrop;
    }

    @Override
    public void onTick() {
        if (shouldDropScroll()) {
            chat.globalMessage(SCROLL_DROP_MSG);
            teams.stream().flatMap(team -> team.getMages().stream())
                    .forEach(Mage::dropScroll);
            this.nextScrollDrop = System.currentTimeMillis() + SCROLL_DELAY;
        }
        if (shouldDropCrate()) {
            chat.globalMessage(CRATE_DROP_MSG);
            teams.stream().flatMap(team -> team.getMages().stream())
                    .forEach(Mage::dropCrate);
            this.nextCrateDrop = System.currentTimeMillis() + CRATE_DELAY;
        }
    }
}
