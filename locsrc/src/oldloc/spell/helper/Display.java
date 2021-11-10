package oldloc.spell.helper;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public record Display(Material type, int amount, String name, List<String> lore) {
    private List<String> formatLore (final List<String> unformatted) {
        List<String> res = new ArrayList<>();
        for (int index = 0; index < lore.size() - 1; index++) {
            res.add("" + ChatColor.WHITE + ChatColor.GRAY + lore.get(index) + ChatColor.WHITE);
        }
        res.add("" + ChatColor.RED + lore.get(lore.size() - 1) + ChatColor.WHITE);
        return res;
    }

    private String formatName (final String name) {
        return "" + ChatColor.WHITE + ChatColor.GRAY + name + ChatColor.WHITE;
    }

    public ItemStack toItem () {
        ItemStack display = new ItemStack(type, amount);
        ItemMeta meta = display.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(formatName(name));
            meta.setLore(formatLore(lore));
            display.setItemMeta(meta);
        }
        return display;
    }
}
