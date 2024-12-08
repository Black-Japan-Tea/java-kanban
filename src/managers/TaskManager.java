package managers;
import tasks.*;
import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getTasks();
    Task getTaskById(Integer id);
    void addTask(Task newTask);
    boolean updTask(Task task);
    boolean rmvTaskById(Integer id);
    void rmvAllTasks();

    ArrayList<Subtask> getSubtasks();
    Subtask getSubtaskById(Integer id);
    void addSubtask(Subtask newSubtask);
    void rmvAllSubtasks();
    boolean updSubtask(Subtask subtask);
    boolean rmvSubtaskById(Integer id);
    ArrayList<Subtask> getSubtaskByEpicId(int epicId);

    ArrayList<Epic> getEpics();
    void rmvAllTypesOfTasks();
    void rmvAllEpics();
    Epic getEpicById(Integer id);
    void addEpic(Epic newEpic);
    boolean updEpic(Epic epic);
    boolean rmvEpicById(Integer id);
}
