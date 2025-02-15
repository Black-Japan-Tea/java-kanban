package task_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Epic epic1;

    @BeforeEach
    void setUp() {
        epic1 = new Epic("name", "description");
    }

    @Test
    void shouldAddSubTaskAndGetTasksIds() {
        epic1.addSubtask(1);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);

        assertEquals(list, epic1.getSubtasks());
    }

    @Test
    void shouldRemoveSubtask() {
        epic1.addSubtask(1);
        epic1.rmvSubtaskById(1);

        ArrayList<Integer> list = new ArrayList<>();
        assertEquals(list, epic1.getSubtasks());

    }

    @Test
    void shouldRemoveAllSubtasks() {
        epic1.addSubtask(1);
        epic1.rmvAllSubtasks();

        ArrayList<Integer> list = new ArrayList<>();
        assertEquals(list, epic1.getSubtasks());
    }
}