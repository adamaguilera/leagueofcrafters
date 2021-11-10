package leagueofcrafters.utils.config;

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
    public double maxDiameter () {
        return Math.max(Math.abs(first.getX() - second.getX()),
                Math.max(Math.abs(first.getY() - second.getY()),
                        Math.abs(first.getZ() - second.getZ())));

    }

    private boolean cmp (double c1, double c2, int test) {
        double min = Math.min (c1, c2);
        double max = Math.max (c1, c2);

        if (test >= min) {
            if (test <= max) {
                return true;
            }
        }
        return false;
    }
}
