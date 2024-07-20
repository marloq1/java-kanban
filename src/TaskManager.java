import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private final Map<String, Task> tasks = new HashMap<>();
    private final Map<String, Epic> epics = new HashMap<>();



    public void taskPut(Task task) {

            tasks.put(task.getId(), task);

    }

    public Result scanId(String id) {
        int count=0;
        Epic epic=new Epic("None","None",Status.NEW);
        if (tasks.containsKey(id)) {
            count=1;
        }
        if (epics.containsKey(id)) {
            count=2;
        }
        for (Epic e: epics.values()) {
            if (e.getSubTasks().containsKey(id)) {
                epic =e;
                count=3;
            }
        }
        return new Result(count,epic);
    }
    public void taskReplace(String id,Task task) {
        tasks.put(id,task);
    }

    public void taskDelete(String id) {
        if (tasks.remove(id)!=null) {
            System.out.println("Объект успешно удален");
            return;
        }
        if (epics.remove(id)!=null) {
            System.out.println("Объект успешно удален");
            return;
        }
        for (Epic e: epics.values()) {
            if (e.getSubTasks().remove(id)!=null) {

                if (e.getSubTasks().isEmpty()) {
                    for (String key: epics.keySet()) {
                        if (epics.get(key).equals(e)) {
                            e.setStatus(Status.NEW);
                            System.out.println("Объект успешно удален");
                            return;
                        }
                    }
                }
                checkEpicStatus(e);
                System.out.println("Объект успешно удален");
            }
            return;

        }

        System.out.println("Такого объекта нет");
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }



    public void epicsPut(Epic epic) {
        epics.put(epic.getId(),epic);
    }

    public Map<String, Epic> getEpics() {
        return epics;
    }

    public void epicReplace(String id,Epic epic) {
        epics.put(id,epic);
    }

    public Task showTask(String id) {

        return tasks.getOrDefault(id, null);
    }
    public Epic showEpic(String id) {

        return epics.getOrDefault(id, null);
    }
    public Map<String,SubTask> showSubTasks (String id) {
        if (epics.containsKey(id)) {
            return epics.get(id).getSubTasks();
        } else {
            return null;
        }
    }

    public void checkEpicStatus(Epic epic) {
        int marker1 = 0,marker2=0,marker3=0;
        for (String key: epic.getSubTasks().keySet()) {
            if ((epic.getSubTasks().get(key).getStatus()==Status.NEW) && (marker1==0)){
                marker1=1;
            }
            if ((epic.getSubTasks().get(key).getStatus()==Status.DONE) && (marker2==0)){
                marker2=1;
            }
            if ((epic.getSubTasks().get(key).getStatus()==Status.IN_PROGRESS) && (marker3==0)){
                marker3=1;
            }
        }
        if (marker1==0 && marker2==1 && marker3==0) {
            epic.setStatus(Status.DONE);
        } else if (marker1==1 && marker2==0 && marker3==0) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void  subTaskPut(Epic epic, SubTask subTask) {
        epic.getSubTasks().put(subTask.getId(),subTask);
    }
    public void subTaskReplace(String id,Epic epic,SubTask subTask) {
        epic.getSubTasks().put(id,subTask);
    }

    public void Clear() {
        tasks.clear();
        epics.clear();
    }


}
