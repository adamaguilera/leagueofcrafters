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
public class ReadyDye extends Spell {
    @Getter
    final SpellName name = SpellName.READY_DYE;
    @Getter
    final User user;
    @Getter
    final int cd = 1000;
    @Getter
    final int cost = 0;
    final String READY_NAME = "Ready Dye";
    final Material READY_TYPE = Material.LIME_DYE;
    final String UNREADY_NAME = "Unready Dye";
    final Material UNREADY_TYPE = Material.RED_DYE;

    public boolean isReady () {
        return user.getHero().getLobbyManager().isReady();
    }

    @Override
    public Material getType () {
        return isReady() ? UNREADY_TYPE : READY_TYPE;
    }
    @Override
    public String toString () {
        return isReady() ? UNREADY_NAME : READY_NAME;
    }
    @Override
    public List<String> getLore () {
        return List.of("Use this dye to ready or unready",
                        "current status: " + isReady(),
                        "CD: 1s | Energy: 0f");
    }

    @Override
    public List<Action> getActions() { return List.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK, Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK); }

    @Override
    public void cast() {
        // remove outdated dye color
        getPlayer().ifPresent(player -> {
            if (typeMatches(player.getInventory().getItemInMainHand().getType())) {
                player.getInventory().getItemInMainHand().setAmount(0);
            } else {
                player.getInventory().getItemInOffHand().setAmount(0);
            }
        });
        // change ready status of player
        this.user.getHero().getLobbyManager().ready();
        // give new ready item
        giveDisplay();
    }
}
