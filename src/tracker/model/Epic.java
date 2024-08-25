package tracker.model;

import java.util.*;

public class Epic extends Task {

    private final Map<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public Map<Integer, SubTask> getSubTasksMap() {
        return subTasks;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }


    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subTasks.keySet());
    }

    public void removeSubtask(int id) {
        subTasks.remove(id);
    }

    public void cleanSubtaskIds() {
        subTasks.clear();
    }


    @Override
    public String toString() {
        return "Epic{" +
                "status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() +
                ", {subTasks=" + subTasks + "}}";
    }
}
