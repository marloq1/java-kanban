import java.util.Objects;

public class Task {
    private Status status;
    private String name;
    private String description;

    public Task(String name, String description,Status status) {
        this.name = name;
        this.description = description;
        this.status=status;
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
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\''+"}";
    }

    public String getId() {
        return "id"+Math.abs((this.hashCode()*31));

    }
}
