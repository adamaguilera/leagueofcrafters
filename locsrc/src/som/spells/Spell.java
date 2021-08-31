package som.spells;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import som.chat.Log;
import som.player.Mage;
import som.spells.helper.Cooldown;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class Spell {
    /**
     * @return mage with this spell
     */
    public abstract Mage getMage ();
    /**
     * @return mana cost of this spell
     */
    public abstract int cost();
    /**
     * @return cooldown manager of this spell
     */
    public abstract Cooldown getCooldown ();
    /**
     * @return name of this spell
     */
    public abstract SpellName getSpellName ();
    /**
     * @return display material on spell scroll / spell book
     */
    public abstract Material getDisplayMaterial ();
    /**
     * @return lore for spell on spell scroll / spell book
     */
    public abstract List<String> getDisplayLore ();
    /**
     * @return items that can cast this spell
     */
    public abstract List<Material> getCastableItems ();


    public boolean attemptCast () {
        if (canUseAbility()) {
            putOnCooldown();
            consumeCost();
            cast();
            return true;
        }
        return false;
    }

    /**
     * Casts spell
     */
    public abstract void cast ();

    public String getDisplayName () { return toString(); }
    public List<Action> getCastableActions () { return List.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK); }
    public boolean cancelEvent () { return false; }
    public boolean leftHandCasts () { return true; }
    public boolean rightHandCasts () { return true; }
    public Optional<ItemStack> getCastableItem () { return Optional.empty(); }
    public UUID getPlayerID () {
        return getMage().getPlayerID();
    }
    public Optional<Player> getPlayer () {
        return getMage().getPlayer();
    }

    public boolean hasAbility () {
        return getMage().getSpellManager().hasSpell(getSpellName());
    }
    public boolean hasFood () {
        return getPlayer().isPresent() &&
                getPlayer().get().getFoodLevel() >= cost();
    }
    public boolean onCooldown () {
        return getCooldown().onCooldown();
    }
    public void consumeCost () { getMage().consumeMana(cost()); }

    public boolean canUseAbility () {
        return (hasAbility() && hasFood() && !onCooldown());
    }

    public boolean eventCastsAbility (final PlayerInteractEvent event) {
        return getCastableActions().contains(event.getAction()) &&
                ((leftHandCasts() && itemCastsAbility(event.getPlayer().getInventory().getItemInOffHand())) ||
                (rightHandCasts() && itemCastsAbility(event.getPlayer().getInventory().getItemInMainHand())));
    }
    public boolean itemCastsAbility (final ItemStack item) {
        return item != null && getCastableItems().contains(item.getType());
    }

    public void putOnCooldown () {
        getCooldown().putOnCooldown();
    }

    @Override
    public String toString () {
        StringBuilder name = new StringBuilder();
        for (String word : getSpellName().toString().split("_")) {
            if (name.length() > 0) {
                name.append(" ");
            }
            if (word.length() > 0) {
                name.append(word.substring(0, 1).toUpperCase(Locale.ROOT));
                name.append(word.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return name.toString();
    }

    public String getCdMessage () {
        return this + " is on cooldown for _ more seconds!";
    }

    public ItemStack getDisplay () {
        ItemStack display = new ItemStack(getDisplayMaterial(), 1);
        ItemMeta meta = display.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("" + ChatColor.GRAY + getDisplayName());
            meta.setLore(getDisplayLore());
            display.setItemMeta(meta);
        }
        return display;
    }

    public void givePlayerDisplay () {
        getPlayer().ifPresent(player -> {
            Map<Integer, ItemStack> remaining = player.getInventory().addItem(getDisplay());
            if (player.getLocation().getWorld() != null) {
                remaining.forEach((index, item) ->
                        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item));
            }
        });
    }

    public Location getLocationOffset (@NotNull final Location base,
                                       final int xOffset,
                                       final int yOffset,
                                       final int zOffset) {
        return base.clone().add(new Vector(xOffset, yOffset, zOffset));
    }

    public List<Location> getBlocksInRadius (@NotNull final Location base, final int radius) {
        List<Location> result = new ArrayList<>();
        for (int x = -1 * radius; x <= radius; x++) {
            for (int z = -1 * radius; z <= radius; z ++) {
                for (int y = -1 * radius; y <= radius; y ++) {
                    result.add(new Location (base.getWorld(), base.getX() + x, base.getY() + y, base.getZ() + z));
                }
            }
        }
        return result;
    }
}
