package som.spells.mobility;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import som.chat.ItemText;
import som.player.Mage;
import som.spells.Spell;
import som.spells.SpellName;
import som.spells.helper.Cooldown;

import java.util.List;

@RequiredArgsConstructor
public class Blink extends Spell {
    @NotNull
    final Mage mage;
    final int cost = 5;
    final int cd = 20*1000;
    final SpellName name = SpellName.BLINK;
    final Material displayMaterial = Material.ENDER_PEARL;
    final List<Material> castableItems = List.of(Material.ENDER_PEARL);
    final List<String> displayLore = ItemText.FORMAT_LORE(List.of(
            "Instantly blinks up in direction",
            "you were looking. Doesn't go on",
            "cooldown if holding more than one",
            "enderpearl.",
            "CD: 20s, Mana: 5"));
    final int MAX_DISTANCE = 20;
    final int MAX_NONSOLID_BLOCKS = 4;
    Cooldown cooldown;


    /**
     * @return mage with this spell
     */
    @Override
    public Mage getMage() {
        return this.mage;
    }

    /**
     * @return mana cost of this spell
     */
    @Override
    public int cost() {
        return this.cost;
    }

    /**
     * @return cooldown manager of this spell
     */
    @Override
    public Cooldown getCooldown() {
        if (this.cooldown == null) {
            this.cooldown = Cooldown.builder()
                    .playerID(getPlayerID())
                    .cooldownMessage(getCdMessage())
                    .cooldownMillis(cd)
                    .build();
        }
        return this.cooldown;
    }

    /**
     * @return name of this spell
     */
    @Override
    public SpellName getSpellName() {
        return this.name;
    }

    /**
     * @return display material on spell scroll / spell book
     */
    @Override
    public Material getDisplayMaterial() {
        return this.displayMaterial;
    }

    /**
     * @return lore for spell on spell scroll / spell book
     */
    @Override
    public List<String> getDisplayLore() {
        return this.displayLore;
    }

    /**
     * @return items that can cast this spell
     */
    @Override
    public List<Material> getCastableItems() {
        return this.castableItems;
    }

    public boolean checkItem (final ItemStack item) {
        return item != null && item.getType().equals(getDisplayMaterial()) &&
                item.getAmount() > 1;
    }

    public boolean holdingExtraItem() {
        return getPlayer().map(player -> checkItem(player.getInventory().getItemInMainHand()) ||
                 checkItem(player.getInventory().getItemInOffHand())).orElse(false);
    }

    public void consumeExtraItem () {
        getPlayer().ifPresent(player -> {
            if (checkItem(player.getInventory().getItemInMainHand())) {
                ItemStack item = player.getInventory().getItemInMainHand();
                item.setAmount(item.getAmount() - 1);
            } else if (checkItem(player.getInventory().getItemInOffHand())) {
                ItemStack item = player.getInventory().getItemInMainHand();
                item.setAmount(item.getAmount() - 1);
            }
        });
    }


    @Override
    public boolean attemptCast () {
        if (canUseAbility()) {
            putOnCooldown();
            consumeCost();
            cast();
            return true;
        } else if (hasFood() && holdingExtraItem()) {
            putOnCooldown();
            consumeCost();
            consumeExtraItem();
            cast();
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelEvent() { return true; }

    /**
     * Casts spell
     */
    @Override
    public void cast() {
        getPlayer().ifPresent(player -> {
           Location current = player.getEyeLocation();
           Vector direction = player.getEyeLocation().getDirection().normalize();
           int nonSolidBlocks = 0;
           Location lastValidLocation = current;
           for (int block = 0; block < MAX_DISTANCE &&
                   nonSolidBlocks < MAX_NONSOLID_BLOCKS; block ++) {
               Location next = current.add(direction).clone();
               if (next.getBlock().isPassable()) {
                   lastValidLocation = next;
               } else {
                   nonSolidBlocks ++;
               }
           }
           player.teleport(lastValidLocation, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
        });
    }
}
