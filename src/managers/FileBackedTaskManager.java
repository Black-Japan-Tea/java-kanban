package managers;

import java.io.*;
import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDateTime;

import exceptions.*;
import tasks.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;
    private ArrayList<Subtask> loadedSubtasks = new ArrayList<>();

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public int addTask(Task task) {
        super.addTask(task);
        save();
        return super.addTask(task);
    }

    @Override
    public int addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
        return super.addSubtask(subtask);
    }

    @Override
    public void refreshEpicStatus(Epic epic) {
        super.refreshEpicStatus(epic);
        save();
    }

    @Override
    public void rmvAllTasks() {
        super.rmvAllTasks();
        save();
    }

    @Override
    public void rmvAllSubtasks() {
        super.rmvAllSubtasks();
        save();
    }

    @Override
    public void rmvAllEpics() {
        super.rmvAllEpics();
        save();
    }

    @Override
    public void updTask(Task newTask) {
        super.updTask(newTask);
        save();
    }

    @Override
    public void updEpic(Epic newEpic) {
        super.updEpic(newEpic);
        save();
    }

    @Override
    public void updSubtask(Subtask newSubtask) {
        super.updSubtask(newSubtask);
        save();
    }

    @Override
    public void rmvTaskById(int id) {
        super.rmvTaskById(id);
        save();
    }

    @Override
    public void rmvEpicById(int id) {
        super.rmvEpicById(id);
        save();
    }

    @Override
    public void rmvSubtaskById(int id) {
        super.rmvSubtaskById(id);
        save();
    }

    @Override
    public void updEpicsStartTimeAndDuration(Epic epic) {
        super.updEpicsStartTimeAndDuration(epic);
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        int maxId = 0;

        try (BufferedReader readerToFile = new BufferedReader(new FileReader(file))) {
            readerToFile.readLine();
            String line;
            while ((line = readerToFile.readLine()) != null) {
                Task task = fileBackedTaskManager.fromString(line);
                if (task instanceof Subtask subtask) {
                    fileBackedTaskManager.loadSubtask(subtask);
                } else if (task instanceof Epic epic) {
                    fileBackedTaskManager.loadEpic(epic);
                } else {
                    fileBackedTaskManager.loadTask(task);
                }
                assert task != null;
                maxId = Math.max(maxId, task.getId());
            }
        } catch (IOException e) {
            throw new FileSaveException("Ошибка при чтении файла");
        }

        fileBackedTaskManager.setNextTaskId(maxId + 1);
        return fileBackedTaskManager;
    }

    private String toString(Task task) {
        if (task instanceof Subtask) {
            return task.getId() + "," + Type.SUBTASK + "," + task.getName() + "," + task.getStatus()
                    + "," + task.getDescription() + "," + ((Subtask) task).getEpicId() + ","
                    + task.getDuration().toMinutes() + "," + task.getStartTime() + "," + task.getEndTime();
        } else if (task instanceof Epic) {
            return task.getId() + "," + Type.EPIC + "," + task.getName() + "," + task.getStatus()
                    + "," + task.getDescription() + "," + task.getDuration()
                    + "," + task.getStartTime() + "," + task.getEndTime();
        } else {
            return task.getId() + "," + Type.TASK + "," + task.getName() + "," + task.getStatus()
                    + "," + task.getDescription() + "," + task.getDuration().toMinutes()
                    + "," + task.getStartTime() + "," + task.getEndTime();
        }
    }

    private void save() {
        try (BufferedWriter writerToFile = new BufferedWriter(new FileWriter(file))) {
            writerToFile.write("id,type,name,status,description,epic,duration,startTime,endTime");
            writerToFile.newLine();
            for (Task task : getTasks()) {
                writerToFile.write(toString(task));
                writerToFile.newLine();
            }
            for (Epic epic : getEpics()) {
                writerToFile.write(toString(epic));
                writerToFile.newLine();
            }
            for (Subtask subtask : getSubtasks()) {
                writerToFile.write(toString(subtask));
                writerToFile.newLine();
            }
        } catch (IOException exception) {
            throw new FileSaveException("Ошибка при сохранении в файл");
        }
    }

    private Task fromString(String str) {

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
                return task;
            }
            case SUBTASK: {
                Subtask subtask = new Subtask(name, description, status, subtasksEpic, duration, startTime);
                subtask.setId(id);
                super.loadSubtask(subtask);
                loadedSubtasks.add(subtask);
                return subtask;
            }
            case EPIC: {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                super.loadEpic(epic);
                return epic;
            }
        }
        return null;
    }
}
