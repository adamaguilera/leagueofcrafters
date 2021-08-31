package som;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import som.chat.Chat;
import som.player.Mage;
import som.team.Team;

import java.util.UUID;

@RequiredArgsConstructor
public class GameListener implements Listener{
    @NotNull
    final Game game;
    final Chat chat = new Chat();

    /*
    LOGIN EVENT
     */


    @EventHandler
    public void onPlayerJoin (final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerID = event.getPlayer().getUniqueId();
        Mage mage = game.getOrCreateMage(playerID);
    }
    @EventHandler
    public void onPlayerLeave (final PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerID = event.getPlayer().getUniqueId();
        Mage mage = game.getOrCreateMage(playerID);
        Team team = mage.getTeam();
    }
    /*
    END LOGIN EVENT
     */

    /*
    Player Event
     */
    @EventHandler
    public void onEntityDeathEvent (EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            UUID playerID = player.getUniqueId();
            Mage mage = game.getOrCreateMage(playerID);
            Player killer = player.getKiller();
            if (killer != null) {
                UUID killerID = killer.getUniqueId();
                Mage killerMage = game.getOrCreateMage(killerID);
                if (mage.sameTeam(killerMage)) {
                    mage.getTeam().incPenalty();
                    chat.teamKillMsg(killerMage, mage);
                } else {
                    killerMage.onKill(mage);
                    mage.onDeath(killerMage);
                    chat.enemyKillMsg(killerMage, mage);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            UUID playerID = player.getUniqueId();
            Mage mage = game.getOrCreateMage(playerID);
            mage.getDamageManager().onEntityDamageByEntity(event);
        }
    }

    @EventHandler
    public void onPlayerInteract (final PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerID = event.getPlayer().getUniqueId();
        Mage mage = game.getOrCreateMage(playerID);
        mage.getSpellManager().getSpells().stream()
                .filter(spell -> spell.eventCastsAbility(event))
                .forEach(spell -> {
                    if (spell.attemptCast() || spell.cancelEvent()) event.setCancelled(true);
                });
    }
    @EventHandler
    public void onInventoryClick (final InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID playerID = player.getUniqueId();
        Mage mage = game.getOrCreateMage(playerID);
        mage.getSelectionManager().onInventoryClickEvent(event);
    }

    @EventHandler
    public void onInventoryClose (final InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID playerID = player.getUniqueId();
        Mage mage = game.getOrCreateMage(playerID);
        mage.getSelectionManager().forceClose();
    }
    /*
    END PLAYER EVENT
     */
}
