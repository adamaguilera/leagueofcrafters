package leagueofcrafters.team;

import org.bukkit.ChatColor;

public abstract class Goal {
    public abstract String getObjectiveName ();
    public abstract int getTaskPriority ();
    public abstract boolean isComplete ();
    public abstract int getProgress ();
    public abstract int getMaxProgress ();

    public double getProgressPercent () { return getProgress() / (double) getMaxProgress(); }

    public String getProgressBar () {
        int curr = getProgress();
        int max = getMaxProgress();
        if (curr >= max) {
            curr = max;
        } else if (curr < 0) {
            curr = 0;
        }
        int completed = (int) (((double) curr) / max * 10);
        int remaining = 10 - completed;
        return ChatColor.RED + "#" + getTaskPriority() + " [" + ChatColor.GREEN +
                "=".repeat(Math.max(0, completed)) +
                "-".repeat(Math.max(0, remaining)) +
                ChatColor.RED + "]" + ChatColor.GREEN + " " +
                (curr == max ? "COMPLETED!" : curr + "/" + max);
    }
}
