package managers;

import tasks.*;

import java.util.List;

public interface TaskManager {

    List<Task> getTasks();

    Task getTaskById(Integer id);

    void addTask(Task newTask);

    boolean updTask(Task task);

    boolean rmvTaskById(Integer id);

    void rmvAllTasks();

    List<Subtask> getSubtasks();

    Subtask getSubtaskById(Integer id);

    void addSubtask(Subtask newSubtask);

    void rmvAllSubtasks();

    boolean updSubtask(Subtask subtask);

    boolean rmvSubtaskById(Integer id);

    List<Subtask> getSubtaskByEpicId(int epicId);

    List<Epic> getEpics();

    void rmvAllTypesOfTasks();

    void rmvAllEpics();

    Epic getEpicById(Integer id);

    void addEpic(Epic newEpic);

    boolean updEpic(Epic epic);

    boolean rmvEpicById(Integer id);

    List getHistory();
}
