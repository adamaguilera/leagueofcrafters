package oldloc.game;

import oldloc.player.User;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class GameListener implements Listener {
    final Game game;

    @EventHandler
    public void onPlayerJoin (final PlayerJoinEvent event) { game.getUserManager().onPlayerJoin(event); }

    @EventHandler
    public void onPlayerInteract (final PlayerInteractEvent event) { game.getPlayerManager().onPlayerInteract(event); }

    @EventHandler
    public void onInventoryClick (final InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID playerID = player.getUniqueId();
        User user = game.getUserManager().getOrCreateUser(playerID);
        user.getHero().getSelectionManager().onInventoryClick(event);
    }
}
