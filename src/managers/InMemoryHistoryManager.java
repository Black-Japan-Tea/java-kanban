package managers;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> historyList = new ArrayList<>();
    int maxSize = 10;

    @Override
    public ArrayList<Task> getHistory() {
        return historyList;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            historyList.add(task);
        } else {
            System.out.println("Ошибка добавления задачи в историю");
        }
        if (historyList.size() == maxSize) {
            historyList.removeFirst();
        }
        historyList.addFirst(task);
    }
}
