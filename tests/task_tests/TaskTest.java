package task_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    Task task1;
    private final Duration duration = Duration.ofMinutes(25);
    private final LocalDateTime startTime = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        task1 = new Task("name", "description", Status.NEW, duration, startTime);
    }

    @Test
    void getterAndSetterForId() {
        task1.setId(1);
        assertEquals(1, task1.getId());
    }

    @Test
    void getterAndSetterForName() {
        task1.setName("new name");
        assertEquals("new name", task1.getName());
    }

    @Test
    void getterAndSetterForDescription() {
        task1.setDescription("new description");
        assertEquals("new description", task1.getDescription());
    }

    @Test
    void getterAndSetterForStatus() {
        task1.setStatus(Status.DONE);
        assertEquals(Status.DONE, task1.getStatus());
    }

    @Test
    public void equalsAndHashCode() {
        Task task1 = new Task("name", "dsc", Status.NEW);
        Task task2 = new Task("name", "dsc", Status.NEW);
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());

        task2.setId(2);
        assertNotEquals(task1, task2);
    }

    @Test
    public void shouldChecksEqualToEachOtherIfTheirIdEqual() {
        Task task1 = new Task("Test1", "DTest1", Status.NEW, duration, startTime);
        Task task2 = new Task("Test2", "DTest2", Status.NEW, duration, startTime);

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
    }
}
