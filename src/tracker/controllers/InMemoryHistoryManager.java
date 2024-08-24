package tracker.controllers;

import tracker.model.Task;


import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();


    //-----------MyLinkedList------------------//

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    private Node<Task> linkLast(Task task) {
        if (head == null) {
            head = new Node<>(task);
            tail = head;
        } else {
            Node<Task> oldTail = tail;
            tail = new Node<>(task);
            tail.prev = oldTail;
            oldTail.next = tail;
        }
        size++;
        return tail;
    }

    private List<Task> getTasks() {
        List<Task> tasksArr = new ArrayList<>();
        Node<Task> iterator = head;
        for (int i = 0; i < size; i++) {
            tasksArr.add(iterator.data);
            iterator = iterator.next;
        }
        return tasksArr;
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else { //Это head
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else { //Это tail
            tail = node.prev;
        }
        size--;
    }


    //-----------------------------------------------//
    @Override
    public List<Task> getHistory() {


        return getTasks();
    }

    @Override
    public void remove(int id) {

        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }


    public void add(Task task) {
        remove(task.getId());
        historyMap.put(task.getId(), linkLast(task));

    }
}
