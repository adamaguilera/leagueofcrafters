package som.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import som.player.Mage;

import java.util.UUID;

public class Chat {

    private void sendMessage (final UUID playerID,
                              final String formattedMessage) {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) return;
        player.sendMessage(formattedMessage);
    }

    public void commandMessage (final UUID playerID, final String message) {
        sendMessage(playerID, formatCommandMessage(message));
    }

    public void abilityMessage (final UUID playerID, final String message) {
        sendMessage(playerID, formatCommandMessage(message));
    }

    private String formatCommandMessage (final String message) {
        return ChatColor.LIGHT_PURPLE + message + ChatColor.WHITE;
    }

    public void globalMessage (final String message) {
        Bukkit.broadcastMessage(formatGlobalMessage(message));
    }

    private String formatGlobalMessage (final String message) {
        return ChatColor.LIGHT_PURPLE + "SOM - " + message + ChatColor.WHITE;
    }

    public void teamKillMsg (final Mage killer, final Mage victim) {
        killer.getPlayer().ifPresent(killerPlayer ->
                victim.getPlayer().ifPresent(victimPlayer ->
                        Bukkit.broadcastMessage(formatTeamKillMsg(killerPlayer, victimPlayer))
        ));
    }
    private String formatTeamKillMsg (final Player killer, final Player victim) {
        return ChatColor.LIGHT_PURPLE + killer.getDisplayName() + " has killed his own friend " + victim.getDisplayName() + "!";
    }

    public void enemyKillMsg (final Mage killer, final Mage victim) {
        killer.getPlayer().ifPresent(killerPlayer ->
                victim.getPlayer().ifPresent(victimPlayer ->
                        Bukkit.broadcastMessage(formatEnemyKillMsg(killerPlayer, victimPlayer))
                ));
    }
    private String formatEnemyKillMsg (final Player killer, final Player victim) {
        return ChatColor.LIGHT_PURPLE + victim.getDisplayName() + " just got pwned by " + killer.getDisplayName() + "!";
    }

    public void gameMessage (final UUID playerID, final String message) {
        sendMessage(playerID, formatGameMessage(message));
    }

    private String formatGameMessage (final String message) {
        return ChatColor.LIGHT_PURPLE + "SOM - " + message + ChatColor.WHITE;
    }
}
