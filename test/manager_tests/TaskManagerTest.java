package manager_tests;

import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    Duration duration = Duration.ofMinutes(10);
    LocalDateTime startTime = LocalDateTime.now();
    InMemoryTaskManager taskManager;


    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();

        Task task1 = new Task("title", "description", Status.NEW, duration, startTime);
        Task task2 = new Task("title", "description", Status.NEW, duration, startTime.plusMinutes(15));
        Task task3 = new Task("title", "description", Status.NEW, duration, startTime.plusMinutes(30));

        Epic epic1 = new Epic("title", "description");
        Epic epic2 = new Epic("title", "description");

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subTask1 = new Subtask("title", "description", Status.NEW, epic1.getId(),
                duration, startTime.plusMinutes(45));
        Subtask subTask2 = new Subtask("title", "description", Status.NEW, epic1.getId(),
                duration, startTime.plusMinutes(60));

        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
    }

    @Test
    void epicNewStatusTest() {

        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        taskManager.addEpic(epic1);

        Subtask subTask1 = new Subtask("title", "description", Status.NEW, epic1.getId(),
                duration, startTime.plusMinutes(75));
        subTask1.setId(6);
        Subtask subTask2 = new Subtask("title", "description", Status.NEW, epic1.getId(),
                duration, startTime.plusMinutes(90));
        subTask2.setId(7);


        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);

        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void epicDoneStatusTest() {

        Epic epic1 = new Epic("title", "description");
        epic1.setId(412);
        taskManager.addEpic(epic1);

        Subtask subTask1 = new Subtask("title", "description", Status.DONE, epic1.getId(),
                duration, startTime.plusMinutes(105));
        subTask1.setId(6);
        Subtask subTask2 = new Subtask("title", "description", Status.DONE, epic1.getId(),
                duration, startTime.plusMinutes(120));
        subTask2.setId(7);


        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);

        assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    void epicNewAndDoneStatusTest() {

        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        taskManager.addEpic(epic1);

        Subtask subTask1 = new Subtask("title", "description", Status.NEW, epic1.getId(),
                duration, startTime.plusMinutes(135));
        subTask1.setId(6);
        Subtask subTask2 = new Subtask("title", "description", Status.DONE, epic1.getId(),
                duration, startTime.plusMinutes(150));
        subTask2.setId(7);


        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    void epicInProgressStatusTest() {

        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        taskManager.addEpic(epic1);

        Subtask subTask1 = new Subtask("title", "description", Status.IN_PROGRESS, epic1.getId(),
                duration, startTime.plusMinutes(165));
        subTask1.setId(6);
        Subtask subTask2 = new Subtask("title", "description", Status.IN_PROGRESS, epic1.getId(),
                duration, startTime.plusMinutes(180));
        subTask2.setId(7);


        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }


    @Test
    void getTasks() {
        Task task1 = new Task("title", "description", Status.NEW, duration, startTime);
        task1.setId(1);
        Task task2 = new Task("title", "description", Status.NEW, duration, startTime.plusMinutes(20));
        task2.setId(2);
        Task task3 = new Task("title", "description", Status.NEW, duration, startTime.plusMinutes(40));
        task3.setId(3);

        ArrayList<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);
        list.add(task3);

        assertEquals(list, taskManager.getTasks());
    }

    @Test
    void getEpics() {
        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        Epic epic2 = new Epic("title", "description");
        epic2.setId(5);

        ArrayList<Epic> list = new ArrayList<>();
        list.add(epic1);
        list.add(epic2);

        assertEquals(list, taskManager.getEpics());
    }

    @Test
    void getSubtasks() {
        Subtask subTask1 = new Subtask("title", "description", Status.NEW, 4,
                duration, startTime);
        subTask1.setId(6);
        Subtask subTask2 = new Subtask("title", "description", Status.NEW, 4,
                duration, startTime);
        subTask2.setId(7);

        ArrayList<Subtask> list = new ArrayList<>();
        list.add(subTask1);
        list.add(subTask2);

        assertEquals(list, taskManager.getSubtasks());
    }

    @Test
    void rmvAllTypesOfTasks() {
        taskManager.rmvAllTypesOfTasks();

        ArrayList<Task> list1 = new ArrayList<>();
        ArrayList<Epic> list2 = new ArrayList<>();
        ArrayList<Subtask> list3 = new ArrayList<>();

        assertEquals(list1, taskManager.getTasks());
        assertEquals(list2, taskManager.getEpics());
        assertEquals(list3, taskManager.getSubtasks());
    }

    @Test
    void rmvAllTasks() {
        ArrayList<Task> list = new ArrayList<>();
        taskManager.rmvAllTasks();
        assertEquals(list, taskManager.getTasks());
    }

    @Test
    void rmvAllEpics() {
        ArrayList<Epic> list = new ArrayList<>();
        taskManager.rmvAllEpics();
        assertEquals(list, taskManager.getEpics());
    }

    @Test
    void removeAllSubTasks() {
        taskManager.rmvAllSubtasks();
        assertNotNull(taskManager.getSubtasks());
    }

    @Test
    void getTaskById() {
        Task task1 = new Task("title", "description", Status.NEW, duration, startTime);
        task1.setId(1);
        assertEquals(task1, taskManager.getTaskById(1).get());
    }

    @Test
    void getSubtaskById() {
        Subtask subTask1 = new Subtask("title", "description", Status.NEW, 4,
                duration, startTime);
        subTask1.setId(6);
        assertEquals(subTask1, taskManager.getSubtaskById(6).get());
    }

    @Test
    void getEpicById() {
        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        assertEquals(epic1, taskManager.getEpicById(4).get());
    }


    @Test
    void updTask() {
        Task task1 = new Task("new title", "new  description", Status.DONE, duration, startTime);
        task1.setId(1);

        taskManager.updTask(task1);
        assertEquals(task1, taskManager.getTaskById(1).get());
    }

    @Test
    void updEpic() {
        Epic epic1 = new Epic("new title", "new  description");
        epic1.setId(4);
        taskManager.updEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(4).get());
    }

    @Test
    void updSubtask() {
        Subtask subTask1 = new Subtask("new title", " new description", Status.DONE, 4,
                duration, startTime);
        subTask1.setId(6);
        taskManager.updSubtask(subTask1);
        assertEquals(subTask1, taskManager.getSubtaskById(6).get());

    }

    @Test
    void rmvTaskById() {
        Task task1 = new Task("title", "description", Status.NEW, duration, startTime);
        task1.setId(1);
        Task task2 = new Task("title", "description", Status.NEW, duration, startTime);
        task2.setId(2);

        ArrayList<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);

        taskManager.rmvTaskById(3);

        assertEquals(list, taskManager.getTasks());

    }

    @Test
    void rmvEpicById() {
        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);

        ArrayList<Epic> list = new ArrayList<>();
        list.add(epic1);

        taskManager.rmvEpicById(5);

        assertEquals(list, taskManager.getEpics());
    }

    @Test
    void rmvSubtaskById() {
        Subtask subTask1 = new Subtask("title", "description", Status.NEW, 4,
                duration, startTime);
        subTask1.setId(6);

        ArrayList<Subtask> list = new ArrayList<>();
        list.add(subTask1);

        taskManager.rmvSubtaskById(7);

        assertEquals(list, taskManager.getSubtasks());
    }

    @Test
    void getSubtaskByEpic() {
        Subtask subTask1 = new Subtask("title", "description", Status.NEW, 4,
                duration, startTime);
        subTask1.setId(6);
        Subtask subTask2 = new Subtask("title", "description", Status.NEW, 4,
                duration, startTime);
        subTask2.setId(7);

        ArrayList<Subtask> list = new ArrayList<>();
        list.add(subTask1);
        list.add(subTask2);

        assertEquals(list, taskManager.getSubtaskByEpicId(4));

    }

    @Test
    void getPrioritizedTasksTest() {
        Task task1 = new Task("title", "description", Status.NEW, duration,
                LocalDateTime.parse("01.01.2025 12:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        Task task2 = new Task("title", "description", Status.NEW, duration.plusMinutes(10),
                LocalDateTime.parse("01.01.2025 12:00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

        taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);

        assertEquals(-1, task2Id);
    }
}