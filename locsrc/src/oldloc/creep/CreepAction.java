package oldloc.creep;

import org.bukkit.entity.LivingEntity;

import java.util.Optional;

public interface CreepAction {
    Creep getCreep ();
    ActionType getType ();
    Optional<LivingEntity> nextTarget ();
}
