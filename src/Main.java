import managers.FileBackedTaskManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static managers.Managers.getDefault;

public class Main {

    public static void main(String[] args) {
                createTask();
                addTaskToFile();
                loadTaskFromFile();
    }

    private static void loadTaskFromFile() {
        File file = new File("src/resources/autoSave.csv");

        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);

        System.out.println(fileBackedTaskManager.getTasks());
        System.out.println(fileBackedTaskManager.getEpics());
        System.out.println(fileBackedTaskManager.getSubtasks());

        Task task6 = new Task("task_name", "task_dsc", Status.NEW,
                Duration.ofMinutes(50), LocalDateTime.of(2025, 2, 4, 15, 0));
        System.out.println("New task ID: " + fileBackedTaskManager.addTask(task6));
    }

    private static void addTaskToFile() {
        File file = new File("src/resources/autoSave.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        Task task1 = new Task("TASK1", "asd", Status.NEW, Duration.ofMinutes(50),
                LocalDateTime.of(2025, 2, 1, 15, 0));
        Task task2 = new Task("TASK2", "asd", Status.IN_PROGRESS, Duration.ofMinutes(33),
                LocalDateTime.of(2025, 2, 2, 10, 55));
        Epic epic1 = new Epic("EPIC1", "asd");
        Subtask subtask1 = new Subtask("SUBTASK1", "ads", Status.DONE, 3,
                Duration.ofMinutes(66), LocalDateTime.of(2025, 2, 3, 15, 0));
        Subtask subtask2 = new Subtask("SUBTASK2", "asd", Status.DONE, 3,
                Duration.ofMinutes(99), LocalDateTime.of(2025, 2, 4, 15, 0));
        Epic epic2 = new Epic("EPIC2", "asd");

        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addEpic(epic1);
        fileBackedTaskManager.addEpic(epic2);
        fileBackedTaskManager.addSubtask(subtask1);
        fileBackedTaskManager.addSubtask(subtask2);
    }

    private static void createTask() {
        TaskManager taskManager = getDefault();

        Task task1 = new Task("taskName1", "taskDsc1", Status.NEW, Duration.ofMinutes(50),
                LocalDateTime.of(2025, 2, 10, 15, 0));
        Task task2 = new Task("taskName2", "taskDsc2", Status.NEW,
                Duration.ofMinutes(50), LocalDateTime.of(2025, 2, 2, 15, 0));
        Task task3 = new Task("taskName3", "taskDsc3", Status.NEW, Duration.ofMinutes(50),
                LocalDateTime.of(2025, 2, 2, 15, 0));
        Task task4 = new Task("taskName4", "taskDsc4", Status.NEW,
                Duration.ofMinutes(50), LocalDateTime.of(2025, 2, 4, 15, 0));
        Task task5 = new Task("taskName5", "taskDsc5", Status.NEW, Duration.ofMinutes(50),
                LocalDateTime.of(2025, 2, 4, 16, 0));
        Task task6 = new Task("taskName6", "taskDsc6", Status.NEW,
                Duration.ofMinutes(50), LocalDateTime.of(2025, 2, 4, 17, 0));
        Epic epic1 = new Epic("epicName1", "epicDsc1");
        Epic epic2 = new Epic("epicName2", "epicDsc2");
        Epic epic3 = new Epic("epicName3", "epicDsc3");

        Subtask subtask11 = new Subtask("subName1", "subDsc1", Status.DONE,
                7, Duration.ofMinutes(50), LocalDateTime.of(2025, 2, 4, 21, 0));
        Subtask subtask12 = new Subtask("subName2", "subDsc2", Status.NEW,
                7, Duration.ofMinutes(50), LocalDateTime.of(2025, 2, 4, 19, 0));
        Subtask subtask21 = new Subtask("subName3", "subDsc3", Status.DONE,
                8, Duration.ofMinutes(50), LocalDateTime.of(2025, 2, 4, 20, 0));

        System.out.println("Tasks:");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        taskManager.addTask(task5);
        taskManager.addTask(task6);

        System.out.println("\nEpics:");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);


        System.out.println("\nSubtasks:");
        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addSubtask(subtask21);

        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Tasks:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Epics:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtaskByEpicId(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Subtasks:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("History:");
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(3);
        manager.getTaskById(4);
        manager.getTaskById(5);
        manager.getTaskById(6);
        manager.getEpicById(7);
        manager.getEpicById(8);
        manager.getEpicById(9);
        manager.getSubtaskById(10);
        manager.getSubtaskById(11);
        manager.getSubtaskById(12);

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("-------");
        manager.rmvTaskById(2);
        manager.rmvEpicById(7);
        manager.rmvAllEpics();
        System.out.println("-------");

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("Sorted tasks:");
        for (Task task : manager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}