package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {

    private LocalDateTime endTime;

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


    public static Epic fromString(String s) {
        String[] parameters = s.split(",");
        return new Epic(parameters[2], parameters[4]);
    }

    public void cleanSubtaskIds() {
        subTasks.clear();
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        if (getStartTime()!=null){
            return Optional.of(endTime);
        } else {
            return Optional.empty();
        }
    }

    public void updateTime() {
        boolean isFirst = true;
        LocalDateTime bufStart = null;
        LocalDateTime bufEnd = null;
        for (SubTask subTask:subTasks.values()){
            if (subTask.getEndTime().isPresent()){
                if (isFirst){
                    bufStart = subTask.getStartTime();
                    bufEnd = subTask.getEndTime().get();
                    isFirst = false;
                }
                if (subTask.getStartTime().isBefore(bufStart)){
                    bufStart = subTask.getStartTime();
                }
                if (subTask.getEndTime().get().isAfter(bufEnd)){
                    bufEnd = subTask.getEndTime().get();
                }
            }
        }
        if (bufStart!=null){
            setStartTime(bufStart);
            endTime = bufEnd;
            setDuration(Duration.between(getStartTime(), endTime));
        } else {
            setStartTime(null);
            endTime = null;
            setDuration(null);
        }

    }
}
