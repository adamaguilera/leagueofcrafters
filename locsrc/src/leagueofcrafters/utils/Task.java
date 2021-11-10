package leagueofcrafters.utils;

public interface Task {
    enum TaskType {
        INSTANT, HALF_SECOND, SECOND
    }
//    TaskType getTaskType ();
    void onTick();
}
