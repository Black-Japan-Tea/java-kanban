package manager_tests;

import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldGetDefault() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Assertions.assertEquals(inMemoryTaskManager.getClass(), Managers.getDefault().getClass());
    }

    @Test
    void shouldGetHistory() {
        assertEquals(inMemoryHistoryManager.getClass(), Managers.getDefaultHistory().getClass());
    }
}