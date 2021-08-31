package som.spells.helper;

import org.bukkit.Material;
import som.player.Mage;
import som.spells.Spell;
import som.spells.SpellName;
import som.spells.basic.BrokenCompass;
import som.spells.basic.Crate;
import som.spells.basic.Eat;
import som.spells.basic.SpellBook;
import som.spells.damage.FlameWalker;
import som.spells.mobility.Blink;
import som.spells.mobility.Powder;
import som.spells.utility.Harvester;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpellConstant {
    public static Optional<Spell> createSpell (final Mage mage,
                                               final SpellName spellName) {
        switch (spellName) {
            // basic
            case EAT:
                return Optional.of(new Eat(mage));
            case BROKEN_COMPASS:
                return Optional.of(new BrokenCompass(mage));
            case CRATE:
                return Optional.of(new Crate(mage));
            case SPELL_BOOK:
                return Optional.of(new SpellBook(mage));
            // damage
            case FLAME_WALKER:
                return Optional.of(new FlameWalker(mage));
            // mobility
            case POWDER:
                return Optional.of(new Powder(mage));
            case BLINK:
                return Optional.of(new Blink(mage));
            // utility
            case HARVESTER:
                return Optional.of(new Harvester(mage));
            default:
                return Optional.empty();
        }
    }

    public static List<SpellName> getRandomSpells(final int amount,
                                                  final List<SpellName> exclude) {
        return Stream.of(SpellName.values())
            .filter(spell -> !exclude.contains(spell))
            .limit(amount)
            .collect(Collectors.toList());
    }

    public static List<Material> getPickaxes () {
        return List.of(Material.WOODEN_PICKAXE,
                Material.STONE_PICKAXE,
                Material.IRON_PICKAXE,
                Material.GOLDEN_PICKAXE,
                Material.DIAMOND_PICKAXE,
                Material.NETHERITE_PICKAXE
                );
    }
}
