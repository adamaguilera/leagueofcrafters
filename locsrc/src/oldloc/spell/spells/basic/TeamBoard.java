package oldloc.spell.spells.basic;

import oldloc.Main;
import oldloc.player.User;
import oldloc.spell.Spell;
import oldloc.spell.SpellName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;

import java.util.List;

@RequiredArgsConstructor
public class TeamBoard extends Spell {
    @Getter
    final SpellName name = SpellName.TEAM_BOARD;
    @Getter
    final User user;
    @Getter
    final int cd = 1000;
    @Getter
    final int cost = 0;
    @Getter
    final Material type = Material.PAPER;
    @Getter
    final List<String> lore = List.of(
            "Use this to see which players",
            "are on which team!",
            "CD: 1s | Energy: 0f");
    @Override
    public List<Action> getActions() { return List.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK, Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK); }

    @Override
    public void cast() {
        Main.GET_GAME().getTeamManager().getTeams().forEach(team -> {
            if (user.getTeam().equals(team)) {
                message("Your team, " + team + ", has the following players:");
            } else {
                message("Team, " + team + ", has the following players:");
            }
            team.getUsers().forEach(user -> user.getPlayer().ifPresent(player ->
                            message(player.getDisplayName())));
            message("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        });
    }
}
