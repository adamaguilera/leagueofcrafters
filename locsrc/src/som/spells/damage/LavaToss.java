package som.spells.damage;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
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
public class LavaToss extends Spell {
    @NotNull
    final Mage mage;
    final int cost = 4;
    final int cd = 30*1000;
    final SpellName name = SpellName.LAVA_TOSS;
    final Material displayMaterial = Material.LAVA_BUCKET;
    final List<Material> castableItems = List.of(Material.LAVA_BUCKET);
    final List<String> displayLore = ItemText.FORMAT_LORE(List.of(
            "Throws a bunch of lava",
            "in front of you fire",
            "CD: 30s, Mana: 6"));
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

    /**
     * Casts spell
     */
    @Override
    public void cast() {
        /*
        getPlayer().ifPresent(player -> {
           Location base = player.getEyeLocation();
           Vector direction = player.getEyeLocation().getDirection().normalize();
           base.add(direction).add(direction);
           Location lower = getLocationOffset(base, 0, -1, 0
        });
         */
    }
}
