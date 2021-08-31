package som.task;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import som.Game;
import loc.Main;
import som.task.basic.LootDropper;
import som.task.basic.ScoreboardTask;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    @NotNull
    private final List<Task> tasks;
    final long TASK_DELAY = 1L;
    final long TASK_PERIOD = 10L;
    private int taskID;

    public TaskManager (@NotNull final Game game) {
        this.tasks = new ArrayList<>();
        addTask(new LootDropper(game.getTeams()));
        addTask(new ScoreboardTask(game.getTeams(), game.getMAX_SCORE()));
    }

    public boolean addTask (@NotNull final Task task) {
        return this.tasks.add(task);
    }
    public boolean removeTask (@NotNull final Task task) {
        return this.tasks.remove(task);
    }
    public void start () {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.GET_INSTANCE(),
                this::onTick, TASK_DELAY, TASK_PERIOD);
    }
    public void end () {
        Bukkit.getScheduler().cancelTask(this.taskID);
    }
    private void onTick () {
        this.tasks.forEach(Task::onTick);
    }
}
