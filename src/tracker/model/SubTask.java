package tracker.model;

public class SubTask extends Task {

    private Epic epic;

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s\n",(getId()),
                getType(),getName(),
                getStatus(),
                getDescription(),getEpicId());
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



    public static SubTask fromString(String s){
        String[] parameters = s.split(",");
        return new SubTask(parameters[2], parameters[4], Status.valueOf(parameters[3]));
    }



    public int getEpicId() {
        return epic.getId();
    }

}
