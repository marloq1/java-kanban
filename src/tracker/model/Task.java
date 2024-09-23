package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private Status status;
    private final String name;
    private final String description;
    private int id;
    private Duration duration;
    private LocalDateTime startTime;
    protected static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyyг");


    public Task(String name, String description, Status status,LocalDateTime startTime,Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;

    }

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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        if (getEndTime().isEmpty()) {
            return String.format("%d,%s,%s,%s,%s\n", (getId()),
                    getType(), getName(),
                    getStatus(),
                    getDescription());
        } else {
            String startDate = getStartTime().format(formatter);
            Long duration = getDuration().toMinutes();
            return String.format("%d,%s,%s,%s,%s,%s,%d минут\n", (getId()),
                    getType(), getName(),
                    getStatus(),
                    getDescription(), startDate, duration);
        }
    }

    public int getId() {
        return id;

    }

    public static Task fromString(String s) {
        String[] parameters = s.split(",");
        if (parameters.length <= 5) {
            return new Task(parameters[2], parameters[4], Status.valueOf(parameters[3]));
        } else
            return new Task(parameters[2],
                    parameters[4],
                    Status.valueOf(parameters[3]),
                    LocalDateTime.parse(parameters[5], formatter),
                    Duration.ofMinutes(Integer.parseInt(parameters[6].split(" ")[0])));
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Optional<LocalDateTime> getEndTime() {
        if ((duration != null) && (startTime != null)) {
            return Optional.of(startTime.plus(duration));
        } else {
            return Optional.empty();
        }
    }
}
