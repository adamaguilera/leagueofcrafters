package leagueofcrafters.utils.config;

import lombok.Data;
import org.bukkit.Color;
import org.bukkit.Particle;

@Data
public class RGB {
    final int red;
    final int green;
    final int blue;

    public Particle.DustOptions toDust () {
        return new Particle.DustOptions(Color.fromRGB(red, green, blue), 1);
    }
}
