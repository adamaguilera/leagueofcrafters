package oldloc.player.managers;

import oldloc.player.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DamageManager {
    @NotNull @Getter
    final User user;
    final double DUMMY_DAMAGE = 0.1;

    public void consume (final int amount) {
        this.user.getPlayer().ifPresent(player -> {
            int foodLevel = Math.max(0, player.getFoodLevel() - amount);
            player.setFoodLevel(foodLevel);
        });
    }

    public void damage (final double pureDamage, final Entity source) {
        this.user.getPlayer().ifPresent(player -> {
            // mock dummy damage
            player.damage(DUMMY_DAMAGE, source);
            // remove player health
            player.setHealth(Math.max(0, player.getHealth() - pureDamage));
        });
    }
}
