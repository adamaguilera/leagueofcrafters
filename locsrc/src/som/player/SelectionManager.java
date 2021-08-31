package som.player;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import som.chat.ItemText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
public class SelectionManager {
    @NotNull
    final Mage mage;
    final Random random = new Random();
    final Material INFORMATION_PANE = Material.BLACK_STAINED_GLASS_PANE;
    Map<ItemStack, Runnable> actions;
    Inventory selection;

    private Runnable emptyRunnable () {
        return () -> {};
    }
    private ItemStack getInformation () {
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(ItemText.TITLE("Information", ChatColor.GRAY));
        meta.setLore(List.of(
                ItemText.LORE("Click an option to select it!"),
                ItemText.LORE("Closing inventory will randomly"),
                ItemText.LORE("select an option.")));
        glass.setItemMeta(meta);
        return glass;
    }
    public void clear () {
        this.actions = null;
        this.selection = null;
    }

    public void onInventoryClickEvent (final InventoryClickEvent event) {
        if (this.selection != null) {
            event.setCancelled(true);
            if (this.selection.equals(event.getClickedInventory())) {
                ItemStack item = this.selection.getItem(event.getSlot());
                if (item != null && item.getType() != INFORMATION_PANE) {
                    actions.getOrDefault(item, this::forceClose).run();
                    clear();
                    mage.getPlayer().ifPresent(Player::closeInventory);
                }
            }
        }
    }

    private int getInventorySize (final double amount) {
        return (int) Math.ceil(amount / 9) * 9;
    }
    public Inventory generateInventory (final String name, final Map<ItemStack, Runnable> actions) {
        Inventory view = Bukkit.createInventory(mage.getPlayer().orElse(null),
                getInventorySize(actions.keySet().size()), name);
        // TODO Add information pane and automatically format it to be nice
        actions.keySet().forEach(view::addItem);
        return view;
    }

    public void openSelection (final String name, final Map<ItemStack, Runnable> actions) {
        forceClose();
        this.actions = actions;
        this.selection = generateInventory (name, actions);
        mage.getPlayer().ifPresent(player -> player.openInventory(selection));
    }

    public void forceClose () {
        if (this.actions != null && this.actions.size() > 0) {
            List<ItemStack> options = new ArrayList<>(this.actions.keySet());
            ItemStack select = options.get(random.nextInt(options.size()));
            this.actions.getOrDefault(select, emptyRunnable()).run();
            this.clear();
            mage.getPlayer().ifPresent(Player::closeInventory);
        }
    }
}
