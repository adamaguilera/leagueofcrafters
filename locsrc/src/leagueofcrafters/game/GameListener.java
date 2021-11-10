package leagueofcrafters.game;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class GameListener implements Listener {
    final Game game;

    @EventHandler
    public void onPlayerJoin (final PlayerJoinEvent event) {
        game.getUserManager().onPlayerJoin(event, game.getGameState());
    }

    @EventHandler
    public void onPlayerQuit (final PlayerQuitEvent event) {
        game.getUserManager().onPlayerQuit(event);
    }
}
