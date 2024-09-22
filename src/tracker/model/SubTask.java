package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private Epic epic;

    public SubTask(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, status,startTime,duration);
    }

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }


    @Override
    public String toString() {
        if (getEndTime().isEmpty()) {
            return String.format("%d,%s,%s,%s,%s,%s\n", (getId()),
                    getType(), getName(),
                    getStatus(),
                    getDescription(), getEpicId());
        } else {

            String startDate = getStartTime().format(formatter);
            Long duration = getDuration().toMinutes();
            return String.format("%d,%s,%s,%s,%s,%s,%s,%d минут\n", (getId()),
                    getType(), getName(),
                    getStatus(),
                    getDescription(),getEpicId(),startDate,duration);
        }
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }


    public Epic getEpic() {
        return epic;
    }



    public static SubTask fromString(String s) {

        String[] parameters = s.split(",");
        if (parameters.length<=6) {
            return new SubTask(parameters[2], parameters[4], Status.valueOf(parameters[3]));
        } else
            return new SubTask(parameters[2],
                    parameters[4],
                    Status.valueOf(parameters[3]),
                    LocalDateTime.parse(parameters[6],formatter),
                    Duration.ofMinutes(Integer.parseInt(parameters[7].split(" ")[0])));
    }



    public int getEpicId() {
        return epic.getId();
    }

}
