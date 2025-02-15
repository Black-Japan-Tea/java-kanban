package task_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Epic epic1;
    Duration duration = Duration.ofMinutes(10);
    LocalDateTime startTime = LocalDateTime.of(2000, 1, 1, 0, 1);

    @BeforeEach
    void setUp() {
        epic1 = new Epic("name", "description");
    }

    @Test
    void shouldRemoveSubtask() {
        Subtask subtask = new Subtask("name", "dsc", Status.NEW, epic1.getId(),
                duration, startTime.plusMinutes(20));
        epic1.addSubtask(subtask);
        epic1.rmvSubtaskById(subtask.getId());

        ArrayList<Integer> list = new ArrayList<>();
        assertEquals(list, epic1.getSubtasks());

    }

    @Test
    void shouldRemoveAllSubtasks() {
        Subtask subtask = new Subtask("name", "dsc", Status.NEW, epic1.getId(),
                duration, startTime.plusMinutes(30));
        epic1.addSubtask(subtask);
        epic1.rmvAllSubtasks();

        ArrayList<Integer> list = new ArrayList<>();
        assertEquals(list, epic1.getSubtasks());
    }
}