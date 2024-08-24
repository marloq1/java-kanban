package tracker.controllers;

import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.model.Epic;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();


    private int generatorId = 0;


    @Override
    public int taskPut(Task task) { //d) Добавление новой задачи
        final int id = ++generatorId;
        task.setId(id);
        tasks.put(id, task);
        return id;

    }

    @Override
    public int epicsPut(Epic epic) { //d) Добавление нового эпика
        final int id = ++generatorId;
        epic.setId(id);
        epics.put(id, epic);
        updateEpicStatus(epic);
        return id;
    }

    @Override
    public int subTaskPut(Epic epic, SubTask subTask) { //d) Добавление новой подзадачи в эпик
        final int id = ++generatorId;
        subTask.setId(id);
        epic.getSubTasksMap().put(id, subTask);
        subtasks.put(id, subTask);
        subTask.setEpic(epic);
        updateEpicStatus(epic);
        return id;
    }


    @Override
    public void taskReplace(int id, Task task) { //e) Обновление задачи
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.put(id, task);
            task.setId(id);
        }
    }

    @Override
    public void epicReplace(int id, Epic epic) { //e) Обновление эпика
        if (epics.containsKey(id)) {
            historyManager.remove(id);
            for (Integer subtaskId : epics.get(id).getSubtaskIds()) {
                historyManager.remove(subtaskId);
                subtasks.remove(subtaskId);
            }
            epics.put(id, epic);
            epic.setId(id);

            updateEpicStatus(epic);
        }
    }

    @Override
    public void subTaskReplace(int id, Epic epic, SubTask subTask) {//e) Обновление подзадачи
        if (subtasks.containsKey(id)) {
            historyManager.remove(id);
            epic.getSubTasksMap().put(id, subTask);
            subtasks.put(id,subTask);
            subTask.setId(id);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }
    @Override
    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        historyManager.remove(id);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
    }
    @Override
    public void deleteSubtask(int id) {
        SubTask subtask = subtasks.remove(id);
        historyManager.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic);
    }


    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }









    @Override
    public Task getTask(int id) { //c) Получение задачи по идентификатору

        if (tasks.containsKey(id)){
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else
            return null;
    }

    @Override
    public Epic getEpic(int id) {// c) Получение эпика с подзадачами по идентификатору

        if (epics.containsKey(id)){
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else
            return null;

    }
    @Override
    public SubTask getSubTask(int id) {
        if (subtasks.containsKey(id)){
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else
            return null;
    }

    @Override
    public List <SubTask> getSubTasksOfEpic (int id) {// 3.a) Получение списка всех подзадач определенного эпика
        if (epics.containsKey(id)) {
            return epics.get(id).getSubTasks();
        } else {
            return null;
        }
    }



    public void updateEpicStatus(Epic epic) { //4.a) Управление статусами
        int marker1 = 0,marker2=0,marker3=0;
        for (int key: epic.getSubTasksMap().keySet()) {
            if ((epic.getSubTasksMap().get(key).getStatus()== Status.NEW) && (marker1==0)){
                marker1=1;
            }
            if ((epic.getSubTasksMap().get(key).getStatus()==Status.DONE) && (marker2==0)){
                marker2=1;
            }
            if ((epic.getSubTasksMap().get(key).getStatus()==Status.IN_PROGRESS) && (marker3==0)){
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






    @Override
    public void deleteTasks() {
        for (Task task: tasks.values()){
            historyManager.remove(task.getId());
        }
        tasks.clear();

    }
    @Override
    public void deleteSubtasks() {
        for (Task task: subtasks.values()){
            historyManager.remove(task.getId());
        }
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic);
        }
        subtasks.clear();

    }
    @Override
    public void deleteEpics() {
        for (Task task: epics.values()){
            historyManager.remove(task.getId());
        }
        for (Task task: subtasks.values()){
            historyManager.remove(task.getId());
        }
        epics.clear();
        subtasks.clear();
    }
    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }




    }
