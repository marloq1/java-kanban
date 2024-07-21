public class SubTask extends Task {

    //Epic epic;

    public SubTask(String name, String description,Status status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription()+"}"
                ;
    }
    @Override
    public String getId() {
        return "id"+Math.abs(this.hashCode()*29);

    }
}
