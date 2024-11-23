public class Main {
    static TaskManager taskManager;

    public static void main(String[] args) {
        taskManager = new TaskManager();

        taskManager.addTask(new Task("name1", "dsc1", Status.NEW));
        taskManager.addTask(new Task("name2", "dsc2", Status.NEW));
        System.out.println("Tasks:");
        for (Task task : taskManager.getTasksList()) {
            System.out.println(task);
        }

        taskManager.addEpic(new Epic("epic1", "epic dsc1"));
        taskManager.addEpic(new Epic("epic2", "epic dsc2"));
        System.out.println("Epics:");
        for (Task epic : taskManager.getEpicsList()) {
            System.out.println(epic);
        }

        taskManager.addSubtask(new Subtask("sbt1", "sbt dsc1", Status.NEW,  1));
        taskManager.addSubtask(new Subtask("sbt2", "sbt dsc2", Status.NEW, 1));
        taskManager.addSubtask(new Subtask("sbt3", "sbt dsc3", Status.NEW, 3));
        System.out.println("Epic's subtasks ID=1");
        for (Subtask subtask : taskManager.getSubtasksListByEpicId(1)) {
            System.out.println(subtask);
        }
        System.out.println("Epic's subtasks ID=3");
        for (Subtask subtask : taskManager.getSubtasksListByEpicId(3)) {
            System.out.println(subtask);
        }

        taskManager.updTask(new Task("new_name1", "new_dsc1", Status.IN_PROGRESS));
        taskManager.updTask(new Task("new_name2", "new_dsc2", Status.DONE));

        taskManager.updSubtask(new Subtask("new_sbt1", "sbt_dsc1", Status.IN_PROGRESS, 1));
        taskManager.updSubtask(new Subtask("new_sbt2", "sbt_dsc2", Status.IN_PROGRESS, 1));
        taskManager.updSubtask(new Subtask("new_sbt3", "sbt_dsc3", Status.DONE, 3));

        System.out.println("Updated Tasks:");
        for (Task task : taskManager.getTasksList()) {
            System.out.println(task);
        }
        System.out.println("Updated Epics:");
        for (Task epic : taskManager.getEpicsList()) {
            System.out.println(epic);
        }
        System.out.println("Updated Subtasks");
        for (Task subtask : taskManager.getSubtasksList()) {
            System.out.println(subtask);
        }

        taskManager.rmvById(1);
        System.out.println("Updated Tasks:");
        for (Task task : taskManager.getTasksList()) {
            System.out.println(task);
        }
        System.out.println("Updated Epics:");
        for (Task epic : taskManager.getEpicsList()) {
            System.out.println(epic);
        }
        System.out.println("Updated Subtasks");
        for (Task subtask : taskManager.getSubtasksList()) {
            System.out.println(subtask);
        }
    }
}