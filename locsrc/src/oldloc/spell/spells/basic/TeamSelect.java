package oldloc.spell.spells.basic;

import oldloc.Main;
import oldloc.player.User;
import oldloc.spell.Spell;
import oldloc.spell.SpellName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TeamSelect extends Spell {
    final String ALREADY_ON_TEAM = "You are already on this team!";

    @Getter
    final SpellName name = SpellName.TEAM_SELECT;
    @Getter
    final User user;
    @Getter
    final int cd = 1000;
    @Getter
    final int cost = 0;
    @Getter
    final Material type = Material.WHITE_BANNER;
    @Getter
    final List<String> lore = List.of(
            "Use this choose a specific",
            "team to join!",
            "CD: 1s | Energy: 0f");
    @Override
    public List<Action> getActions() { return List.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK, Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK); }

    @Override
    public void cast() {
        Map<ItemStack, Runnable> options = new HashMap<>();
        Main.GET_GAME().getTeamManager().getTeams().forEach(team -> {
            options.put(team.getDisplay(), () -> {
                if (user.getTeam().equals(team)) {
                    message(ALREADY_ON_TEAM);
                } else {
                    Main.GET_GAME().getTeamManager().switchTeam(team, user);
                }
            });
        });
        user.getHero().getSelectionManager().openSelection(toString(), options, false);
    }
}
