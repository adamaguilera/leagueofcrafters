package leagueofcrafters.user;

import leagueofcrafters.game.Game;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class UserManager {
    @Getter
    Map<UUID, User> users = new HashMap<>();

    public void onPlayerJoin (final PlayerJoinEvent event, Game.GameState state) {
        User user = getUser(event.getPlayer().getUniqueId());
        user.spectatorDefault();
    }

    public void onPlayerQuit (final PlayerQuitEvent event) {
        users.remove(event.getPlayer().getUniqueId());
    }

    public User createUser (final UUID id) { return new User(id); }

    public User getUser (final UUID id) {
        if (!users.containsKey(id)) {
            users.put(id, createUser(id));
        }
        return users.get(id);
    }

    public List<User> getUsersList () { return users.values().stream().toList(); }
    public int size () { return users.size(); }
}
