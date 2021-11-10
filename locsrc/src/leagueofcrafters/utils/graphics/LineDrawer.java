package leagueofcrafters.utils.graphics;

import leagueofcrafters.utils.config.RGB;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;


public record LineDrawer (@NotNull Location start, @NotNull Location end, @NotNull RGB dust, double gap) {
    public void draw () {

        double distance = start.distance(end);
        Vector current = start.toVector().clone();
        Vector increment = end.toVector().clone().subtract(current).normalize().multiply(gap);
        for (double len = 0; len < distance; current.add(increment)) {
            start.getWorld().spawnParticle(Particle.REDSTONE, current.getX(), current.getY(), current.getZ(), 1, dust.toDust());
            len += gap;
        }
    }
}
