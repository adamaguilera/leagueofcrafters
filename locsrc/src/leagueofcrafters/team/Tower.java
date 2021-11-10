package leagueofcrafters.team;

import leagueofcrafters.team.config.TowerConfig;
import leagueofcrafters.utils.config.RGB;
import leagueofcrafters.utils.graphics.LineDrawer;
import leagueofcrafters.utils.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class Tower extends Goal implements Task {
    final EntityType type = EntityType.ARMOR_STAND;
    @Getter
    final TaskType taskType = TaskType.HALF_SECOND;
    final int attackDelay = 1250;

    final TeamName teamName;
    final TowerConfig towerConfig;
    @Getter
    final int taskPriority;

    UUID id;
    double health;
    long nextAttack;

    UUID target;

    public boolean isComplete () { return this.id == null; }
    public int getProgress () { return getMaxProgress() - (int) this.health; }
    public int getMaxProgress () { return this.towerConfig.getMaxHealth(); }
    public String getObjectiveName() { return "Destroy the " + this.towerConfig.getTowerName() + "!"; }

    public void spawn () {
        ArmorStand stand = (ArmorStand) towerConfig.getCenter().getWorld().spawnEntity(towerConfig.getCenter(), type);
        stand.setRemoveWhenFarAway(false);
        stand.setInvisible(true);
        stand.setInvulnerable(true);
        stand.setSmall(true);
        stand.setCollidable(false);
        stand.setPersistent(true);
        stand.setSilent(true);
        this.id = stand.getUniqueId();
        this.health = towerConfig.getMaxHealth();
    }

    public Optional<ArmorStand> getStand () {
        if (this.id != null) {
            Entity stand = Bukkit.getEntity(id);
            return stand instanceof ArmorStand ? Optional.of((ArmorStand) stand) : Optional.empty();
        }
        return Optional.empty();
    }

    public Optional<Team> getTeam () { return teamName.toTeam(); }
    public boolean onTeam (@NotNull final UUID id) { return getTeam().filter(team -> team.onTeam(id)).isPresent(); }

    public void attack () {
        if (System.currentTimeMillis() >= nextAttack) {
            getStand().ifPresent(tower -> getTarget().ifPresent(target -> {
                new LineDrawer(towerConfig.getCenter(), target.getLocation(), towerConfig.getDust(), 0.20).draw();
                target.damage(2, tower);
                this.nextAttack = System.currentTimeMillis() + attackDelay;
            }));
        }
    }

    public void drawAggro () {
        getStand().flatMap(tower -> getTarget()).ifPresent(target -> new LineDrawer(towerConfig.getCenter(), target.getLocation(), new RGB(192, 192, 192), 1.5).draw());
    }

    public double calculateDamage (final Material block, final Material pick) {
        double modifier = switch (pick) {
            case NETHERITE_PICKAXE -> 4.0;
            case DIAMOND_PICKAXE -> 2.5;
            case IRON_PICKAXE -> 1.25;
            case STONE_PICKAXE, WOODEN_PICKAXE -> 1.0;
            default -> 0.5;
        };
        return modifier * switch (block) {
            case BEACON -> 5.0;
            case ANDESITE, STONE_BRICK_WALL -> 1.0;
            default -> 0.1;
        };
    }

    public void takeDamage (@NotNull final Material block, @NotNull final Material pick, @NotNull LivingEntity source) {
        this.health -= calculateDamage(block, pick);
        if (this.health <= 0.0) {
            this.id = null;
        }
    }
    
    public Optional<LivingEntity> getTarget () {
        if (this.target != null) {
            Entity entity = Bukkit.getEntity(target);
            return entity instanceof LivingEntity ? Optional.of((LivingEntity) entity) : Optional.empty();
        }
        return Optional.empty();
    }
    
    public boolean validTarget () {
        return getTarget().stream().anyMatch(target -> !target.isDead() && !onTeam(target.getUniqueId()) &&
                towerConfig.getRegion().inside(target.getLocation()));
    }

    public void updateTarget () {
        if (!validTarget()) {
            this.target = null;
            double diameter = towerConfig.getRegion().maxDiameter();
            towerConfig.getCenter().getWorld().getNearbyEntities(towerConfig.getCenter(), diameter, diameter, diameter)
                    .stream()
                    .filter(entity -> !(entity instanceof ArmorStand) && entity instanceof LivingEntity &&
                            !onTeam(entity.getUniqueId()) && this.towerConfig.getRegion().inside(entity.getLocation())).min((o1, o2) ->
                            (int) (Math.abs(towerConfig.getCenter().distance(o1.getLocation())) - Math.abs(towerConfig.getCenter().distance(o2.getLocation()))))
                    .ifPresent(nextTarget -> this.target = nextTarget.getUniqueId());
        }
    }

    @Override
    public void onTick() {
        if (getStand().isPresent()) {
            updateTarget();
            drawAggro();
            attack();
        }
    }

    public void onDisable () {
        getStand().ifPresent(Entity::remove);
    }
}
