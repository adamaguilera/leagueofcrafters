package oldloc.game.managers;

import oldloc.game.Game;
import oldloc.player.Hero;
import oldloc.player.User;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class PlayerManager {
    final Game game;

    public void onPlayerInteract (PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();
        User user = game.getUserManager().getOrCreateUser(playerID);
        Hero hero = user.getHero();
        hero.getSpellManager().getSpells().stream()
                .filter(spell -> spell.eventCasts(event))
                .forEach(spell -> {
                    if (spell.safeCast() || spell.cancelEvent()) event.setCancelled(true);
                });;
    }
}
