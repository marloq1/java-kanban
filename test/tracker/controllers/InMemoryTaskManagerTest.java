package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.model.TaskType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    private Task task1;
    private Task task2;
    private Epic epic1;
    private SubTask subTask11;
    private SubTask subTask12;
    private Epic epic2;
    private SubTask subTask21;
    private int idT1,idT2,idE1,idSt1,idSt2,idE2,idSt3;

    //Невозможно добавить в эпик эпик на уровне компиляции кода
    //Невозможно сделать SubTask своим же эпиком на уровне компиляции кода

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        task1 = new Task("Задача 1", "Действие таска 1", Status.NEW);
        task2 = new Task("Задача 2", "Действие таска 2", Status.NEW);
        epic1 = new Epic("Эпик 1", "Действие эпика 1");
        subTask11 = new SubTask("Подзадача 1", "Действие подтаска 1", Status.DONE);
        subTask12 = new SubTask("Подзадача 2", "Действие подтаска 2", Status.NEW);
        epic2 = new Epic("Эпик 2", "Действие эпика 2");
        subTask21 = new SubTask("Подзадача 1", "Действие", Status.DONE);
        idT1 = taskManager.taskPut(task1);
        idT2 = taskManager.taskPut(task2);
        idE1 = taskManager.epicsPut(epic1);
        idSt1 = taskManager.subTaskPut(epic1, subTask11);
        idSt2 = taskManager.subTaskPut(epic1, subTask12);
        idE2 = taskManager.epicsPut(epic2);
        idSt3 = taskManager.subTaskPut(epic2, subTask21);

    }
    @Test
    public void TaskManagerCanFindTasksById() {

        assertEquals(task1,taskManager.getTask(idT1));
        assertEquals(task2,taskManager.getTask(idT2));
        assertEquals(epic1,taskManager.getEpic(idE1));
        assertEquals(subTask11,taskManager.getSubTask(idSt1));
        assertEquals(subTask12,taskManager.getSubTask(idSt2));
        assertEquals(epic2,taskManager.getEpic(idE2));
        assertEquals(subTask21,taskManager.getSubTask(idSt3));

    }

    @Test
    public void TaskImmutability() {

        int idT1 = taskManager.taskPut(task1);
        Task taskTest1 = taskManager.getTask(idT1);
        taskManager.epicsPut(epic1);
        Task taskTest2 = taskManager.getTask(idT1);
        assertEquals(taskTest1,taskTest2);
        assertEquals(taskTest1.getStatus(),taskTest2.getStatus());
        taskManager.subTaskPut(epic1,subTask11);
        taskTest2 = taskManager.getTask(idT1);
        assertEquals(taskTest1,taskTest2);
        assertEquals(taskTest1.getStatus(),taskTest2.getStatus());
    }

    @Test
    public void HistoryCheck() {
        HistoryManager historyManager = taskManager.getHistoryManager();
        Task taskTest1 = taskManager.getTask(idT1);//0 //deleted
        Task taskTest2 = taskManager.getTask(idT2);//1 //new 0
        taskManager.getEpic(idE1);//2 //new 1
        Epic epicTest2 = taskManager.getEpic(idE2);//3 //new 2
        taskManager.getSubTask(idSt1);//4 //new 3
        taskManager.getTask(idT2);//5 //new 4
        SubTask subTaskTest1 = taskManager.getSubTask(idSt2);//6 //new 5
        taskManager.getTask(idT1);//7 //new 6
        taskManager.getTask(idT2);//8 //new 7
        Epic epicTest1 = taskManager.getEpic(idE1);//9 //new 8
        assertEquals(historyManager.getHistory().getFirst(),taskTest1);
        assertEquals(historyManager.getHistory().get(6),subTaskTest1);
        assertEquals(historyManager.getHistory().getLast(),epicTest1);
        SubTask subTaskTest2 = taskManager.getSubTask(idSt1);//new 9
        assertEquals(historyManager.getHistory().getFirst(),taskTest2);
        assertEquals(historyManager.getHistory().getLast(),subTaskTest2);
        assertEquals(historyManager.getHistory().get(2),epicTest2);


    }

    @Test
    public void TasksReplace() {
        assertEquals(task1,taskManager.getTask(idT1));
        Task taskTest = new Task("Задача 1", "Действие таска тест", Status.NEW);
        taskManager.taskReplace(idT1,taskTest);
        assertNotEquals(task1.getDescription(),taskManager.getTask(idT1).getDescription());
        assertEquals(taskTest,taskManager.getTask(idT1));

        assertEquals(subTask11,taskManager.getSubTask(idSt1));
        SubTask subTaskTest = new SubTask("Подзадача 1", "Действие подтаска тест", Status.DONE);
        taskManager.subTaskReplace(idSt1,epic1,subTaskTest);
        assertNotEquals(subTask11.getDescription(),taskManager.getSubTask(idSt1).getDescription());
        assertEquals(subTaskTest,taskManager.getSubTask(idSt1));

        assertEquals(epic1,taskManager.getEpic(idE1));
        Epic epicTest = new Epic("Эпик 1", "Действие эпика тест");
        taskManager.epicReplace(idE1,epicTest);
        assertNotEquals(epic1.getDescription(),taskManager.getEpic(idE1).getDescription());
        assertEquals(epicTest,taskManager.getEpic(idE1));

    }

    @Test
    public void TaskDelete() {
        assertEquals(task1,taskManager.getTask(idT1));
        taskManager.deleteTask(idT1);
        assertNull(taskManager.getTask(idT1));

        assertEquals(subTask11,taskManager.getSubTask(idSt1));
        taskManager.deleteSubtask(idSt1);
        assertNull(taskManager.getSubTask(idSt1));

        assertEquals(epic1,taskManager.getEpic(idE1));
        List<SubTask> subtasks = taskManager.getSubtasks();
        taskManager.deleteEpic(idE1);
        List<SubTask> subtasks1 = taskManager.getSubtasks();

        assertNull(taskManager.getEpic(idE1));
        assertNotEquals(subtasks,subtasks1);
    }

    @Test
    public void TaskListDelete() {
        List<Task> tasks=taskManager.getTasks();

        taskManager.deleteTasks();
        List<Task> tasks1=taskManager.getTasks();
        assertNotEquals(tasks,tasks1);

        List<SubTask> subTasks = taskManager.getSubtasks();
        taskManager.deleteSubtasks();
        List<SubTask> subTasks1 = taskManager.getSubtasks();
        //List subTasks1 = taskManager.getEpics();
        assertNotEquals(subTasks,subTasks1);

        List<Epic> epics = taskManager.getEpics();
        taskManager.deleteEpics();
        List<Epic> epics1 = taskManager.getEpics();
        assertNotEquals(epics, epics1);

    }

    @Test
    public void scanIdCheck() {
        assertEquals(task1.getType(), TaskType.TASK);
        assertEquals(epic1.getType(), TaskType.EPIC);
        assertEquals(subTask11.getType(), TaskType.SUBTASK);
    }

    @Test
    public void EpicStatus() {
        assertEquals(epic1.getStatus(),Status.IN_PROGRESS);
        taskManager.subTaskReplace(idSt1,epic1,new SubTask("Подзадача 3", "Действие подтаска 1", Status.NEW));
        assertEquals(epic1.getStatus(),Status.NEW);
        taskManager.subTaskPut(epic1, new SubTask("Подзадача 4", "Действие подтаска 1", Status.DONE));
        assertEquals(epic1.getStatus(),Status.IN_PROGRESS);
        taskManager.subTaskReplace(idSt1,epic1,new SubTask("Подзадача 5", "Действие подтаска 1", Status.DONE));
        assertEquals(epic1.getStatus(),Status.IN_PROGRESS);
        taskManager.subTaskReplace(idSt2,epic1,new SubTask("Подзадача 6", "Действие подтаска 1", Status.DONE));
        assertEquals(epic1.getStatus(),Status.DONE);
        taskManager.deleteSubtasks();
        assertEquals(epic1.getStatus(),Status.NEW);
        taskManager.subTaskPut(epic1,new SubTask("Подзадача 6", "Действие подтаска 1", Status.IN_PROGRESS));
        assertEquals(epic1.getStatus(),Status.IN_PROGRESS);

    }






}