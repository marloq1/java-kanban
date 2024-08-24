package tracker.model;

import java.util.Objects;

public class Task {
    private Status status;
    private final String name;
    private final String description;
    private int id;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;

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
        return task.id == id;

    }

    public Status getStatus() {
        return status;

    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "Task{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' + "}";
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
