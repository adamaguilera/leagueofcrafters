package som.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import som.spells.Spell;
import som.spells.helper.SpellConstant;
import som.spells.helper.SpellEffect;
import som.spells.SpellName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpellManager {
    @NotNull
    final Mage mage;
    @NotNull
    final List<Spell> spells;
    final List<SpellEffect> spellEffects;

    public SpellManager (@NotNull final Mage mage) {
        this.mage = mage;
        this.spells = getStartingSpells();
        this.spellEffects = new ArrayList<>();
    }

    public Optional<Spell> getSpell (final SpellName name) {
        return spells.stream().filter(spell -> spell.getSpellName().equals(name)).findFirst();
    }
    public List<Spell> getSpells () {
        return spells;
    }

    public List<Spell> getStartingSpells () {
        List<Spell> startingSpells = new ArrayList<>();
        SpellConstant.createSpell(this.mage, SpellName.EAT).ifPresent(startingSpells::add);
        SpellConstant.createSpell(this.mage, SpellName.BROKEN_COMPASS).ifPresent(startingSpells::add);
        SpellConstant.createSpell(this.mage, SpellName.CRATE).ifPresent(startingSpells::add);
        SpellConstant.createSpell(this.mage, SpellName.POWDER).ifPresent(startingSpells::add);
        SpellConstant.createSpell(this.mage, SpellName.SPELL_BOOK).ifPresent(startingSpells::add);
        SpellConstant.createSpell(this.mage, SpellName.BLINK).ifPresent(startingSpells::add);
        SpellConstant.createSpell(this.mage, SpellName.FLAME_WALKER).ifPresent(startingSpells::add);
        SpellConstant.createSpell(this.mage, SpellName.HARVESTER).ifPresent(startingSpells::add);
        return startingSpells;
    }
    public boolean hasSpell (final SpellName spellName) {
        return spells.stream().anyMatch(spell -> spell.getSpellName().equals(spellName));
    }

    public void validateEffects () {
        for (SpellEffect effect : spellEffects.stream().filter(SpellEffect::isOver).collect(Collectors.toList())) {
            spellEffects.remove(effect);
        }
    }
    public void addSpellEffect (final SpellEffect effect) {
        validateEffects();
        spellEffects.add(effect);
    }
    public boolean hasSpellEffect (final SpellName effectName) {
        validateEffects();
        return spellEffects.stream().anyMatch(effect -> effect.getName() == effectName);
    }
    public void addPotionEffect (final PotionEffect effect) {
        this.mage.getPlayer().ifPresent(player -> player.addPotionEffect(effect));
    }
}
