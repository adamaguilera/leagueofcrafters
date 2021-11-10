package oldloc.game;

import oldloc.game.managers.CreepManager;
import oldloc.game.managers.PlayerManager;
import oldloc.game.managers.UserManager;
import oldloc.game.managers.TeamManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Game {
    public enum GameState {
        LOBBY, GAME, COMPLETED
    }
    @Getter
    private GameState state = GameState.LOBBY;
    @Getter
    final TeamManager teamManager = new TeamManager();
    @Getter
    final UserManager userManager = new UserManager();
    @Getter
    final PlayerManager playerManager = new PlayerManager(this);
    @Getter
    final CreepManager creepManager = new CreepManager();

    public void start () {
        this.state = GameState.GAME;
    }
}
