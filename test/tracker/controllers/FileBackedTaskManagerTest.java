package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    public void testFileSaveAndLoad() {
        TaskManager taskManager = new FileBackedTaskManager();
        Task task1 = new Task("Задача 1", "Действие таска 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Действие таска 2", Status.NEW);
        Epic epic1 = new Epic("Эпик 1", "Действие эпика 1");
        SubTask subTask11 = new SubTask("Подзадача 1", "Действие подтаска 1", Status.DONE);
        SubTask subTask12 = new SubTask("Подзадача 2", "Действие подтаска 2", Status.NEW);
        SubTask subTask13 = new SubTask("Подзадача 3", "Действие подтаска 2", Status.NEW);
        Epic epic2 = new Epic("Эпик 2", "Действие эпика 2");
        SubTask subTask21 = new SubTask("Подзадача 1", "Действие", Status.DONE);
        taskManager.taskPut(task1);
        taskManager.taskPut(task2);
        taskManager.epicsPut(epic1);
        taskManager.subTaskPut(epic1, subTask11);
        taskManager.subTaskPut(epic1, subTask12);
        taskManager.subTaskPut(epic1, subTask13);
        taskManager.epicsPut(epic2);
        taskManager.subTaskPut(epic2, subTask21);
        List<Task> tasksBeforeLoad = taskManager.getTasks();
        List<Epic> epicsBeforeLoad = taskManager.getEpics();
        List<SubTask> subTasksBeforeLoad = taskManager.getSubtasks();
        taskManager = FileBackedTaskManager.loadFromFile(Paths.get("resources\\TasksMemory").toFile());
        List<Task> tasksAfterLoad = taskManager.getTasks();
        List<Epic> epicsAfterLoad = taskManager.getEpics();
        List<SubTask> subTasksAfterLoad = taskManager.getSubtasks();
        for (int i=0;i<tasksBeforeLoad.size();i++){
            assertEquals(tasksBeforeLoad.get(i),tasksAfterLoad.get(i));
        }
        for (int i=0;i<epicsBeforeLoad.size();i++){
            assertEquals(epicsBeforeLoad.get(i),epicsAfterLoad.get(i));
        }
        for (int i=0;i<subTasksBeforeLoad.size();i++){
            assertEquals(subTasksBeforeLoad.get(i),subTasksAfterLoad.get(i));
        }
    }

    @Test
    public void testEmptyFileSaveAndLoad(){
        FileBackedTaskManager emptyManager = new FileBackedTaskManager();
        emptyManager.save();
        assertTrue(emptyManager.tasks.isEmpty());
        assertTrue(emptyManager.epics.isEmpty());
        assertTrue(emptyManager.subtasks.isEmpty());
        emptyManager = FileBackedTaskManager.loadFromFile(Paths.get("resources\\TasksMemory").toFile());
        assertTrue(emptyManager.tasks.isEmpty());
        assertTrue(emptyManager.epics.isEmpty());
        assertTrue(emptyManager.subtasks.isEmpty());
    }



}