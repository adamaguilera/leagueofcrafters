package som.player;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import som.spells.helper.DamageType;

import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DamageManager {
    @NotNull
    final Mage mage;
    final double DAMAGE_THRESHOLD = 1.25;
    final double DUMMY_DAMAGE = 0.1;

    public void damage (final double rawDamage, final DamageType type, final Entity source) {
        double resistance = getResistance(type);
        double damage = type == DamageType.PHYSICAL ?
                Math.max(0, rawDamage * (1.0 - resistance) - DAMAGE_THRESHOLD) :
                Math.max(0, rawDamage * (1.0 - resistance));
        Bukkit.broadcastMessage("Raw damage: " + rawDamage);
        Bukkit.broadcastMessage("Resistance: " + resistance);
        Bukkit.broadcastMessage("Fin Damage: " + damage);
        mage.getPlayer().ifPresent(player -> {
            // mock dummy damage
            player.damage(DUMMY_DAMAGE, source);
            // remove player health
            player.setHealth(Math.max(0, player.getHealth() - damage));
        });
    }

    public double getResistance (final DamageType type) {
        if (mage.getPlayer().isPresent() && type != DamageType.PURE) {
            Player player = mage.getPlayer().get();
            return Stream.of(player.getInventory().getArmorContents())
                    .filter(Objects::nonNull)
                    .mapToDouble(item -> getArmorResistance(item.getType()))
                    .sum();
        }
       return 0.0;
    }

    public double getArmorResistance (final Material material) {
        switch (material) {
            case CHAINMAIL_HELMET:
            case LEATHER_HELMET:
                return 0.08;
            case CHAINMAIL_CHESTPLATE:
            case LEATHER_CHESTPLATE:
                return 0.14;
            case CHAINMAIL_LEGGINGS:
            case LEATHER_LEGGINGS:
                return 0.12;
            case CHAINMAIL_BOOTS:
            case LEATHER_BOOTS:
                return 0.06;
            case IRON_HELMET:
                return 0.10;
            case IRON_CHESTPLATE:
                return 0.16;
            case IRON_LEGGINGS:
                return 0.14;
            case IRON_BOOTS:
                return 0.10;
            case GOLDEN_HELMET:
                return 0.16;
            case GOLDEN_CHESTPLATE:
                return 0.25;
            case GOLDEN_LEGGINGS:
                return 0.23;
            case GOLDEN_BOOTS:
                return 0.16;
            case DIAMOND_HELMET:
                return 0.12;
            case DIAMOND_CHESTPLATE:
                return 0.19;
            case DIAMOND_LEGGINGS:
                return 0.17;
            case DIAMOND_BOOTS:
                return 0.12;
            case NETHERITE_HELMET:
                return 0.13;
            case NETHERITE_CHESTPLATE:
                return 0.21;
            case NETHERITE_LEGGINGS:
                return 0.18;
            case NETHERITE_BOOTS:
                return 0.13;
            default:
                return 0.0;
        }
    }

    public DamageType getDamageType (EntityDamageEvent.DamageCause cause) {
        switch (cause) {
            case BLOCK_EXPLOSION:
            case DRAGON_BREATH:
            case ENTITY_EXPLOSION:
            case FIRE:
            case FIRE_TICK:
            case FREEZE:
            case HOT_FLOOR:
            case LAVA:
            case LIGHTNING:
            case MAGIC:
            case MELTING:
            case POISON:
                return DamageType.MAGICAL;
            case CONTACT:
            case ENTITY_ATTACK:
            case FALL:
            case FALLING_BLOCK:
            case FLY_INTO_WALL:
                return DamageType.PHYSICAL;
            case CUSTOM:
            case CRAMMING:
            case DROWNING:
            case DRYOUT:
            case ENTITY_SWEEP_ATTACK:
            case STARVATION:
            case SUFFOCATION:
            case SUICIDE:
            case THORNS:
            case VOID:
            case WITHER:
                return DamageType.PURE;
            case PROJECTILE:
            default:
                return DamageType.PIERCING;
        }
    }

    public void onEntityDamageByEntity (EntityDamageByEntityEvent event) {
        if (event.getDamage() > DAMAGE_THRESHOLD) {
            event.setCancelled(true);
            damage(event.getDamage(), getDamageType(event.getCause()), event.getDamager());
        }
    }
}
