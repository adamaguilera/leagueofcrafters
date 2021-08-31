package som.spells.utility;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import som.chat.ItemText;
import som.player.Mage;
import som.spells.Spell;
import som.spells.SpellName;
import som.spells.basic.data.RarityData;
import som.spells.helper.Cooldown;

import java.util.List;
import java.util.Random;
import java.util.TreeMap;

@RequiredArgsConstructor
public class Harvester extends Spell {
    @NotNull
    final Mage mage;
    final int cost = 10;
    final int cd = 50*1000;
    final SpellName name = SpellName.HARVESTER;
    final Material displayMaterial = Material.WOODEN_HOE;
    final List<Material> castableItems = List.of(Material.WOODEN_HOE);
    final List<String> displayLore = ItemText.FORMAT_LORE(List.of(
            "Rakes all grass around you",
            "and randomly drops food",
            "CD: 50s, Mana: 10"));
    final int RADIUS = 3;
    final Material HARVEST_FROM = Material.GRASS_BLOCK;
    final Material HARVEST_INTO = Material.FARMLAND;
    List<RarityData<Material>> foods = List.of(
            new RarityData<>(Material.AIR, 1.0),
            new RarityData<>(Material.SWEET_BERRIES, 0.35),
            new RarityData<>(Material.CARROT, 0.35),
            new RarityData<>(Material.APPLE, 0.22),
            new RarityData<>(Material.GLOW_BERRIES, 0.15),
            new RarityData<>(Material.GOLDEN_CARROT, 0.15),
            new RarityData<>(Material.GOLDEN_APPLE, 0.10),
            new RarityData<>(Material.ENCHANTED_GOLDEN_APPLE, 0.04));
    final Random random = new Random();
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


    public ItemStack getRandomFood () {
        TreeMap<Double, ItemStack> map = new TreeMap<>();
        double next = 0.0;
        for (RarityData<Material> food : foods) {
            map.put(next, new ItemStack(food.data(), 1));
            next += food.rarity();
        }
        return map.floorEntry(random.nextDouble() * next).getValue();
    }

    /**
     * Casts spell
     */
    @Override
    public void cast() {
        getPlayer().ifPresent(player -> {
            getBlocksInRadius(player.getLocation(), RADIUS).forEach(location -> {
                if (location != null && location.getWorld() != null &&
                        location.getBlock().getType() == HARVEST_FROM) {
                    location.getBlock().setType(HARVEST_INTO);
                    ItemStack drop = getRandomFood();
                    if (drop.getType() != Material.AIR) {
                        location.getWorld().dropItemNaturally(
                                getLocationOffset(location, 0, 1, 0),
                                drop);
                    }
                }
            });
        });
    }
}
