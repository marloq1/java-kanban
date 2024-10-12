package tracker.controllers;

import tracker.model.SubTask;
import tracker.model.Task;
import tracker.model.Epic;


import java.util.List;
import java.util.Optional;

public interface TaskManager {
    int taskPut(Task task);

    boolean taskReplace(int id, Task task);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    List<Task> getTasks();

    List<SubTask> getSubtasks();

    List<Epic> getEpics();

    int epicsPut(Epic epic);

    void epicReplace(int id, Epic epic);

    int subTaskPut(Epic epic, SubTask subTask);

    boolean subTaskReplace(int id, Epic epic, SubTask subTask);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    Optional<Task> getTask(int id);

    Optional<Epic> getEpic(int id);

    Optional<SubTask> getSubTask(int id);

    List<SubTask> getSubTasksOfEpic(int id);

    HistoryManager getHistoryManager();

    List<Task> getPrioritizedTasks();

    boolean validateTask(Task task);
}
