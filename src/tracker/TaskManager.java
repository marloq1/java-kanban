package tracker;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subtasks = new HashMap<>();

    private int generatorId = 0;


    public int taskPut(Task task) { //d) Добавление новой задачи
        final int id = ++generatorId;
        task.setId(id);
        tasks.put(id, task);
        return id;

    }

    public Task scanId(int id) {// Вспомогательный метод для определения типа задачи по id


        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        if (epics.containsKey(id)) {
            return epics.get(id);
        }
        if (subtasks.containsKey(id)){
           return subtasks.get(id);
        }
        return null;

    }
    public void taskReplace(int id,Task task) { //e) Обновление задачи
        tasks.put(id,task);
    }


    public void deleteTask(int id) {
        tasks.remove(id);
    }
    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
    }
    public void deleteSubtask(int id) {
        SubTask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic);
    }


    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubtasks() {
        ArrayList<SubTask> tasks = new ArrayList<>(subtasks.values());
        return tasks;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> tasks = new ArrayList<>(epics.values());
        return tasks;
    }



    public int epicsPut(Epic epic) { //d) Добавление нового эпика
        final int id = ++generatorId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }



    public void epicReplace(int id,Epic epic) { //e) Обновление эпика
        for (Integer subtaskId : epics.get(id).getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        epics.put(id,epic);

        updateEpicStatus(epic);
    }

    public Task showTask(int id) { //c) Получение задачи по идентификатору

        return tasks.getOrDefault(id, null);
    }
    public Epic showEpic(int id) {// c) Получение эпика с подзадачами по идентификатору

        return epics.getOrDefault(id, null);
    }
    public Map<Integer,SubTask> showSubTasks (int id) {// 3.a) Получение списка всех подзадач определенного эпика
        if (epics.containsKey(id)) {
            return epics.get(id).getSubTasks();
        } else {
            return null;
        }
    }

    public void updateEpicStatus(Epic epic) { //4.a) Управление статусами
        int marker1 = 0,marker2=0,marker3=0;
        for (int key: epic.getSubTasks().keySet()) {
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
        } else if ((marker1==1 && marker2==0 && marker3==0) ||  (marker1==0 && marker2==0 && marker3==0))  {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public int subTaskPut(Epic epic, SubTask subTask) { //d) Добавление новой подзадачи в эпик
        final int id = ++generatorId;
        subTask.setId(id);
        epic.getSubTasks().put(id, subTask);
        subtasks.put(id,subTask);
        subTask.setEpic(epic);
        updateEpicStatus(epic);
        return id;
    }
    public void subTaskReplace(int id,Epic epic,SubTask subTask) {//e) Обновление подзадачи
        epic.getSubTasks().put(id,subTask);
        updateEpicStatus(epic);
    }



    public void deleteTasks() {
        tasks.clear();
    }
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }




}
