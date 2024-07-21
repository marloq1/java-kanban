package tracker;

import java.util.*;

public class Epic extends Task{

    private final Map<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    List<Integer> getSubtaskIds() {
        return new ArrayList<>(subTasks.keySet());
    }

    void removeSubtask(int id) {
        subTasks.remove(id);
    }

    void cleanSubtaskIds() {
        subTasks.clear();
    }


    @Override
    public String toString() {
        return "Epic{" +
                "status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription()  +
                ", {subTasks=" + subTasks+"}}";
    }
}
