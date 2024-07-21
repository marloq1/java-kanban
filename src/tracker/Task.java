package tracker;

import java.util.Objects;

public class Task {
    private Status status;
    private String name;
    private String description;
    private int id;

    public Task(String name, String description,Status status) {
        this.name = name;
        this.description = description;
        this.status=status;

    }
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status);

    }

    public Status getStatus() {
            return status;

    }

    void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }



    @Override
    public String toString() {
        return "Task{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\''+"}";
    }

    public int getId() {
        return id;

    }
    public TaskType getType() {
        return TaskType.TASK;
    }

    public void setId(int id) {
        this.id = id;
    }
}
