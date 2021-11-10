package oldloc.creep;

import oldloc.creep.creeps.ZombieCreep;
import oldloc.game.Team;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Wave {
    public enum CreepType {
        MELEE, RANGED, GUARDIAN
    }
    public record CreepInfo(CreepType type, int amount) { }
    final List<CreepInfo> types;


    public List<Creep> getCreeps (@NotNull final Team team) {
        return types.stream().map(info -> toCreep(info, team)).collect(Collectors.toList());
    }

    public Creep toCreep (@NotNull final CreepInfo creepInfo, @NotNull final Team team) {
        switch (creepInfo.type) {
            case MELEE:
            default:
                return new ZombieCreep(team);
        }
    }

}
