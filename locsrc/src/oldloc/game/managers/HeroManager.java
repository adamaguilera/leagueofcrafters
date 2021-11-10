package oldloc.game.managers;

import oldloc.player.HeroRecord;
import oldloc.player.User;

import java.util.ArrayList;
import java.util.List;

public class HeroManager {
    List<HeroRecord> heroes;

    public HeroManager () {
        heroes = new ArrayList<>();
    }

    public List<HeroRecord> getHeroes () {
        return List.of();
    }

    public Runnable generateRun (final User t) {
        return new Runnable() {
            User user = t;
            @Override
            public void run() {
            }
        };
    }
}
