package managers;

import tasks.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> historyMap = new HashMap<>();
    private Integer firstId;
    private Integer lastId;

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            System.out.println("Ошибка добавления задачи в историю");
            return;
        }

        int newTaskId = task.getId();

        if (historyMap.containsKey(newTaskId)) {
            rmv(newTaskId);
        }
        Node node = new Node(task, historyMap.get(lastId));
        linkLastNode(node);
    }

    @Override
    public void rmv(int taskIdToRmv) {
        Node nodeToRmv = historyMap.get(taskIdToRmv);

        if (nodeToRmv == null) {
            System.out.println("Ошибка удаления узла: такого узла не существует.");
            return;
        }

        rmvNode(nodeToRmv);
        historyMap.remove(taskIdToRmv);

        if (historyMap.isEmpty()) {
            firstId = null;
            lastId = null;
        }
    }

    private void rmvNode(Node nodeToRmv) {
        Node prevNode = nodeToRmv.getPreviousNode();
        Node nextNode = nodeToRmv.getNextNode();

        if (prevNode != null) {
            prevNode.setNextNode(nextNode);

            if (nodeToRmv.getData().getId() == lastId) {
                lastId = prevNode.getData().getId();
            }

        }
        if (nextNode != null) {
            nextNode.setPreviousNode(prevNode);

            if (nodeToRmv.getData().getId() == firstId) {
                firstId = nextNode.getData().getId();
            }
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = historyMap.get(firstId);

        while (currentNode != null) {
            tasks.add(currentNode.getData());
            currentNode = currentNode.getNextNode();
        }
        return tasks;
    }

    private void linkLastNode(Node newNode) {
        if (lastId != null) {
            Node prevNode = historyMap.get(lastId);
            prevNode.setNextNode(newNode);
        }

        Integer newNodeId = newNode.getData().getId();
        historyMap.put(newNodeId, newNode);

        if (firstId == null) {
            firstId = newNodeId;
        }

        lastId = newNodeId;
    }
}
