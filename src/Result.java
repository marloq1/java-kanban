public class Result {
    private int count;
    private Epic epic;

    public Result(int count,Epic epic) {
        this.count=count;
        this.epic=epic;
    }

    public int getCount() {
        return count;
    }

    public Epic getEpic() {
        return epic;
    }
}
