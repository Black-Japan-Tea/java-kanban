package managers;

import tasks.*;

import java.util.List;
import java.util.Optional;

public interface TaskManager {

    List<Task> getTasks();

    Optional<Task> getTaskById(Integer id);

    int addTask(Task newTask);

    boolean updTask(Task task);

    boolean rmvTaskById(Integer id);

    void rmvAllTasks();

    List<Subtask> getSubtasks();

    Optional<Subtask> getSubtaskById(Integer id);

    int addSubtask(Subtask newSubtask);

    void rmvAllSubtasks();

    boolean updSubtask(Subtask subtask);

    boolean rmvSubtaskById(Integer id);

    List<Subtask> getSubtaskByEpicId(int epicId);

    List<Epic> getEpics();

    void rmvAllTypesOfTasks();

    void rmvAllEpics();

    Optional<Epic> getEpicById(Integer id);

    int addEpic(Epic newEpic);

    boolean updEpic(Epic epic);

    boolean rmvEpicById(Integer id);

    List getHistory();

    List<Task> getPrioritizedTasks();
}
