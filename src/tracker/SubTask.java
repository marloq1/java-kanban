package tracker;

import java.util.Objects;

public class SubTask extends Task {

    private Epic epic;

    public SubTask(String name, String description,Status status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        return "tracker.SubTask{" +
                "status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription()+"}"
                ;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public Epic getEpic() {
        return epic;
    }

    public int getEpicId() {
        return epic.getId();
    }
    /*  @Override
    public int hashCode() {
        return Math.abs(Objects.hash(getName(), getDescription())*29);
    }*/
}
