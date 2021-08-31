package som.spells.helper;


import lombok.Builder;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import loc.Main;

@Builder
public class RunnableTask {
    private int taskID = Integer.MIN_VALUE;
    @NotNull
    private final Runnable onTaskTick;
    @NotNull
    private final Runnable onTaskEnd;

    /**
     * @return returns empty runnable that does nothing
     */
    public static Runnable emptyRunnable () {
        return () -> {};
    }

    /**
     * cancel task related to this task, will run onTaskEnd if true passed in
     */
    public void cancelTask (final boolean runTaskEnd) {
        if (runTaskEnd) onTaskEnd.run();
        Bukkit.getServer().getScheduler().cancelTask(taskID);
    }

    /**
     * Creates a task that triggers onTaskTick every second,
     * Automatically cancels event when duration is over
     * @param duration duration for how long to run the runnable in seconds
     */
    public void createTask(int duration) {
        createTickTask(20L, duration);
    }

    /**
     * Creates a task that triggers
     * Automatically cancels event when duration is over
     * @param ticksPerPeriod ticks in between each iteration
     * @param iterations number of iterations to run the task
     * example execution: 20L, 5 -> runs 5 times each second
     */
    public void createTickTask (long ticksPerPeriod, int iterations) {
        // cancel previous task
        cancelTask(false);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        this.taskID = scheduler.scheduleSyncRepeatingTask(Main.GET_INSTANCE(), new Runnable() {
            double time = iterations;
            @Override
            public void run() {
                if (time-- <= 0) {
                    // cancel task
                    cancelTask(true);
                }
                // otherwise trigger on second
                onTaskTick.run();
            }
        }, 0, ticksPerPeriod);
    }
}

