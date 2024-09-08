import tracker.controllers.*;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.model.Epic;

import java.nio.file.Paths;

public class Main {
    public static TaskManager taskManager;


    public static void main(String[] args) {


        taskManager = new FileBackedTaskManager();
        Task task1 = new Task("Задача 1", "Действие", Status.NEW);
        Task task2 = new Task("Задача 2", "Действие", Status.NEW);
        Epic epic1 = new Epic("Эпик 1", "Действие");
        SubTask subTask11 = new SubTask("Подзадача 1", "Действие", Status.DONE);
        SubTask subTask12 = new SubTask("Подзадача 2", "Действие", Status.NEW);
        SubTask subTask13 = new SubTask("Подзадача 3", "Действие", Status.IN_PROGRESS);
        Epic epic2 = new Epic("Эпик 2", "Действие");
        taskManager.taskPut(task1);
        taskManager.taskPut(task2);
        taskManager.epicsPut(epic1);
        taskManager.subTaskPut(epic1, subTask11);
        taskManager.subTaskPut(epic1, subTask12);
        taskManager.subTaskPut(epic1, subTask13);
        taskManager.epicsPut(epic2);
        taskManager = FileBackedTaskManager.loadFromFile(Paths.get("C:\\Users\\yogua\\dev\\java-kanban.txt").toFile());


    }
}
