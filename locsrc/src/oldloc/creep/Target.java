package oldloc.creep;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Target {
    @NotNull
    final Location spawn;
    LivingEntity marker;

    public Target (@NotNull final Location spawn) {
        this.spawn = spawn;
        this.marker = (LivingEntity) this.spawn.getWorld().spawnEntity(this.spawn, EntityType.ARMOR_STAND);
        ArmorStand stand = (ArmorStand) this.marker;
        stand.setSmall(true);
        stand.setVisible(false);
        stand.setInvulnerable(true);
        stand.setInvisible(true);
        stand.setRemoveWhenFarAway(false);
    }

    public Optional<LivingEntity> get () {
        return Optional.ofNullable(marker);
    }

    public Location getLocationOrSpawn () {
        return marker != null && marker.isValid() ? marker.getLocation() : this.spawn;
    }
}
