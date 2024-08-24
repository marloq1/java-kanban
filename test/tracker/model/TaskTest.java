package tracker.model;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task1;
    private Task task2;

    @Test
    public void showEqualTasksWithEqualId() {
        task1 = new Task("Задача 1", "Действие", Status.NEW);
        task2 = new Task("Задача 1", "Действие", Status.DONE);
        task1.setId(100);
        task2.setId(100);
        assertTrue(task1.getId() == task2.getId());
        assertTrue(task1.equals(task2));
        assertEquals(task1.getId() == task2.getId(), task1.equals(task2));

    }

    @Test
    public void showNotEqualTasksWithNotEqualId() {
        task1 = new Task("Задача 2", "Действие", Status.NEW);
        task2 = new Task("Задача 1", "Действие", Status.DONE);
        task1.setId(98);
        task2.setId(100);
        assertFalse(task1.getId() == task2.getId());
        assertFalse(task1.equals(task2));
        assertEquals(task1.getId() == task2.getId(), task1.equals(task2));
    }
}