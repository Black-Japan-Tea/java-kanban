package manager_tests;

import managers.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import tasks.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    protected File test = new File("test.cvs");
    protected FileBackedTaskManager fileBackedTaskManager;
    Duration duration = Duration.ofMinutes(10);
    LocalDateTime startTime = LocalDateTime.of(2000, 1, 1, 0, 1);

    @BeforeEach
    void beforeEach() {
        fileBackedTaskManager = new FileBackedTaskManager(test);
    }

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File("test.cvs"));
    }

    @Test
    void shouldSaveTasksToFile() {
        Task task = new Task("Покормить кота", "Насыпать корм в миску", Status.NEW, duration, startTime);
        Epic epic = new Epic("epicName", "epicDsc");

        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDsc1", Status.NEW,
                epic.getId(), duration, startTime.plusMinutes(100));
        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDsc2", Status.NEW,
                epic.getId(), duration, startTime.plusMinutes(200));
        fileBackedTaskManager.addSubtask(subtask1);
        fileBackedTaskManager.addSubtask(subtask2);

        Assertions.assertTrue(test.exists());
        Assertions.assertTrue(test.length() > 0);
    }

    @Test
    void shouldLoadingFromFile() {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(test);

        Assertions.assertTrue(fileBackedTaskManager.getTasks().isEmpty());
        Assertions.assertTrue(fileBackedTaskManager.getEpics().isEmpty());
        Assertions.assertTrue(fileBackedTaskManager.getSubtasks().isEmpty());
    }

    @Test
    void shouldThrowExceptionForNonExistentFile() throws RuntimeException {
        File nonExistFile = new File("testFile.cvs");
        assertThrows(RuntimeException.class, () -> FileBackedTaskManager.loadFromFile(nonExistFile));
    }
}