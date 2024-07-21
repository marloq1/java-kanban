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

    /*@Override
    public int hashCode() {
        return Math.abs(Objects.hash(getName(), getDescription())*17);
    }*/

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
        return "tracker.Epic{" +
                "status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription()  +
                ", {subTasks=" + subTasks+"}}";
    }
}
