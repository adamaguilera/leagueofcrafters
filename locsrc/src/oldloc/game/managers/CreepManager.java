package oldloc.game.managers;

import oldloc.Main;
import oldloc.creep.Creep;
import oldloc.game.Game;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CreepManager {
    @Getter
    List<Creep> creeps = new ArrayList<>();

    public Game getGame () {
        return Main.GET_GAME();
    }

    public Optional<Creep> getCreep (final UUID id) {
        return creeps.stream().filter(creep -> creep.matches(id)).findFirst();
    }
}
