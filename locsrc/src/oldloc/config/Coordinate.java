package oldloc.config;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

@Data
@Builder
public class Coordinate {
    @NonNull
    private String world;
    @NonNull
    private int x;
    @NonNull
    private int y;
    @NonNull
    private int z;
    private float yaw;
    private float pitch;

    public static Coordinate fromLocation(Location location) {
        return Coordinate.builder()
                .world(location.getWorld().getName())
                .x(location.getBlockX())
                .y(location.getBlockY())
                .z(location.getBlockZ())
                .yaw(location.getYaw())
                .pitch(location.getPitch())
                .build();
    }

    public boolean sameVector (Vector vector) {
        Vector compare = toVector();
        return compare.getBlockX() == vector.getBlockX() &&
                compare.getBlockY() == vector.getBlockY() &&
                compare.getBlockZ() == vector.getBlockZ();
    }
    public boolean sameLocation (Location location) {
        return location.getWorld().getName().equals(world) &&
                sameVector(location.toVector());
    }

    public Vector toVector () {
        return new Vector (x, y, z);
    }

    public Location toLocation() {
        final World world = Bukkit.getWorld(this.world);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public void setVector (Vector vector) {
        x = vector.getBlockX();
        y = vector.getBlockY();
        z = vector.getBlockZ();
    }

    public void setLocation(Location location) {
        x = location.getBlockX();
        y = location.getBlockY();
        z = location.getBlockZ();
        yaw = location.getYaw();
        pitch = location.getPitch();

    }
}

