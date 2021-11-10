package leagueofcrafters.game;

import leagueofcrafters.utils.TaskManager;
import leagueofcrafters.utils.chat.Log;
import leagueofcrafters.team.TeamManager;
import leagueofcrafters.user.User;
import leagueofcrafters.user.UserManager;
import leagueofcrafters.utils.RunnableTask;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Game {
    public enum GameState {
        LOBBY, GAME, ENDED
    }
    @Getter
    final GameConfig config;
    @Getter
    final UserManager userManager;
    @Getter
    final TeamManager teamManager;
    @Getter
    final TaskManager taskManager;

    @Getter
    GameState gameState = GameState.LOBBY;
    RunnableTask startTask = RunnableTask.builder()
            .onTaskTick(() -> Log.BROADCAST("Countdown in progress..."))
            .onTaskEnd(() -> {
                if (canStart()) start();
                else Log.BROADCAST("A player has unreadied and the countdown has cancelled.");
            })
            .build();

    public Game (@NotNull final GameConfig config) {
        this.config = config;
        this.taskManager = new TaskManager();
        this.userManager = new UserManager();
        this.teamManager = new TeamManager(config.createTeams());
        taskManager.addTasks(teamManager.getTasks());
    }

    private boolean canStart () {
        return userManager.getUsersList()
                .stream()
                .filter(User::isReadyAndValid)
                .count() == userManager.size();
    }

    public void start () {
        Log.BROADCAST("Game is starting...");
        teamManager.spawnTowers();
        teamManager.assignPlayers(userManager.getUsersList());
        userManager.getUsersList().forEach(User::gameDefault);
        taskManager.start();
        this.gameState = GameState.GAME;
        Log.BROADCAST("Game has started, good luck!");
    }

    public void ready (boolean readied) {
        long ready = userManager.getUsersList()
                .stream()
                .filter(User::isReadyAndValid)
                .count();
        int total = userManager.size();
        if (readied) Log.BROADCAST("A player has readied up, (" + ready + "/" + total + ")!");
        else startTask.cancelTask(true);
        if (canStart()) {
            startTask.createTickTask(20, 5);
        }
    }

    public void onDisable () {
        this.teamManager.onDisable();
    }
}
