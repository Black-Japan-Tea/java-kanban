package managers;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import exceptions.*;
import tasks.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;
    private ArrayList<Subtask> loadedSubtasks = new ArrayList<>();

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
        loadFromFile(this.file);
    }

    private void save() {
        List<String> strToSave = new ArrayList<>();

        if (this.file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        strToSave.add("id,type,name,status,description,epic,duration,startTime\n");

        for (Task task : super.getTasks()) {
            strToSave.add(taskToString(task, Type.TASK) + "\n");
        }

        for (Epic epic : super.getEpics()) {
            strToSave.add(taskToString(epic, Type.EPIC) + "\n");
        }

        for (Subtask subtask : super.getSubtasks()) {
            strToSave.add(subtaskToString(subtask) + "\n");
        }

        try (Writer writer = new FileWriter(this.file, StandardCharsets.UTF_8, false)) {
            for (String str : strToSave) {
                writer.write(str);
            }
        } catch (IOException e) {
            throw new FileSaveException("Ошибка записи в " + file.getName());
        }
    }

    private String taskToString(Task task, Type type) {
        StringBuilder sb = new StringBuilder();

        sb.append(task.getId() + "," + type.toString() + "," +
                task.getName() + "," + task.getStatus().toString() + "," +
                task.getDescription() + ",");

        return sb.toString();
    }

    private String subtaskToString(Subtask subtask) {
        StringBuilder sb = new StringBuilder(taskToString(subtask, Type.SUBTASK));

        sb.append(subtask.getEpicId());

        return sb.toString();
    }

    private void loadFromFile(File file) {

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String line = br.readLine();
                if (!fromString(line)) {
                    throw new FileLoadException("Ошибка загрузки " + file.getName());
                }
            }
        } catch (IOException | FileLoadException e) {
            System.out.println("Во время загрузки " + file.getName() + " произошла ошибка");
        }

    }

    private Boolean fromString(String str) {

        String[] items = str.split(",");

        int id = Integer.parseInt(items[0]);
        Type type = Type.valueOf(items[1]);
        String name = items[2];
        Status status = Status.valueOf(items[3]);
        String description = items[4];
        Integer subtasksEpic = null;
        Duration duration = Duration.ofMinutes(Integer.parseInt(items[6]));
        LocalDateTime startTime = LocalDateTime.parse(items[7]);
        if (items.length > 7) {
            subtasksEpic = Integer.valueOf(items[5]);
        }
        if (id > super.getLastId()) {
            super.setLastId(id);
        }

        switch (type) {
            case TASK: {
                Task task = new Task(name, description, status, duration, startTime);
                task.setId(id);
                super.loadTask(task);
                return true;
            }
            case SUBTASK: {
                Subtask subtask = new Subtask(name, description, status, subtasksEpic, duration, startTime);
                subtask.setId(id);
                super.loadSubtask(subtask);
                loadedSubtasks.add(subtask);
                return true;
            }
            case EPIC: {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                super.loadEpic(epic);
                return true;
            }
        }
        return false;
    }

    @Override
    public void rmvAllTypesOfTasks() {
        super.rmvAllTypesOfTasks();
        save();
    }

    @Override
    public void rmvAllTasks() {
        super.rmvAllTasks();
        save();
    }

    @Override
    public void rmvAllEpics() {
        super.rmvAllEpics();
        save();
    }

    @Override
    public void rmvAllSubtasks() {
        super.rmvAllSubtasks();
        save();
    }

    @Override
    public int addTask(Task newTask) {
        int r = super.addTask(newTask);
        save();
        return r;
    }

    @Override
    public int addEpic(Epic newEpic) {
        int r = super.addEpic(newEpic);
        save();
        return r;
    }

    @Override
    public int addSubtask(Subtask newSubtask) {
        int r = super.addSubtask(newSubtask);
        save();
        return r;
    }

    @Override
    public boolean updTask(Task task) {
        boolean result = super.updTask(task);
        save();
        return result;
    }

    @Override
    public boolean updEpic(Epic epic) {
        boolean result = super.updEpic(epic);
        save();
        return result;
    }

    @Override
    public boolean updSubtask(Subtask subtask) {
        boolean result = super.updSubtask(subtask);
        save();
        return result;
    }

    @Override
    public boolean rmvTaskById(Integer id) {
        boolean result = super.rmvTaskById(id);
        save();
        return result;
    }

    @Override
    public boolean rmvEpicById(Integer id) {
        boolean result = super.rmvEpicById(id);
        save();
        return result;
    }

    @Override
    public boolean rmvSubtaskById(Integer id) {
        boolean result = super.rmvSubtaskById(id);
        save();
        return result;
    }
}
