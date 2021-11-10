package leagueofcrafters.utils.chat;

import org.bukkit.Bukkit;

public class Log {
    public static void INFO (final String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    public static void BROADCAST(final String message) {
        Bukkit.broadcastMessage(message);
    }
}
