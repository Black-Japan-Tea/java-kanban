package taskTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    Task task1;

    @BeforeEach
    void setUp(){
        task1 = new Task("name", "description", Status.NEW);
    }

    @Test
    void GetterAndSetterForId() {
        task1.setId(1);
        assertEquals(1, task1.getId());
    }

    @Test
    void GetterAndSetterForName() {
        task1.setName("new name");
        assertEquals("new name", task1.getName());
    }

    @Test
    void GetterAndSetterForDescription() {
        task1.setDescription("new description");
        assertEquals("new description", task1.getDescription());
    }

    @Test
    void GetterAndSetterForStatus() {
        task1.setStatus(Status.DONE);
        assertEquals(Status.DONE, task1.getStatus());
    }

    @Test
    public void EqualsAndHashCode() {
        Task task1 = new Task("name", "dsc", Status.NEW);
        Task task2 = new Task("name", "dsc", Status.NEW);
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());

        task2.setId(2);
        assertNotEquals(task1, task2);
    }
}
