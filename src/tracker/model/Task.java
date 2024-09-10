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
        return String.format("%d,%s,%s,%s,%s\n",(getId()),
                getType(),getName(),
                getStatus(),
                getDescription());
    }

    public int getId() {
        return id;

    }

    public static Task fromString(String s) {
        String[] parameters = s.split(",");
        return new Task(parameters[2], parameters[4], Status.valueOf(parameters[3]));
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public void setId(int id) {
        this.id = id;
    }
}
