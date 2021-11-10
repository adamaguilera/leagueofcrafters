package oldloc.spell.spells.basic;

import oldloc.player.User;
import oldloc.spell.Spell;
import oldloc.spell.SpellName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;

import java.util.List;

@RequiredArgsConstructor
public class HeroSelect extends Spell {
    final String ALREADY_HERO = "You are already this hero!";

    @Getter
    final SpellName name = SpellName.HERO_SELECT;
    @Getter
    final User user;
    @Getter
    final int cd = 1000;
    @Getter
    final int cost = 0;
    @Getter
    final Material type = Material.BOOK;
    @Getter
    final List<String> lore = List.of(
            "Use this choose a specific",
            "hero to play as!",
            "CD: 1s | Energy: 0f");
    @Override
    public List<Action> getActions() { return List.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK, Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK); }

    @Override
    public void cast() {
    }
}
