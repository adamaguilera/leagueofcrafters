package som.spells.basic;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import loc.Main;
import som.chat.ItemText;
import som.player.Mage;
import som.spells.Spell;
import som.spells.SpellName;
import som.spells.helper.Cooldown;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BrokenCompass extends Spell {
    @NotNull
    final Mage mage;
    final int cost = 4;
    final int cd = 25*1000;
    final SpellName name = SpellName.BROKEN_COMPASS;
    final Material displayMaterial = Material.COMPASS;
    final List<Material> castableItems = List.of(Material.COMPASS);
    final List<String> displayLore = ItemText.FORMAT_LORE(List.of(
            "A tracker that tells you the",
            "direction of the nearest player",
            "CD: 25s"));
    final int SEARCH_RADIUS = 100;
    final String NO_ENEMIES_FOUND = "No enemies found in 100 block radius!";
    Cooldown cooldown;


    /**
     * @return mage with this spell
     */
    @Override
    public Mage getMage() {
        return this.mage;
    }

    /**
     * @return mana cost of this spell
     */
    @Override
    public int cost() {
        return this.cost;
    }

    /**
     * @return cooldown manager of this spell
     */
    @Override
    public Cooldown getCooldown() {
        if (this.cooldown == null) {
            this.cooldown = Cooldown.builder()
                    .playerID(getPlayerID())
                    .cooldownMessage(getCdMessage())
                    .cooldownMillis(cd)
                    .build();
        }
        return this.cooldown;
    }

    /**
     * @return name of this spell
     */
    @Override
    public SpellName getSpellName() {
        return this.name;
    }

    /**
     * @return display material on spell scroll / spell book
     */
    @Override
    public Material getDisplayMaterial() {
        return this.displayMaterial;
    }

    /**
     * @return lore for spell on spell scroll / spell book
     */
    @Override
    public List<String> getDisplayLore() {
        return this.displayLore;
    }

    /**
     * @return items that can cast this spell
     */
    @Override
    public List<Material> getCastableItems() {
        return this.castableItems;
    }

    private Optional<Mage> getClosestEnemy () {
        return getPlayer().isPresent() ?
                getPlayer().get()
                        .getNearbyEntities(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS)
                        .stream()
                        .filter(entity -> (entity instanceof Player) &&
                                entity.getUniqueId() != getPlayerID())
                        .map(entity -> Main.GET_MAGE(entity.getUniqueId()))
                        .filter(mage -> !getMage().sameTeam(mage))
                        .min((o1, o2) -> {
                            if (getPlayer().isPresent() &&
                                    o1.getPlayer().isPresent() &&
                                    o2.getPlayer().isPresent()) {
                                Location o1Loc = o1.getPlayer().get().getLocation();
                                Location o2Loc = o2.getPlayer().get().getLocation();
                                Location baseLoc = getPlayer().get().getLocation();
                                return Double.compare(baseLoc.distanceSquared(o1Loc),
                                        baseLoc.distanceSquared(o2Loc));
                            }
                            return 0;
                        })
            : Optional.empty();
    }

    private String getDirection (@NotNull final Player player, @NotNull final Player target) {
        Vector vector = target.getLocation().toVector()
                        .subtract(player.getLocation().toVector());
        Vector playerDirection = player.getEyeLocation().getDirection();
        double angle = (vector.angle(playerDirection) * 180 / Math.PI) % 360;
        return getDirection(angle);
    }

    private String getDirection (@NotNull final double angle) {
        if (angle >= 30 && angle < 60) {
            return "North East";
        }
        if (angle >= 60 && angle < 120) {
            return "North";
        }
        if (angle >= 120 && angle < 150) {
            return "North West";
        }
        if (angle >= 150 && angle < 210) {
            return "West";
        }
        if (angle >= 210 && angle < 240) {
            return "South West";
        }
        if (angle >= 240 && angle < 300) {
            return "South";
        }
        if (angle >= 300 && angle < 330) {
            return "South East";
        }
        // 330 inclusive - 30 not inclusive
        return "East";
    }

    private String formatMessage (@NotNull String distance, @NotNull String direction) {
        return "An enemy is " + distance + " blocks " + direction + " from you!";
    }

    /**
     * Casts spell
     */
    @Override
    public void cast() {
        getPlayer().ifPresent(player -> {
            Optional<Mage> closestEnemy = getClosestEnemy();
            if (closestEnemy.isPresent()) {
                closestEnemy.get().getPlayer().ifPresent(enemyPlayer -> {
                    String distance = String.format("%.2f",
                            player.getLocation().distance(enemyPlayer.getLocation()));
                    String direction = getDirection(player, enemyPlayer);
                    getMage().sendAbilityMessage(formatMessage(distance, direction));
                });
            } else {
                getMage().sendAbilityMessage(NO_ENEMIES_FOUND);
            }
        });
    }
}
