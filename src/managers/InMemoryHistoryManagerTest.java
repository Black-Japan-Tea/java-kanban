package managers;

import tasks.Task;
import tasks.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void shouldAddTaskAndGetHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        historyManager.add(new Task("name1", "dsc1", Status.NEW));
        assertEquals(1, historyManager.getHistory().size());
        assertEquals("name1", historyManager.getHistory().getFirst().getName()); /* Приветствую! Этот тест не проходит.
                                                                                            Вместо ожидаемого одного элемента
                                                                                            в списке получаю два.
                                                                                            Не понимаю, почему так. */
    }
}