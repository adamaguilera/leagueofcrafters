package som.spells.mobility;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import som.chat.ItemText;
import som.player.Mage;
import som.spells.Spell;
import som.spells.SpellName;
import som.spells.helper.Cooldown;
import som.spells.helper.DamageType;
import som.spells.helper.RunnableTask;

import java.util.List;

@RequiredArgsConstructor
public class Powder extends Spell {
    @NotNull
    final Mage mage;
    final int cost = 4;
    final int cd = 30*1000;
    final SpellName name = SpellName.POWDER;
    final Material displayMaterial = Material.SUGAR;
    final List<Material> castableItems = List.of(Material.SUGAR);
    final List<String> displayLore = ItemText.FORMAT_LORE(List.of(
            "A special powder that improves",
            "your move speed and attack speed but",
            "gives opposite effect afterwards",
            "CD: 30s | Mana 4"));
    final int SPEED_AMP = 1;
    final int HASTE_AMP = 1;
    final int CONFUSION_AMP = 0;
    final int FATIGUE_AMP = 1;
    // ticks
    final int BUFF_DURATION = 8*20;
    //ticks
    final int DEBUFF_DURATION = 10*20;
    // seconds
    final int DEBUFF_DELAY = 8;
    final DamageType DAMAGE_TYPE = DamageType.MAGICAL;
    final double SELF_DAMAGE = 3.0;
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

    public PotionEffect getSpeed () {
        return new PotionEffect(PotionEffectType.SPEED, BUFF_DURATION, SPEED_AMP, false, true);
    }
    public PotionEffect getHaste () {
        return new PotionEffect(PotionEffectType.FAST_DIGGING, BUFF_DURATION, HASTE_AMP, false, true);
    }
    public PotionEffect getConfusion () {
        return new PotionEffect(PotionEffectType.CONFUSION, DEBUFF_DURATION, CONFUSION_AMP, false, true);
    }
    public PotionEffect getFatigue () {
        return new PotionEffect(PotionEffectType.SLOW_DIGGING, DEBUFF_DURATION, FATIGUE_AMP, false, true);
    }
    public Runnable getDebuff () {
        return () -> {
            getMage().getSpellManager().addPotionEffect(getConfusion());
            getMage().getSpellManager().addPotionEffect(getFatigue());
            getMage().getDamageManager().damage(SELF_DAMAGE, DAMAGE_TYPE, getPlayer().orElse(null));
        };
    }

    public RunnableTask getPowderTask () {
        return RunnableTask.builder()
                .onTaskTick(RunnableTask.emptyRunnable())
                .onTaskEnd(getDebuff())
                .build();
    }
    /**
     * Casts spell
     */
    @Override
    public void cast() {
        getMage().getSpellManager().addPotionEffect(getSpeed());
        getMage().getSpellManager().addPotionEffect(getHaste());
        getPowderTask().createTask(DEBUFF_DELAY);
    }
}
