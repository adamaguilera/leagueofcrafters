package som.spells.basic;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import som.chat.ItemText;
import som.player.Mage;
import som.spells.Spell;
import som.spells.SpellName;
import som.spells.helper.Cooldown;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SpellBook extends Spell {
    @NotNull
    final Mage mage;
    final int cost = 0;
    final int cd = 5000;
    final SpellName name = SpellName.SPELL_BOOK;
    final Material displayMaterial = Material.ENCHANTED_BOOK;
    final List<Material> castableItems = List.of(Material.ENCHANTED_BOOK);
    final List<String> displayLore = ItemText.FORMAT_LORE(List.of(
            "Displays all spells you have!",
            "CD: 5s"));
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

    public Comparator<Spell> getSpellNameSort () {
        return Comparator.comparing(Spell::getSpellName);
    }
    /**
     * Casts spell
     */
    @Override
    public void cast() {
        Map<ItemStack, Runnable> actions = new HashMap<>();
        mage.getSpellManager().getSpells().stream().sorted(getSpellNameSort())
                .map(Spell::getDisplay)
                .forEach(item -> actions.put(item, () -> {}));
        mage.getSelectionManager().openSelection("Spell Book", actions);
    }
}
