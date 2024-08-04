package tracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    private SubTask subTask1;
    private SubTask subTask2;

    @Test
    public void showEqualEpicsWithEqualId() {
        subTask1 = new SubTask("Задача 1", "Действие",Status.DONE);
        subTask2 =new SubTask("Задача 1", "Действие",Status.NEW);
        subTask1.setId(100);
        subTask2.setId(100);
        assertTrue(subTask1.getId()== subTask2.getId());
        assertTrue(subTask1.equals(subTask2));
        assertEquals(subTask1.getId()== subTask2.getId(), subTask1.equals(subTask2));
    }

    @Test
    public void showNotEqualEpicsWithNotEqualId() {
        subTask1 = new SubTask("Задача 1", "Действие",Status.DONE);
        subTask2 =new SubTask("Задача 2", "Действие",Status.NEW);
        subTask1.setId(98);
        subTask2.setId(100);
        assertFalse(subTask1.getId()== subTask2.getId());
        assertFalse(subTask1.equals(subTask2));
        assertEquals(subTask1.getId()== subTask2.getId(), subTask1.equals(subTask2));
    }

}