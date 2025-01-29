package taskTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import tasks.Status;
import tasks.Subtask;

class SubtaskTest {
    Subtask subtask;

    @BeforeEach
    void setUp() {
        subtask = new Subtask("name", "description", Status.NEW, 1);
    }

    @Test
    void shouldGetEpicId() {
        Assertions.assertEquals(1, subtask.getEpicId());
    }
}