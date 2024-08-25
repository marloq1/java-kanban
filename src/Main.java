import tracker.controllers.*;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.model.Epic;

public class Main {
    public static TaskManager taskManager;


    public static void main(String[] args) {

        taskManager = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Действие", Status.NEW);
        Task task2 = new Task("Задача 2", "Действие", Status.NEW);
        Epic epic1 = new Epic("Эпик 1", "Действие");
        SubTask subTask11 = new SubTask("Подзадача 1", "Действие", Status.DONE);
        SubTask subTask12 = new SubTask("Подзадача 2", "Действие", Status.NEW);
        SubTask subTask13 = new SubTask("Подзадача 3", "Действие", Status.IN_PROGRESS);
        Epic epic2 = new Epic("Эпик 2", "Действие");
        int idT1 = taskManager.taskPut(task1);
        int idT2 = taskManager.taskPut(task2);
        int idE1 = taskManager.epicsPut(epic1);
        int idSt1 = taskManager.subTaskPut(epic1, subTask11);
        int idSt2 = taskManager.subTaskPut(epic1, subTask12);
        int idSt3 = taskManager.subTaskPut(epic1, subTask13);
        int idE2 = taskManager.epicsPut(epic2);
        taskManager.getTask(idT1); //1
        taskManager.getTask(idT2); //2
        taskManager.getEpic(idE1); //3
        taskManager.getEpic(idE2); //4
        System.out.println();
        printHistory(taskManager);
        taskManager.getSubTask(idSt1); //5
        taskManager.getTask(idT2); //6
        taskManager.getSubTask(idSt2); //7
        taskManager.getTask(idT1); //8
        System.out.println();
        printHistory(taskManager);
        taskManager.getTask(idT2); //9
        taskManager.getSubTask(idSt3);
        taskManager.getEpic(idE1); //10
        taskManager.getSubTask(idSt1);
        System.out.println();
        printHistory(taskManager);
        taskManager.deleteTask(idT1);
        System.out.println();
        printHistory(taskManager);
        taskManager.deleteEpic(idE1);
        System.out.println();
        printHistory(taskManager);


    }

    private static void printHistory(TaskManager manager) {
        System.out.println("История:");
        for (Task task : manager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }
    }
}
