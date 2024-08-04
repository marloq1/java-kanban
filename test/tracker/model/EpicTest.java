package tracker.model;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private Epic epic1;
    private Epic epic2;

    @Test
    public void showEqualEpicsWithEqualId() {
        epic1 = new Epic("Задача 1", "Действие");
        epic2=new Epic("Задача 1", "Действие");
        epic1.setId(100);
        epic2.setId(100);
        assertTrue(epic1.getId()==epic2.getId());
        assertTrue(epic1.equals(epic2));
        assertEquals(epic1.getId()==epic2.getId(),epic1.equals(epic2));
    }

    @Test
    public void showNotEqualEpicsWithNotEqualId() {
        epic1 = new Epic("Задача 1", "Действие");
        epic2=new Epic("Задача 2", "Действие");
        epic1.setId(98);
        epic2.setId(100);
        assertFalse(epic1.getId()==epic2.getId());
        assertFalse(epic1.equals(epic2));
        assertEquals(epic1.getId()==epic2.getId(),epic1.equals(epic2));
    }

}