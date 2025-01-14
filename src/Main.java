import managers.*;
import tasks.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("name1", "dsc1", Status.NEW);
        Task task2 = new Task("name2", "dsc2", Status.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Integer task1Id = task1.getId();
        Integer task2Id = task2.getId();

        Epic epic1 = new Epic("epic1", "dsc1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("sbt1", "dsc1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("sbt2", "dsc2", Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        Integer subtask1Id = subtask1.getId();
        Integer epic1Id = epic1.getId();

        Epic epic2 = new Epic("epic2", "dsc1");
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("sbt3", "dsc3", Status.NEW, epic2.getId());
        taskManager.addSubtask(subtask3);
        Integer subtask3Id = subtask3.getId();
        Integer epic2Id = epic2.getId();

        Epic epicToUpdate1 = new Epic("updEpic2", epic2.getDescription());
        epicToUpdate1.setId(epic2.getId());
        for (Integer subId : epic2.getSubtasks()
        ) {
            epicToUpdate1.addSubtask(taskManager.getSubtaskById(subId).getId());
        }
        System.out.println("Обновление эпика с id = " + epicToUpdate1.getId() + " Результат:"
                + taskManager.updEpic(epicToUpdate1));

        printTasks(taskManager);

        Task oldTask = taskManager.getTaskById(task1Id);
        Task taskToUpdate1 = new Task(oldTask.getName(),
                oldTask.getDescription(),
                Status.IN_PROGRESS);
        taskToUpdate1.setId(taskManager.getTaskById(task1Id).getId());
        System.out.println("Обновление задачи с id = " + taskToUpdate1.getId() + " Результат:"
                + taskManager.updTask(taskToUpdate1));


        Subtask oldSubtask1 = taskManager.getSubtaskById(subtask1Id);
        Subtask subtaskToUpdate2 = new Subtask(oldSubtask1.getName(),
                oldSubtask1.getDescription(),
                Status.DONE,
                oldSubtask1.getEpicId());
        subtaskToUpdate2.setId(oldSubtask1.getId());
        subtaskToUpdate2.setStatus(Status.DONE);
        System.out.println("Обновление подзадачи с id = " + subtaskToUpdate2.getId() + " Результат:"
                + taskManager.updSubtask(subtaskToUpdate2));
        taskManager.updSubtask(subtaskToUpdate2);

        Subtask oldSubtask2 = taskManager.getSubtaskById(subtask3Id);
        Subtask subtaskToUpdate3 = new Subtask(oldSubtask2.getName(),
                oldSubtask2.getDescription(),
                Status.DONE,
                oldSubtask2.getEpicId());
        subtaskToUpdate3.setId(oldSubtask2.getId());
        System.out.println("Обновление подзадачи с id = " + subtaskToUpdate3.getId() + " Результат:"
                + taskManager.updSubtask(subtaskToUpdate3));

        printTasks(taskManager);

        System.out.println("История:");
        System.out.println(historyManager.getHistory());

        System.out.println("Удаление подзадачи с id = " + subtask3Id + " Результат:" + taskManager.rmvSubtaskById(subtask3Id));
        System.out.println("Удаление задачи с id = " + task2Id + " Результат:" + taskManager.rmvTaskById(task2Id));
        System.out.println("Удаление эпика с id = " + epic1Id + " Результат:" + taskManager.rmvEpicById(epic1Id));

        printTasks(taskManager);

        System.out.println("Удаление эпика с id = " + epic2Id + " Результат:" + taskManager.rmvTaskById(epic2Id));

        taskManager.rmvAllEpics();
        System.out.println("Удаление всех эпиков");
        printTasks(taskManager);

        System.out.println("Удаление всех типов задач");
        taskManager.rmvAllTypesOfTasks();

        printTasks(taskManager);
    }

    private static void printTasks(TaskManager taskManager) {

        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }

        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
            for (Subtask subtask : taskManager.getSubtaskByEpicId(epic.getId())) {
                System.out.println(subtask);
            }
        }
    }
}