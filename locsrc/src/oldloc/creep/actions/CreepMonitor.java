package oldloc.creep.actions;

import oldloc.creep.ActionType;
import oldloc.creep.Creep;
import oldloc.creep.CreepAction;
import oldloc.creep.Target;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
public class CreepMonitor implements CreepAction {
    @Getter
    final Creep creep;
    @Getter
    final ActionType type = ActionType.MONITOR;
    @Getter
    final boolean isValid = true;
    final Random random = new Random();
    final double REACHED_DISTANCE = 0.75;
    @NotNull
    final List<Location> destinations;
    Target target;

    private boolean targetReached (@NotNull final Mob mob) {
        return this.target != null &&
                mob.getLocation().distanceSquared(this.target.getLocationOrSpawn()) <= REACHED_DISTANCE;
    }

    public Optional<LivingEntity> nextTarget () {
        return getCreep().getMob().map(mob -> {
            if (this.target == null || targetReached(mob)) {
                this.target = new Target(nextDestination());
            }
            return this.target.get();
        }).orElse(Optional.empty());
    }

    public Location nextDestination () {
        return destinations.get(random.nextInt(destinations.size()));
    }
}
