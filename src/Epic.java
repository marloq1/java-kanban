import java.util.*;

public class Epic extends Task{

    private final Map<String, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String description,Status status) {
        super(name, description,status);
    }



    @Override
    public String getId() {
        return "id"+(this.hashCode()*17);

    }

    Map<String, SubTask> getSubTasks() {
        return subTasks;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription()  +
                ", {subTasks=" + subTasks+"}}";
    }
}
