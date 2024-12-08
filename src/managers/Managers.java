package managers;

public class Managers {
    private static InMemoryHistoryManager historyManager= new InMemoryHistoryManager();
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);

    public static TaskManager getDefault(){
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }



}
