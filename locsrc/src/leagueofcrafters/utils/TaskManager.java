package leagueofcrafters.utils;

import leagueofcrafters.Main;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static final long TASK_DELAY = 0L;
    private static final long TASK_PERIOD = 10L;

    List<Task> instantTasks = new ArrayList<>();
    List<Task> halfSecondTasks = new ArrayList<>();
    List<Task> secondTasks = new ArrayList<>();

    @Getter
    int taskId;

    public void start () {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        this.taskId = scheduler.scheduleSyncRepeatingTask(Main.GET(),
                this::onTick, TASK_DELAY, TASK_PERIOD);
    }

    public void onTick () {
        halfSecondTasks.forEach(Task::onTick);
    }

    public void addTasks(@NotNull final List<Task> tasks) { tasks.forEach(this::addTask); }
    public void addTask (@NotNull final Task task) {
        halfSecondTasks.add(task);
//        switch (task.getTaskType()) {
//            case SECOND -> secondTasks.add(task);
//            case HALF_SECOND -> halfSecondTasks.add(task);
//            case INSTANT -> instantTasks.add(task);
//        }
    }


}
