package oldloc.creep;

import oldloc.Main;
import oldloc.game.Team;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class Creep {
    protected abstract EntityType getType();
    protected abstract List<CreepAction> getActions();
    protected abstract Team getTeam();
    public UUID id;
    public LivingEntity target;

    public void spawn (@NotNull final Location spawn) {
        getMob().ifPresent(Entity::remove);
        Entity entity = spawn.getWorld().spawnEntity(spawn, getType());
        if (entity instanceof Mob) {
            this.id = entity.getUniqueId();
            getMob().ifPresent(mob -> mob.setRemoveWhenFarAway(false));
            update();
        }
    }

    public void update () {
        for (CreepAction creepAction : getActions()) {
            Optional<LivingEntity> nextTarget = creepAction.nextTarget();
            if (nextTarget.isPresent()) {
                this.target = nextTarget.get();
                return;
            }
        }
    }

    public boolean matches (final UUID id) {
        return this.id.equals(id);
    }

    public boolean sameTeam (UUID id) {
        Optional<Creep> target = Main.GET_GAME().getCreepManager().getCreep(id);
        return target.isPresent() && getTeam().equals(target.get().getTeam());
    }

    public List<LivingEntity> getNearbyEnemies (final int aggroRadius) {
        return getMob().isPresent() ?
                getMob().get().getNearbyEntities(aggroRadius, aggroRadius, aggroRadius)
                        .stream()
                        .filter(entity -> entity instanceof LivingEntity &&
                                !sameTeam(entity.getUniqueId()))
                        .map(entity -> (LivingEntity) entity)
                        .collect(Collectors.toList()) :
                new ArrayList<>();
    }

    public Optional<Mob> getMob () {
        Entity entity = (Bukkit.getEntity(this.id));
        return Optional.ofNullable(entity instanceof Mob ? (Mob) entity : null);
    }

    public void onEntityTargetEvent (final EntityTargetEvent event) { event.setTarget(this.target); }
}
