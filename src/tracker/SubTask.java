package tracker;

import java.util.Objects;

public class SubTask extends Task {

    private Epic epic;

    public SubTask(String name, String description,Status status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription()+"}"
                ;
    }

    void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }


    public Epic getEpic() {
        return epic;
    }

    public int getEpicId() {
        return epic.getId();
    }

}
