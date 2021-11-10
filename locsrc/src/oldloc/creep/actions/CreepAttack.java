package oldloc.creep.actions;

import oldloc.Main;
import oldloc.creep.ActionType;
import oldloc.creep.Creep;
import oldloc.creep.CreepAction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class CreepAttack implements CreepAction {
    @Getter
    final Creep creep;
    @Getter
    final ActionType type = ActionType.CREEP_ATTACK;
    final int aggroRadius;

    public Optional<LivingEntity> nextTarget () {
        return this.creep.getNearbyEnemies(this.aggroRadius)
                .stream()
                .filter(mob -> mob.isValid() && !(mob instanceof Player))
                .min((o1, o2) -> {
                    Location base = getCreep().getMob().map(Entity::getLocation)
                            .orElseGet(() -> new Location(Main.GET_WORLD(), 0, 0, 0));
                    return Double.compare(base.distanceSquared(o1.getLocation()),
                            base.distanceSquared(o2.getLocation()));
                });
    }
}
