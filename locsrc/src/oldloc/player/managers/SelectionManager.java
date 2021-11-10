package oldloc.player.managers;

import oldloc.player.User;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
public class SelectionManager {
    @NotNull
    final User user;
    final Random random = new Random();
    Map<ItemStack, Runnable> actions;
    Inventory selection;
    boolean randomOnForceClose;

    public void clear () {
        this.actions = null;
        this.selection = null;
        randomOnForceClose = false;
    }

    public void forceClose () {
        if (this.actions != null && this.actions.size() > 0) {
            if (randomOnForceClose) {
                List<ItemStack> options = new ArrayList<>(this.actions.keySet());
                ItemStack select = options.get(random.nextInt(options.size()));
                this.actions.getOrDefault(select, () -> {}).run();
            }
            this.clear();
            user.getPlayer().ifPresent(Player::closeInventory);
        }
    }

    private int getInventorySize (final double amount) {
        return (int) Math.ceil(amount / 9) * 9;
    }

    private Inventory generateInventory (final String name, final Map<ItemStack, Runnable> actions) {
        Inventory view = Bukkit.createInventory(user.getPlayer().orElse(null),
                getInventorySize(actions.keySet().size()), name);
        // TODO Add information pane and automatically format it to be nice
        actions.keySet().forEach(view::addItem);
        return view;
    }

    public void openSelection (final String name, final Map<ItemStack, Runnable> actions, boolean randomOnForceClose) {
        forceClose();
        this.actions = actions;
        this.randomOnForceClose = randomOnForceClose;
        this.selection = generateInventory (name, actions);
        user.getPlayer().ifPresent(player -> player.openInventory(selection));
    }

    public void onInventoryClick (final InventoryClickEvent event) {
        if (this.selection != null) {
            if (this.selection.equals(event.getClickedInventory())) {
                event.setCancelled(true);
                ItemStack item = this.selection.getItem(event.getSlot());
                if (item != null) {
                    actions.getOrDefault(item, () -> {}).run();
                    clear();
                    user.getPlayer().ifPresent(Player::closeInventory);
                }
            }
        }
    }
}

