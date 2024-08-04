package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import tracker.model.Epic;
import tracker.model.Status;
import org.junit.jupiter.api.Test;
import tracker.model.SubTask;
import tracker.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private Task task1;
    private Task task2;
    private Epic epic1;
    private SubTask subTask11;
    private SubTask subTask12;
    private Epic epic2;
    private SubTask subTask21;

    @BeforeEach
    public void beforeEach() {
        task1 = new Task("Задача 1", "Действие", Status.NEW);
        task2=new Task("Задача 1", "Действие", Status.DONE);
        TaskManager taskManager = new InMemoryTaskManager();
        epic1= new Epic("Эпик 1", "Действие");
        subTask11 = new SubTask("Подзадача 1", "Действие", Status.DONE);
        subTask12 = new SubTask("Подзадача 2", "Действие", Status.NEW);
        epic2 = new Epic("Эпик 2", "Действие");
        subTask21 = new SubTask("Подзадача 1", "Действие", Status.DONE);
        taskManager.taskPut(task1);
        taskManager.taskPut(task2);
        taskManager.epicsPut(epic1);
        taskManager.subTaskPut(epic1, subTask11);
        taskManager.subTaskPut(epic1, subTask12);
        taskManager.epicsPut(epic2);
        taskManager.subTaskPut(epic2, subTask21);
    }

    @Test
    public void showEqualTasksWithEqualId() {
        assertTrue(task1.getId()==task2.getId());
        assertTrue(task1.equals(task2));
        assertEquals(task1.getId()==task2.getId(),task1.equals(task2));
    }

    @Test
    public void showEqualEpicsWithEqualId() {
        assertEquals(epic1.getId()==epic2.getId(),epic1.equals(epic2));
    }


}