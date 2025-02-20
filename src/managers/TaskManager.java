package managers;

import tasks.*;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    int addTask(Task task);

    int addEpic(Epic epic);

    int addSubtask(Subtask subtask);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    void refreshEpicStatus(Epic epic);

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    void rmvAllTasks();

    void rmvAllSubtasks();

    void rmvAllEpics();

    void updTask(Task newTask);

    void updEpic(Epic newEpic);

    void updSubtask(Subtask newSubtask);

    void rmvTaskById(int id);

    void rmvEpicById(int id);

    void rmvSubtaskById(int id);

    List<Subtask> getSubtaskByEpicId(Epic epic);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();
}
