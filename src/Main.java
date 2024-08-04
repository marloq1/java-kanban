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
        Epic epic2 = new Epic("Эпик 2", "Действие");
        SubTask subTask21 = new SubTask("Подзадача 1", "Действие", Status.DONE);
        int idT1 = taskManager.taskPut(task1);
        int idT2 = taskManager.taskPut(task2);
        int idE1 = taskManager.epicsPut(epic1);
        int idSt1 = taskManager.subTaskPut(epic1, subTask11);
        int idSt2 = taskManager.subTaskPut(epic1, subTask12);
        int idE2 = taskManager.epicsPut(epic2);
        int idSt3 = taskManager.subTaskPut(epic2, subTask21);
        /*System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        task1 = new Task("Задача 1", "Действие", Status.DONE);
        epic2 = new Epic("Эпик 4", "Действие");
        subTask11 = new SubTask("Подзадача 1", "Действие", Status.NEW);
        taskManager.taskReplace(idT1,task1);
        taskManager.epicReplace(idE2,epic2);
        taskManager.subTaskReplace(idSt1,epic1,subTask11);
        System.out.println();
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        taskManager.deleteTask(idT1);
        taskManager.deleteEpic(idE1);
        taskManager.deleteSubtask(idSt3);
        System.out.println();
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        System.out.println();
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());*/
        System.out.println(taskManager.getTask(idT1));//1
        System.out.println(taskManager.getTask(idT2));//2
        System.out.println(taskManager.getEpic(idE1));//3
        System.out.println(taskManager.getEpic(idE2));//4
        System.out.println(taskManager.getSubTask(idSt1));//5
        System.out.println(taskManager.getTask(idT2));//6
        System.out.println(taskManager.getSubTask(idSt2));//7
        System.out.println(taskManager.getTask(idT1));//8
        System.out.println(taskManager.getTask(idT2));//9
        System.out.println(taskManager.getEpic(idE1));//10
        System.out.println(taskManager.getSubTask(idSt1));
        System.out.println();
        printAllTasks(taskManager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubTasksOfEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistoryManager().getHistory()) {
            System.out.println(task);
        }
    }
}
