import managers.*;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        LocalDateTime localDateTime = LocalDateTime.of(2024, 5, 25, 23, 30);
        Duration duration = Duration.ofMinutes(25);
        Task task1 = new Task("Покормить кота", "Насыпать корм в миску", Status.NEW, duration,
                localDateTime);
        final int idTask1 = task1.getId();

        Task task2 = new Task("Отправить отчет по лабораторной работе",
                "Написать выводы в работе и отправить ее на проверку",
                Status.NEW, duration, localDateTime);
        final int idTask2 = task2.getId();

        Epic epic1 = new Epic("Разработка мобильного приложения для планирования задач.",
                "Разработка мобильного приложения для планирования задач.");

        final int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("Создание пользовательского интерфейса (UI) для приложения.",
                "Создание пользовательского интерфейса (UI) для приложения.",
                Status.NEW, idEpic1, duration, localDateTime);
        final int idSubtask1 = subtask1.getId();
        Subtask subtask2 = new Subtask(" Реализация функции уведомлений и напоминаний о задачах.",
                " Реализация функции уведомлений и напоминаний о задачах.", Status.NEW, idEpic1, duration,
                LocalDateTime.now());
        final int idSubtask2 = subtask2.getId();

        Epic epic2 = new Epic("Подготовка презентации для конференции",
                "Подготовка презентации для конференции.");
        final int idEpic2 = epic2.getId();

        Subtask subtask3 = new Subtask("Сбор материалов и данных для слайдов презентации.",
                "Сбор материалов и данных для слайдов презентации.", Status.NEW, idEpic2, duration,
                localDateTime);
        final int idSubtask3 = subtask3.getId();

        printAllTasks(manager);

        System.out.println(" ");
        System.out.println(" ");

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        System.out.println(manager.getTaskById(idTask1));
        System.out.println(manager.getTaskById(idTask2));
        System.out.println(manager.getEpicById(idEpic1));
        System.out.println(manager.getSubtaskById(idSubtask1));
        System.out.println(manager.getSubtaskById(idSubtask2));
        System.out.println(manager.getEpicById(idEpic2));
        System.out.println(manager.getSubtaskById(idSubtask3));

        task1.setStatus(Status.IN_PROGRESS);
        task1.setId(idTask1);
        manager.updTask(task1);
        task2.setStatus(Status.DONE);
        task2.setId(idTask2);
        manager.updTask(task2);

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask1.setId(idSubtask1);
        manager.updSubtask(subtask1);

        subtask2.setStatus(Status.DONE);
        subtask2.setId(idSubtask2);
        manager.updSubtask(subtask2);

        subtask3.setStatus(Status.DONE);
        subtask3.setId(idSubtask3);
        manager.updSubtask(subtask3);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(manager.getTaskById(idTask1));
        System.out.println(manager.getTaskById(idTask2));
        System.out.println(manager.getEpicById(idEpic1));
        System.out.println(manager.getSubtaskById(idSubtask1));
        System.out.println(manager.getSubtaskById(idSubtask2));
        System.out.println(manager.getEpicById(idEpic2));
        System.out.println(manager.getSubtaskById(idSubtask3));

        manager.rmvTaskById(idTask2);
        manager.rmvSubtaskById(idSubtask2);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(manager.getTaskById(idTask1));
        System.out.println(manager.getTaskById(idTask2));
        System.out.println(manager.getEpicById(idEpic1));
        System.out.println(manager.getSubtaskById(idSubtask1));
        System.out.println(manager.getSubtaskById(idSubtask2));
        System.out.println(manager.getEpicById(idEpic2));
        System.out.println(manager.getSubtaskById(idSubtask3));

        manager.rmvEpicById(idEpic2);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(manager.getTaskById(idTask1));
        System.out.println(manager.getTaskById(idTask2));
        System.out.println(manager.getEpicById(idEpic1));
        System.out.println(manager.getSubtaskById(idSubtask1));
        System.out.println(manager.getSubtaskById(idSubtask2));
        System.out.println(manager.getEpicById(idEpic2));
        System.out.println(manager.getSubtaskById(idSubtask3));
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");

        InMemoryTaskManager manager2 = new InMemoryTaskManager();

        Task testTask1 = new Task("testTask1", "testTask1", Status.NEW, duration, localDateTime);
        Task testTask2 = new Task("testTask2", "testTask2", Status.NEW, duration, localDateTime);
        Epic testEpic1 = new Epic("testEpic1", "testEpic1");
        Epic testEpic2 = new Epic("testEpic2", "testEpic2");
        manager2.addEpic(testEpic2);
        Subtask testSubtask1 = new Subtask("testSubtask1", "testSubtask1", Status.NEW,
                testEpic2.getId(), duration, localDateTime);
        Subtask testSubtask2 = new Subtask("testSubtask2", "testSubtask2", Status.NEW,
                testEpic2.getId(), duration, localDateTime);
        Subtask testSubtask3 = new Subtask("testSubtask3", "testSubtask3", Status.NEW,
                testEpic2.getId(), duration, localDateTime);

        manager2.addTask(testTask1);
        manager2.addTask(testTask2);
        manager2.addEpic(testEpic1);
        manager2.addSubtask(testSubtask1);
        manager2.addSubtask(testSubtask2);
        manager2.addSubtask(testSubtask3);

        manager2.getTaskById(testTask1.getId());
        manager2.getTaskById(testTask2.getId());
        System.out.println(manager2.getHistory());
        System.out.println(manager2.getHistory().size());
        manager2.getEpicById(testEpic1.getId());
        manager2.getEpicById(testEpic2.getId());
        manager2.getSubtaskById(testSubtask1.getId());
        manager2.getTaskById(testTask2.getId());
        manager2.getTaskById(testTask2.getId());
        manager2.getSubtaskById(testSubtask1.getId());
        System.out.println(manager2.getHistory());
        System.out.println(manager2.getHistory().size());
        manager2.getSubtaskById(testSubtask2.getId());
        manager2.getSubtaskById(testSubtask3.getId());
        manager2.rmvTaskById(testTask1.getId());
        System.out.println(manager2.getHistory());
        System.out.println(manager2.getHistory().size());
        manager2.rmvEpicById(testEpic2.getId());
        System.out.println(manager2.getHistory());
        System.out.println(manager2.getHistory().size());
        System.out.println("-----------------------------");
        System.out.println("-----------------------------");

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task newTask1 = new Task("newTask1", "newTask1", Status.NEW, duration, localDateTime);
        Task newTask2 = new Task("newTask2", "newTask2", Status.NEW, duration,
                LocalDateTime.of(2024, 5, 29, 23, 29));
        Epic newEpic = new Epic("newEpic", "newEpic");
        taskManager.addTask(newTask1);
        taskManager.addTask(newTask2);
        taskManager.addEpic(newEpic);
        Subtask newSubtask1 = new Subtask("newSubtask1", "newSubtask1", Status.NEW, newEpic.getId(),
                duration, LocalDateTime.of(2024, 5, 20, 23, 10));
        Subtask newSubtask2 = new Subtask("newSubtask2", "newSubtask2", Status.NEW, newEpic.getId(),
                duration, LocalDateTime.of(2024, 5, 21, 23, 15));
        taskManager.addSubtask(newSubtask1);
        taskManager.addSubtask(newSubtask2);
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        taskManager.rmvSubtaskById(newSubtask1.getId());
        System.out.println(taskManager.getSubtasks());
    }

    private static void printAllTasks(TaskManager manager) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(manager.getTaskById(task.getId()));
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {

            System.out.println(manager.getEpicById(epic.getId()));

            for (Task task : manager.getSubtaskByEpicId(epic.getId())) {
                System.out.println("--> " + manager.getSubtaskById(task.getId()));
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(manager.getSubtaskById(subtask.getId()));
        }

        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("Кол-во записей в истории: ");
        System.out.println(historyManager.getHistory().size());
    }
}