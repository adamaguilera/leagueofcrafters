package oldloc.messager;

import org.bukkit.Bukkit;

public class Log {
    public static void INFO (final String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
}
