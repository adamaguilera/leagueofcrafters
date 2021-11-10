package oldloc.messager;

import oldloc.player.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class Chat {

    public void broadcast (final String message) {
        Bukkit.broadcastMessage(formatBroadcast(message));
    }
    public String formatBroadcast (final String message) {
        return "" + ChatColor.LIGHT_PURPLE + message + ChatColor.WHITE;
    }

    public void command (@NotNull final User user, final String message) {
        user.getPlayer().ifPresent(player -> player.sendMessage(formatCommand(message)));
    }
    public String formatCommand (final String message) {
        return "" + ChatColor.LIGHT_PURPLE + "[LOC] - " + message + ChatColor.WHITE;
    }

    public void ability (@NotNull final User user, final String message) {
        user.getPlayer().ifPresent(player -> player.sendMessage(formatAbility(message)));
    }
    public String formatAbility (final String message) {
        return "" + ChatColor.LIGHT_PURPLE + message + ChatColor.WHITE;
    }
}
