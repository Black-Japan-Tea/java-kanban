package managerTests;

import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @BeforeEach
    void fillTasks() {
        Task task1 = new Task("name", "dsc", Status.NEW);
        task1.setId(1);
        Task task2 = new Task("name", "dsc", Status.NEW);
        task2.setId(2);
        Task task3 = new Task("name", "dsc", Status.NEW);
        task3.setId(3);
        Task task4 = new Task("name", "dsc", Status.NEW);
        task4.setId(4);
        Task task5 = new Task("name", "dsc", Status.NEW);
        task5.setId(5);
        Task task6 = new Task("name", "dsc", Status.NEW);
        task6.setId(6);
        Epic epic1 = new Epic("name", "dsc");
        epic1.setId(7);
        Epic epic2 = new Epic("name", "dsc");
        epic2.setId(8);
        Subtask subtask1 = new Subtask("name", "dsc", Status.NEW, 111);
        subtask1.setId(9);
        Subtask subtask2 = new Subtask("name", "dsc", Status.NEW, 111);
        subtask2.setId(10);
        Subtask subtask3 = new Subtask("name", "dsc", Status.NEW, 111);
        subtask3.setId(11);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        historyManager.add(subtask3);
    }

    @Test
    void shouldAddTaskAndGetHistory() {
        historyManager.add(new Task("name", "dsc", Status.NEW));
        assertEquals(12, historyManager.getHistory().size());
        assertEquals("name", historyManager.getHistory().getFirst().getName());
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task1 = new Task("name", "dsc", Status.NEW);
        task1.setId(1);
        Task task2 = new Task("name", "dsc", Status.NEW);
        task2.setId(2);
        Task task3 = new Task("name", "dsc", Status.NEW);
        task3.setId(3);
        Task task4 = new Task("name", "dsc", Status.NEW);
        task4.setId(4);
        Task task5 = new Task("name", "dsc", Status.NEW);
        task5.setId(5);
        Task task6 = new Task("name", "dsc", Status.NEW);
        task6.setId(6);
        Epic epic1 = new Epic("name", "dsc");
        epic1.setId(7);
        Epic epic2 = new Epic("name", "dsc");
        epic2.setId(8);
        Subtask subtask1 = new Subtask("name", "dsc", Status.NEW, 999);
        subtask1.setId(9);
        Subtask subtask2 = new Subtask("name", "dsc", Status.NEW, 999);
        subtask2.setId(10);
        Subtask subtask3 = new Subtask("name", "dsc", Status.NEW, 999);
        subtask3.setId(11);

        ArrayList<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);
        list.add(task3);
        list.add(task4);
        list.add(task5);
        list.add(task6);
        list.add(epic1);
        list.add(epic2);
        list.add(subtask1);
        list.add(subtask2);
        list.add(subtask3);

        list.remove(0);
        historyManager.rmv(1);
        list.remove(5);
        historyManager.rmv(7);
        list.remove(8);
        historyManager.rmv(11);

        assertEquals(list, historyManager.getHistory());
    }
}