package manager_tests;

import managers.*;
import tasks.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Test
    void getHistory() {

        Duration duration = Duration.ofMinutes(10);
        LocalDateTime startTime = LocalDateTime.now();
        TaskManager taskManager = Managers.getDefault();

        taskManager.rmvAllEpics();
        taskManager.rmvAllTasks();
        taskManager.rmvAllSubtasks();


        ArrayList<Task> list = new ArrayList<>();

        Task task1 = new Task("title", "description", Status.NEW, duration, startTime);
        taskManager.addTask(task1);
        Optional<Task> optionalTask = taskManager.getTaskById(1);
        optionalTask.ifPresent(list::add);


        Task task2 = new Task("title", "description", Status.NEW,
                duration.plusMinutes(10), startTime);
        taskManager.addTask(task2);
        Optional<Task> optionalTask2 = taskManager.getTaskById(2);
        optionalTask2.ifPresent(list::add);


        taskManager.rmvTaskById(1);
        list.remove(0);


        List<Task> history = Managers.getDefault().getHistory();

        assertEquals(list, history);
    }

}