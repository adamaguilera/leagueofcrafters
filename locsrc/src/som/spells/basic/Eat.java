package som.spells.basic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import som.chat.ItemText;
import som.player.Mage;
import som.spells.Spell;
import som.spells.SpellName;
import som.spells.basic.data.FoodData;
import som.spells.helper.Cooldown;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class Eat extends Spell {
    @Getter
    final static int STARTING_AMOUNT = 12;
    @NotNull
    final Mage mage;
    final int cost = 0;
    final int cd = 800;
    final SpellName name = SpellName.EAT;
    final Material displayMaterial = Material.APPLE;

    final List<String> displayLore = ItemText.FORMAT_LORE(List.of(
            "Instantly consumes food to restore",
            "health and mana",
            "CD: 0.8s"));
    final Map<Material, FoodData> validFood = Map.of(
            Material.SWEET_BERRIES, new FoodData(Material.SWEET_BERRIES, 4, 6, 1),
            Material.CARROT, new FoodData(Material.CARROT, 5, 4, 2),
            Material.APPLE, new FoodData(Material.APPLE, 6, 6, 2),
            Material.GLOW_BERRIES, new FoodData(Material.GLOW_BERRIES, 6, 8, 2),
            Material.GOLDEN_CARROT, new FoodData(Material.GOLDEN_CARROT, 7, 4, 3),
            Material.GOLDEN_APPLE, new FoodData(Material.GOLDEN_APPLE, 8, 8, 4),
            Material.ENCHANTED_GOLDEN_APPLE, new FoodData(Material.ENCHANTED_GOLDEN_APPLE, 20, 20, 10)
    );
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
        return this.validFood.keySet().stream().toList();
    }

    /**
     * Overrides what item casts this ability
     * @param item item that was cast
     * @return whether that material is edible
     */
    @Override
    public boolean itemCastsAbility (final ItemStack item) {
        return item != null && item.getType().isEdible();
    }

    @Override
    public boolean canUseAbility () {
        return (hasAbility() && hasFood() && !onCooldown() &&
                (getMage().isDamaged() || getMage().isHungry()));
    }

    @Override
    public boolean cancelEvent () { return true; }

    /**
     * Casts spell
     */
    @Override
    public void cast() {
        getPlayer().ifPresent(player -> {
            if (!applyFood(player.getInventory().getItemInMainHand())) {
                applyFood(player.getInventory().getItemInOffHand());
            }
        });
    }

    public boolean applyFood (final ItemStack item) {
        getFoodData(item.getType()).ifPresent(food -> {
            item.setAmount(item.getAmount() - 1);
                food.applyTo(getMage());
        });
        return getFoodData(item.getType()).isPresent();
    }

    public Optional<FoodData> getFoodData (final Material material) {
        return Optional.ofNullable(validFood.getOrDefault(material, null));
    }
}
