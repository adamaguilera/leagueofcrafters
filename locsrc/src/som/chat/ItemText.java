package som.chat;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ItemText {
    public static String LORE (@NotNull final String lore) {
        return "" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + lore;
    }
    public static String CD (@NotNull final String lore) {
        return "" + ChatColor.RESET + ChatColor.RED + lore;
    }
    public static String TITLE (@NotNull final String title) {
        return "" + ChatColor.RESET + ChatColor.GOLD + title;
    }
    public static String TITLE (@NotNull final String title, final ChatColor color) {
        return "" + ChatColor.RESET + color + title;
    }
    public static List<String> FORMAT_LORE (@NotNull final List<String> lore)  {
        List<String> formatted = new ArrayList<>();
        for (int index = 0; index < lore.size() - 1; index++) {
            formatted.add(LORE(lore.get(index)));
        }
        if (lore.size() > 0) formatted.add(CD(lore.get(lore.size() - 1)));
        return formatted;
    }
}
