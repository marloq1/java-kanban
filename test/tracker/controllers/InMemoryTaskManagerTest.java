package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.model.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    private Task task1;
    private Task task2;
    private Epic epic1;
    private SubTask subTask11;
    private SubTask subTask12;
    private SubTask subTask13;
    private Epic epic2;
    private SubTask subTask21;
    private int idT1, idT2, idE1, idSt1, idSt2, idE2, idSt3, idSt4;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        task1 = new Task("Задача 1", "Действие таска 1", Status.NEW);
        task2 = new Task("Задача 2", "Действие таска 2", Status.NEW);
        epic1 = new Epic("Эпик 1", "Действие эпика 1");
        subTask11 = new SubTask("Подзадача 1", "Действие подтаска 1", Status.DONE);
        subTask12 = new SubTask("Подзадача 2", "Действие подтаска 2", Status.NEW);
        subTask13 = new SubTask("Подзадача 3", "Действие подтаска 2", Status.NEW);
        epic2 = new Epic("Эпик 2", "Действие эпика 2");
        subTask21 = new SubTask("Подзадача 1", "Действие", Status.DONE);
        idT1 = taskManager.taskPut(task1);
        idT2 = taskManager.taskPut(task2);
        idE1 = taskManager.epicsPut(epic1);
        idSt1 = taskManager.subTaskPut(epic1, subTask11);
        idSt2 = taskManager.subTaskPut(epic1, subTask12);
        idSt3 = taskManager.subTaskPut(epic1, subTask13);
        idE2 = taskManager.epicsPut(epic2);
        idSt4 = taskManager.subTaskPut(epic2, subTask21);


    }

    @Test
    public void TaskManagerCanFindTasksById() {

        assertEquals(task1, taskManager.getTask(idT1).get());
        assertEquals(task2, taskManager.getTask(idT2).get());
        assertEquals(epic1, taskManager.getEpic(idE1).get());
        assertEquals(subTask11, taskManager.getSubTask(idSt1).get());
        assertEquals(subTask12, taskManager.getSubTask(idSt2).get());
        assertEquals(subTask13, taskManager.getSubTask(idSt3).get());
        assertEquals(epic2, taskManager.getEpic(idE2).get());
        assertEquals(subTask21, taskManager.getSubTask(idSt4).get());

    }

    @Test
    public void TaskImmutability() {

        int idT1 = taskManager.taskPut(task1);
        Task taskTest1 = taskManager.getTask(idT1).get();
        taskManager.epicsPut(epic1);
        Task taskTest2 = taskManager.getTask(idT1).get();
        assertEquals(taskTest1, taskTest2);
        assertEquals(taskTest1.getStatus(), taskTest2.getStatus());
        taskManager.subTaskPut(epic1, subTask11);
        taskTest2 = taskManager.getTask(idT1).get();
        assertEquals(taskTest1, taskTest2);
        assertEquals(taskTest1.getStatus(), taskTest2.getStatus());
    }

    @Test
    public void HistoryCheck() {
        HistoryManager historyManager = taskManager.getHistoryManager();

        Task taskTest1 = taskManager.getTask(idT1).get();
        Task taskTest2 = taskManager.getTask(idT2).get();
        Epic epicTest1 = taskManager.getEpic(idE1).get();
        SubTask subTaskTest1 = taskManager.getSubTask(idSt1).get();

        Iterator iterator = historyManager.getHistory().iterator();
        assertEquals(iterator.next(), taskTest1);
        assertEquals(iterator.next(), taskTest2);
        assertEquals(iterator.next(), epicTest1);
        assertEquals(iterator.next(), subTaskTest1);

         taskTest1 = taskManager.getTask(idT1).get();
        iterator = historyManager.getHistory().iterator();
        assertEquals(iterator.next(), taskTest2);
        assertEquals(iterator.next(), epicTest1);
        assertEquals(iterator.next(), subTaskTest1);
        assertEquals(iterator.next(), taskTest1);

        taskTest1 = taskManager.getTask(idT1).get();
        assertEquals(historyManager.getHistory().get(0),taskTest2);
        assertEquals(historyManager.getHistory().get(3),taskTest1);
        historyManager.remove(idT1);
        assertTrue(historyManager.getHistory().size()==3);
        historyManager.remove(idT2);
        assertEquals(historyManager.getHistory().get(0),epicTest1);
        assertEquals(historyManager.getHistory().get(1),subTaskTest1);
        taskTest2 =taskManager.getTask(idT2).get();
        historyManager.remove(idSt1);
        assertEquals(historyManager.getHistory().get(0),epicTest1);
        assertEquals(historyManager.getHistory().get(1),taskTest2);

    }


    @Test
    public void TasksReplace() {
        assertEquals(task1, taskManager.getTask(idT1).get());
        Task taskTest = new Task("Задача 1", "Действие таска тест", Status.NEW);
        taskManager.taskReplace(idT1, taskTest);
        assertNotEquals(task1.getDescription(), taskManager.getTask(idT1).get().getDescription());
        assertEquals(taskTest, taskManager.getTask(idT1).get());

        assertEquals(subTask11, taskManager.getSubTask(idSt1).get());
        SubTask subTaskTest = new SubTask("Подзадача 1", "Действие подтаска тест", Status.DONE);
        taskManager.subTaskReplace(idSt1, epic1, subTaskTest);
        assertNotEquals(subTask11.getDescription(), taskManager.getSubTask(idSt1).get().getDescription());
        assertEquals(subTaskTest, taskManager.getSubTask(idSt1).get());

        assertEquals(epic1, taskManager.getEpic(idE1).get());
        Epic epicTest = new Epic("Эпик 1", "Действие эпика тест");
        taskManager.epicReplace(idE1, epicTest);
        assertNotEquals(epic1.getDescription(), taskManager.getEpic(idE1).get().getDescription());
        assertEquals(epicTest, taskManager.getEpic(idE1).get());

    }

    @Test
    public void TaskDelete() {
        assertEquals(task1, taskManager.getTask(idT1).get());
        taskManager.deleteTask(idT1);
        assertTrue(taskManager.getTask(idT1).isEmpty());

        assertEquals(subTask11, taskManager.getSubTask(idSt1).get());
        taskManager.deleteSubtask(idSt1);
        assertTrue(taskManager.getSubTask(idSt1).isEmpty());
        assertFalse(taskManager.getEpic(idE1).get().getSubtaskIds().contains(idSt1));

        assertEquals(epic1, taskManager.getEpic(idE1).get());
        List<SubTask> subtasks = taskManager.getSubtasks();
        taskManager.deleteEpic(idE1);
        List<SubTask> subtasks1 = taskManager.getSubtasks();

        assertTrue(taskManager.getEpic(idE1).isEmpty());
        assertNotEquals(subtasks, subtasks1);
    }

    @Test
    public void TaskListDelete() {
        List<Task> tasks = taskManager.getTasks();

        taskManager.deleteTasks();
        List<Task> tasks1 = taskManager.getTasks();
        assertNotEquals(tasks, tasks1);

        List<SubTask> subTasks = taskManager.getSubtasks();
        taskManager.deleteSubtasks();
        List<SubTask> subTasks1 = taskManager.getSubtasks();
        //List subTasks1 = taskManager.getEpics();
        assertNotEquals(subTasks, subTasks1);

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
        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
        taskManager.subTaskReplace(idSt1, epic1, new SubTask("Подзадача 3", "Действие подтаска 1", Status.NEW));
        assertEquals(epic1.getStatus(), Status.NEW);
        taskManager.subTaskPut(epic1, new SubTask("Подзадача 4", "Действие подтаска 1", Status.DONE));
        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
        taskManager.subTaskReplace(idSt1, epic1, new SubTask("Подзадача 5", "Действие подтаска 1", Status.DONE));
        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);
        taskManager.subTaskReplace(idSt2, epic1, new SubTask("Подзадача 6", "Действие подтаска 1", Status.DONE));
        taskManager.subTaskReplace(idSt3, epic1, new SubTask("Подзадача 7", "Действие подтаска 1", Status.DONE));
        assertEquals(epic1.getStatus(), Status.DONE);
        taskManager.deleteSubtasks();
        assertEquals(epic1.getStatus(), Status.NEW);
        taskManager.subTaskPut(epic1, new SubTask("Подзадача 6", "Действие подтаска 1", Status.IN_PROGRESS));
        assertEquals(epic1.getStatus(), Status.IN_PROGRESS);

    }

    @Test
    public void TasksDeleteFromHistory() {
        HistoryManager historyManager = taskManager.getHistoryManager();
        Task task1 = taskManager.getTask(idT1).get();//1
        taskManager.getTask(idT2);//2
        Epic epic1 = taskManager.getEpic(idE1).get();//3
        taskManager.getEpic(idE2);//4
        SubTask subTask1 = taskManager.getSubTask(idSt1).get();//5
        taskManager.getTask(idT2);//6
        SubTask subTask2 = taskManager.getSubTask(idSt2).get();//7
        taskManager.getTask(idT1);//8
        taskManager.getTask(idT2);//9
        taskManager.getSubTask(idSt3);
        taskManager.getEpic(idE1);//10
        SubTask subTask3 = taskManager.getSubTask(idSt3).get();//11
        assertEquals(historyManager.getHistory().get(3), task1);
        taskManager.deleteTask(idT1);
        assertFalse(historyManager.getHistory().contains(task1));
        assertEquals(historyManager.getHistory().get(4), epic1);
        assertEquals(historyManager.getHistory().get(1), subTask1);
        assertEquals(historyManager.getHistory().get(2), subTask2);
        assertEquals(historyManager.getHistory().get(5), subTask3);
        taskManager.deleteEpic(idE1);
        assertFalse(historyManager.getHistory().contains(epic1));
        assertFalse(historyManager.getHistory().contains(subTask1));
        assertFalse(historyManager.getHistory().contains(subTask2));
        assertFalse(historyManager.getHistory().contains(subTask3));


    }

    @Test
    public void intersectionCheck(){
        Task newTask = new Task("Задача 1", "Действие", Status.NEW,
                LocalDateTime.of(2024,9,22,13,30), Duration.ofHours(1));
        taskManager.taskReplace(idT1, newTask);
        SubTask newSubTask1 = new SubTask("Подзадача 2", "Действие", Status.NEW,
                LocalDateTime.of(2024,9,22,11,30), Duration.ofHours(2));
        taskManager.subTaskReplace(idSt2,epic1,newSubTask1);
        SubTask newSubTask2 = new SubTask("Подзадача 3", "Действие", Status.IN_PROGRESS,
                LocalDateTime.of(2024,9,22,10,30), Duration.ofHours(1));
        taskManager.subTaskReplace(idSt3,epic1,newSubTask2);
        assertEquals(taskManager.getTask(idT1).get(),newTask);
        assertEquals(taskManager.getSubTask(idSt2).get(),newSubTask1);
        assertEquals(taskManager.getSubTask(idSt3).get(),newSubTask2);
        Task crossedTask = new Task("Задача 1", "Действие", Status.NEW,
                LocalDateTime.of(2024,9,22,12,30), Duration.ofHours(1));
        taskManager.taskReplace(idT1,crossedTask);
        assertNotEquals(taskManager.getTask(idT1).get(),crossedTask);
        SubTask crossedSubTask = new SubTask("Подзадача 2", "Действие", Status.NEW,
                LocalDateTime.of(2024,9,22,11,0), Duration.ofHours(2));
        taskManager.subTaskReplace(idSt1,epic1,crossedSubTask);

    }


}