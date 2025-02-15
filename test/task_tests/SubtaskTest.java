package task_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {
    Subtask subtask;
    private static final Duration duration = Duration.ofMinutes(25);
    private static final LocalDateTime startTime = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        subtask = new Subtask("name", "dsc", Status.NEW, 1, duration, startTime);
    }

    @Test
    void shouldGetEpicId() {
        Assertions.assertEquals(1, subtask.getEpicId());
    }

    @Test
    public void shouldBeEqualToEachOtherIfTheirIdsEqual() {
        Epic epic1 = new Epic("name1", "dsc1");
        Subtask subtask1 = new Subtask("Subtask1", "DSubtask1", Status.NEW, epic1.getId(),
                duration, startTime);
        Subtask subtask2 = new Subtask("Subtask2", "DSubtask2", Status.NEW, epic1.getId(),
                duration, startTime);
        subtask1.setId(1);
        subtask2.setId(1);

        Assertions.assertEquals(subtask1, subtask2);
    }
}