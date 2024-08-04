package tracker.controllers;

import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;


import java.util.List;
import java.util.Map;

public interface TaskManager {
    int taskPut(Task task);

    void taskReplace(int id, Task task);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    List<Task> getTasks();

    List<SubTask> getSubtasks();

    List<Epic> getEpics();

    int epicsPut(Epic epic);

    void epicReplace(int id, Epic epic);

    int subTaskPut(Epic epic, SubTask subTask);

    void subTaskReplace(int id, Epic epic, SubTask subTask);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    List <SubTask> getSubTasksOfEpic (int id);

    public HistoryManager getHistoryManager();
}
