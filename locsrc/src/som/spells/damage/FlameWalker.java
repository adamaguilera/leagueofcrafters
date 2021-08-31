package som.spells.damage;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import som.chat.ItemText;
import som.player.Mage;
import som.spells.Spell;
import som.spells.SpellName;
import som.spells.helper.Cooldown;
import som.spells.helper.RunnableTask;

import java.util.List;

@RequiredArgsConstructor
public class FlameWalker extends Spell {
    @NotNull
    final Mage mage;
    final int cost = 6;
    final int cd = 25*1000;
    final SpellName name = SpellName.FLAME_WALKER;
    final Material displayMaterial = Material.FLINT;
    final List<Material> castableItems = List.of(Material.FLINT);
    final List<String> displayLore = ItemText.FORMAT_LORE(List.of(
            "Causes you to leave a trail",
            "of fire while activated",
            "CD: 25s, Mana: 6"));
    final int FIRE_RESISTANCE_DURATION = 30 * 20;
    final int FLAME_DURATION = 10;
    final int RADIUS = 2;
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


    private PotionEffect getFireResistance () {
        return new PotionEffect(PotionEffectType.FIRE_RESISTANCE, FIRE_RESISTANCE_DURATION, 0, false, true);
    }

    private RunnableTask flameWalkerTask () {
        return RunnableTask.builder()
                .onTaskTick(flameWalkerOnSecond())
                .onTaskEnd(RunnableTask.emptyRunnable())
                .build();
    }

    private Runnable flameWalkerOnSecond () {
        return () -> getPlayer().ifPresent(player -> {
            getBlocksInRadius(player.getLocation(), RADIUS).forEach(location -> {
                if (location.getBlock().isPassable()) {
                    location.getBlock().setType(Material.FIRE);
                }
            });
        });
    }


    /**
     * Casts spell
     */
    @Override
    public void cast() {
        getMage().getSpellManager().addPotionEffect(getFireResistance());
        flameWalkerTask().createTask(FLAME_DURATION);
    }
}
