package managers;

import tasks.Task;

public class Node {
    private final Task data;
    private Node previousNode;
    private Node nextNode;

    public Node(Task data, Node previousNode) {
        this.data = data;
        this.previousNode = previousNode;
    }


    public Task getData() {
        return data;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }
}
