package tracker;

public class Result {
    private TaskType taskType;
    private Epic epic;

    public Result(TaskType taskType,Epic epic) {
        this.taskType=taskType;
        this.epic=epic;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Epic getEpic() {
        return epic;
    }
}
