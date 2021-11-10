package oldloc.player.managers;

import oldloc.player.User;
import oldloc.spell.Spell;
import oldloc.spell.SpellName;
import oldloc.spell.spells.basic.ReadyDye;
import oldloc.spell.spells.basic.TeamBoard;
import oldloc.spell.spells.basic.TeamSelect;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpellManager {
    final User user;
    @Getter
    List<Spell> spells;
    // List<Passive> passives;

    public SpellManager (@NotNull final User user) {
        this.user = user;
        this.spells = new ArrayList<>();
        this.spells.addAll(getBasicSpells());
    }

    public List<Spell> getBasicSpells () {
        return List.of(
                new TeamSelect(this.user),
                new TeamBoard(this.user),
                new ReadyDye(this.user)
        );
    }
    public void giveAllSpellDisplays() {
        this.spells.forEach(Spell::giveDisplay);
    }
    public boolean hasSpell (final SpellName name) {
        return spells.stream().map(Spell::getName).anyMatch(spellName -> spellName.equals(name));
    }

    public boolean addSpell (@NotNull final Spell spell) {
        return spells.add(spell);
    }
}
