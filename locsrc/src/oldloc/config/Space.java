package oldloc.config;

import lombok.NonNull;
import org.bukkit.Location;

public class Space {
    @NonNull
    private Coordinate first;
    @NonNull
    private Coordinate second;

    // determines whether or not the coordinate is within the space
    public boolean inside (Location location) {
        // test x then tests y then tests z
        return (cmp (first.getX(), second.getX(), location.getBlockX())) &&
                (cmp (first.getY(), second.getY(), location.getBlockY())) &&
                (cmp (first.getZ(), second.getZ(), location.getBlockZ()));
    }

    private boolean cmp (int c1, int c2, int test) {
        int min = Math.min (c1, c2);
        int max = Math.max (c1, c2);

        if (test >= min) {
            if (test <= max) {
                return true;
            }
        }
        return false;
    }
}
