package oldloc.creep.creeps;

import oldloc.creep.Creep;
import oldloc.creep.CreepAction;
import oldloc.creep.actions.CreepAttack;
import oldloc.game.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;

import java.util.List;

@RequiredArgsConstructor
public class ZombieCreep extends Creep {
    @Getter
    final EntityType type = EntityType.ZOMBIE;
    final int aggroRadius = 10;
    @Getter
    final Team team;

    @Override
    protected List<CreepAction> getActions() {
        return List.of(new CreepAttack(this, this.aggroRadius));
    }
}
