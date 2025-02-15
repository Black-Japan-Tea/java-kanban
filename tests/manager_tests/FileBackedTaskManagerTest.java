package manager_tests;

import managers.*;
import tasks.*;
import exceptions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Test
    void emptyFileTest() throws IOException {
        File file = File.createTempFile("tasks", null);
        TaskManager taskManager = new FileBackedTaskManager(file);
        List<tasks.Task> list = new ArrayList<>();
        List<Task> listFromFile = taskManager.getTasks();

        assertEquals(list, listFromFile);
    }

    @Test
    public void exceptionTest() throws IOException {

        File file = File.createTempFile("tasks", null);

        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8, false)) {

            fileWriter.write("id,type,name,status,description,epic"+ "\n");
            fileWriter.write("2,TASK,Задача #2,INVALID,TaSk 222,2025-02-14T18:06:55.641439400,15,"+ "\n");
        }

        try {
            assertThrows(FileLoadException.class, () -> {
                TaskManager taskManager = new FileBackedTaskManager(file);
            }, "Загрузка из файла " + file.getName() + " не удалась");
        } finally {
            file.delete();
        }
    }

}