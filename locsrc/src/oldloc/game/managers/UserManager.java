package oldloc.game.managers;

import oldloc.Main;
import oldloc.game.Game;
import oldloc.player.User;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {
    @Getter
    List<User> users = new ArrayList<>();

    public Game getGame () {
        return Main.GET_GAME();
    }

    public User createUser (final UUID playerID) {
        User user = new User(playerID, getGame().getTeamManager().getNextTeam());
        this.users.add(user);
        return user;
    }

    public User getOrCreateUser (final UUID playerID) {
        return this.users.stream().filter(user -> user.getId() == playerID)
                .findFirst().orElseGet(() -> createUser(playerID));
    }

    private void lobby (final User user) {
        user.reset();
        user.getPlayer().ifPresent(player -> player.setGameMode(GameMode.ADVENTURE));
        user.getHero().getSpellManager().giveAllSpellDisplays();
    }

    private void login (final User user) {
        if (getGame().getState() == Game.GameState.LOBBY) {
            lobby(user);
        }
    }

    public void onPlayerJoin (final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = getOrCreateUser(player.getUniqueId());
        login(user);
    }

    public void onPlayerQuit (final PlayerQuitEvent event) {

    }
}
