package oldloc.player;

import oldloc.game.Team;
import oldloc.player.heroes.Recruit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class User {
    @Getter @NotNull
    final UUID id;
    @Getter @Setter @NotNull
    Team team;
    @NotNull @Setter @Getter
    protected Hero hero;

    public User (@NotNull final UUID id, @NotNull final Team team) {
        this.id = id;
        this.team = team;
        this.team.addUser(this);
        this.hero = new Recruit(this);
    }


    /**
     * Reset's player's inventory and sets their health to max and health to max
     * and clears all status effects
     */
    public void reset () {
        getPlayer().ifPresent(player -> {
            player.getInventory().clear();
            AttributeInstance MAX_HEALTH = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            player.setHealth(MAX_HEALTH != null ? MAX_HEALTH.getDefaultValue() : 20);
            player.setFoodLevel(20);
            player.setSaturation(5.0f);
            List<PotionEffectType> activeEffects = player.getActivePotionEffects().stream().map(PotionEffect::getType).collect(Collectors.toList());
            activeEffects.forEach(player::removePotionEffect);
        });
    }

    public Optional<Player> getPlayer () {
        return Optional.ofNullable(Bukkit.getPlayer(this.id));
    }
    public boolean matches (final UUID id) {
        return this.id.equals(id);
    }
}
