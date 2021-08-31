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
import som.spells.basic.data.CrateData;
import som.spells.helper.Cooldown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Crate extends Spell {
    @Getter
    final static int STARTING_AMOUNT = 6;
    @NotNull
    final Mage mage;
    final int cost = 0;
    final int cd = 1000;
    final SpellName name = SpellName.CRATE;
    final Material displayMaterial = Material.BARREL;
    final List<Material> castableItems = List.of(Material.BARREL);
    final List<String> displayLore = ItemText.FORMAT_LORE(List.of(
            "A lootbox that discovers a reward",
            "Based on your team's tier!",
            "CD: 1s"));
    final Random random = new Random();
    final int CRATE_SIZE = 9;
    Cooldown cooldown;


    /**
     * @return mage with this spell
     */
    @Override
    public @NotNull Mage getMage() {
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


    @Override
    public boolean itemCastsAbility (final ItemStack item) {
        if (item != null && getCastableItems().contains(item.getType()) &&
                item.getItemMeta() != null &&
                item.getItemMeta().getLore() != null) {
            return ChatColor.stripColor(String.valueOf(item.getItemMeta().getLore())).equalsIgnoreCase(ChatColor.stripColor(String.valueOf(getDisplayLore()))) &&
                    getCastableItems().contains(item.getType());
        }
        return false;
    }

    @Override
    public boolean cancelEvent () { return true; }

    /**
     * Casts spell
     */
    @Override
    public void cast() {
        HashMap<ItemStack, Runnable> crate = new HashMap<>();
        int tier = getMage().getTier();
        List<CrateData> crateData = getCrate(tier, CRATE_SIZE);
        crateData.forEach(data -> crate.put(data.toItemStack(tier), createRunnable(data.toItemStack(tier))));
        getMage().getSelectionManager().openSelection("Crate Tier " + tier, crate);
        getMage().getPlayer().ifPresent(player -> {
            if (!removeCrate(player.getInventory().getItemInMainHand())){
                removeCrate(player.getInventory().getItemInOffHand());
            }
        });
    }

    private boolean removeCrate (final ItemStack item) {
        if (itemCastsAbility(item)) {
            item.setAmount(item.getAmount() - 1);
            return true;
        }
        return false;
    }
    public Runnable createRunnable (final ItemStack reward) {
        return () -> {
            getPlayer().ifPresent(player -> {
                Map<Integer, ItemStack> remaining = player.getInventory().addItem(reward);
                if (player.getLocation().getWorld() != null) {
                    remaining.forEach((index, item) ->
                            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item));
                }
            });
        };
    }

    public List<CrateData> getCrate (final int tier, final int amount) {
        List<CrateData> crate = new ArrayList<>();
        for (int index = 0; index < amount ; index ++) {
            CrateData nextCrate = getRandomCrateData(tier, crate);
            crate.add(nextCrate);
        }
        return crate;
    }
    public CrateData getRandomCrateData (final int tier, final List<CrateData> exclude) {
        List<CrateData> allCrateData = getAllCrateData().stream().filter(crateData -> crateData.hasTier(tier)).collect(Collectors.toList());
        exclude.forEach(allCrateData::remove);
        return allCrateData.get(random.nextInt(allCrateData.size()));
    }

    public List<CrateData> getAllCrateData () {
        List<CrateData> crates = new ArrayList<>();
        // TOOLS
        crates.add(new CrateData(Material.BOW, List.of(1, 1, 1)));
        crates.add(new CrateData(Material.CROSSBOW, List.of(1, 1, 1)));
        crates.add(new CrateData(Material.ARROW, List.of(20, 48, 64)));
        crates.add(new CrateData(Material.STONE_PICKAXE, List.of(1, 0, 0)));
        crates.add(new CrateData(Material.STONE_AXE, List.of(1, 0, 0)));
        crates.add(new CrateData(Material.STONE_SWORD, List.of(1, 0, 0)));
        crates.add(new CrateData(Material.STONE_SHOVEL, List.of(1, 0, 0)));
        crates.add(new CrateData(Material.IRON_PICKAXE, List.of(1, 1, 0)));
        crates.add(new CrateData(Material.IRON_AXE, List.of(1, 1, 0)));
        crates.add(new CrateData(Material.IRON_SWORD, List.of(1, 1, 0)));
        crates.add(new CrateData(Material.IRON_SHOVEL, List.of(1, 1, 0)));
        crates.add(new CrateData(Material.DIAMOND_PICKAXE, List.of(0, 1, 1)));
        crates.add(new CrateData(Material.DIAMOND_AXE, List.of(0, 1, 1)));
        crates.add(new CrateData(Material.DIAMOND_SHOVEL, List.of(0, 1, 1)));
        crates.add(new CrateData(Material.NETHERITE_PICKAXE, List.of(0, 0, 1)));
        crates.add(new CrateData(Material.NETHERITE_AXE, List.of(0, 0, 1)));
        crates.add(new CrateData(Material.NETHERITE_SHOVEL, List.of(0, 0, 1)));
        // ARMOR
        crates.add(new CrateData(Material.CHAINMAIL_HELMET, List.of(1, 0, 0)));
        crates.add(new CrateData(Material.CHAINMAIL_CHESTPLATE, List.of(1, 0, 0)));
        crates.add(new CrateData(Material.CHAINMAIL_LEGGINGS, List.of(1, 0, 0)));
        crates.add(new CrateData(Material.CHAINMAIL_BOOTS, List.of(1, 0, 0)));

        crates.add(new CrateData(Material.IRON_HELMET, List.of(1, 1, 0)));
        crates.add(new CrateData(Material.IRON_CHESTPLATE, List.of(1, 1, 0)));
        crates.add(new CrateData(Material.IRON_LEGGINGS, List.of(1, 1, 0)));
        crates.add(new CrateData(Material.IRON_BOOTS, List.of(1, 1, 0)));

        crates.add(new CrateData(Material.DIAMOND_HELMET, List.of(0, 1, 1)));
        crates.add(new CrateData(Material.DIAMOND_CHESTPLATE, List.of(0, 1, 1)));
        crates.add(new CrateData(Material.DIAMOND_LEGGINGS, List.of(0, 1, 1)));
        crates.add(new CrateData(Material.DIAMOND_BOOTS, List.of(0, 1, 1)));

        crates.add(new CrateData(Material.NETHERITE_HELMET, List.of(0, 0, 1)));
        crates.add(new CrateData(Material.NETHERITE_CHESTPLATE, List.of(0, 0, 1)));
        crates.add(new CrateData(Material.NETHERITE_LEGGINGS, List.of(0, 0, 1)));
        crates.add(new CrateData(Material.NETHERITE_BOOTS, List.of(0, 0, 1)));
        // ITEMS
        crates.add(new CrateData(Material.COAL, List.of(16, 32, 64)));
        crates.add(new CrateData(Material.IRON_INGOT, List.of(6, 24, 64)));
        crates.add(new CrateData(Material.DIAMOND, List.of(1, 1, 2)));
        crates.add(new CrateData(Material.EMERALD, List.of(1, 1, 1)));
        crates.add(new CrateData(Material.NETHERITE_INGOT, List.of(0, 0, 1)));
        crates.add(new CrateData(Material.SADDLE, List.of(1, 1, 1)));
        crates.add(new CrateData(Material.MAP, List.of(1, 1, 1)));
        crates.add(new CrateData(Material.FLINT_AND_STEEL, List.of(1, 1, 1)));
        crates.add(new CrateData(Material.WATER_BUCKET, List.of(1, 1, 1)));
        crates.add(new CrateData(Material.LAVA_BUCKET, List.of(1, 1, 1)));
        crates.add(new CrateData(Material.ENDER_PEARL, List.of(2, 4, 8)));
        crates.add(new CrateData(Material.CREEPER_SPAWN_EGG, List.of(2, 4, 8)));
        crates.add(new CrateData(Material.GHAST_SPAWN_EGG, List.of(2, 3, 4)));
        crates.add(new CrateData(Material.HORSE_SPAWN_EGG, List.of(1, 1, 1)));

        // BLOCKS
        crates.add(new CrateData(Material.DIRT, List.of(64, 64, 64)));
        crates.add(new CrateData(Material.STONE, List.of(32, 64, 64)));
        crates.add(new CrateData(Material.OAK_LOG, List.of(16, 32, 64)));
        crates.add(new CrateData(Material.MAGMA_BLOCK, List.of(8, 16, 32)));
        crates.add(new CrateData(Material.OBSIDIAN, List.of(8, 16, 32)));
        crates.add(new CrateData(Material.TNT, List.of(4, 8, 16)));
        crates.add(new CrateData(Material.COBWEB, List.of(4, 8, 16)));
        crates.add(new CrateData(Material.BEDROCK, List.of(1, 2, 3)));
        // FOOD
        crates.add(new CrateData(Material.SWEET_BERRIES, List.of(8, 14, 22)));
        crates.add(new CrateData(Material.CARROT, List.of(8, 14, 22)));
        crates.add(new CrateData(Material.APPLE, List.of(6, 10, 16)));
        crates.add(new CrateData(Material.GLOW_BERRIES, List.of(4, 6, 12)));
        crates.add(new CrateData(Material.GOLDEN_CARROT, List.of(4, 6, 12)));
        crates.add(new CrateData(Material.GOLDEN_APPLE, List.of(2, 4, 8)));
        crates.add(new CrateData(Material.ENCHANTED_GOLDEN_APPLE, List.of(0, 1, 2)));
        return crates;
    }

}
