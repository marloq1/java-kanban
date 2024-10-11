package tracker.controllers;

import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.model.Epic;


import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subtasks = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {
        if (task1.getStartTime().isBefore(task2.getStartTime())) {
            return -1;
        } else if (task1.getStartTime().isAfter(task2.getStartTime())) {
            return 1;
        } else {
            return task1.getId() - task2.getId();
        }
    });

    private final HistoryManager historyManager = Managers.getDefaultHistory();


    protected int generatorId = 0;


    @Override
    public int taskPut(Task task) { //d) Добавление новой задачи
        if (validateTask(task)) {
            final int id = ++generatorId;
            task.setId(id);
            tasks.put(id, task);
            if (task.getEndTime().isPresent()) {
                prioritizedTasks.add(task);
            }
            return id;
        } else {
            return 0;
        }

    }

    @Override
    public int epicsPut(Epic epic) { //d) Добавление нового эпика
        if (validateTask(epic)) {
            final int id = ++generatorId;
            epic.setId(id);
            epics.put(id, epic);
            updateEpicStatus(epic);
            return id;
        } else {
            return 0;
        }
    }

    @Override
    public int subTaskPut(Epic epic, SubTask subTask) { //d) Добавление новой подзадачи в эпик
        if (validateTask(subTask)) {
            final int id = ++generatorId;
            subTask.setId(id);
            epic.getSubTasksMap().put(id, subTask);
            subtasks.put(id, subTask);
            subTask.setEpic(epic);
            updateEpicStatus(epic);
            epic.updateTime();
            if (subTask.getEndTime().isPresent()) {
                prioritizedTasks.add(subTask);
            }
            return id;
        } else {
            return 0;
        }
    }


    @Override
    public void taskReplace(int id, Task task) { //e) Обновление задачи
        if (validateTask(task)) {
            if (tasks.containsKey(id)) {
                if (tasks.get(id).getEndTime().isPresent())
                    prioritizedTasks.remove(tasks.get(id));
                historyManager.remove(id);
                tasks.put(id, task);
                task.setId(id);
                if (task.getEndTime().isPresent()) {
                    prioritizedTasks.add(task);
                }
            }
        }
    }

    @Override
    public void epicReplace(int id, Epic epic) { //e) Обновление эпика
        if (validateTask(epic)) {
            if (epics.containsKey(id)) {
                historyManager.remove(id);
                for (Integer subtaskId : epics.get(id).getSubtaskIds()) {
                    if (subtasks.get(subtaskId).getEndTime().isPresent())
                        prioritizedTasks.remove(subtasks.get(subtaskId));
                    historyManager.remove(subtaskId);
                    subtasks.remove(subtaskId);
                }

                epics.put(id, epic);
                epic.setId(id);

                updateEpicStatus(epic);
                epic.updateTime();
            }
        }
    }

    @Override
    public void subTaskReplace(int id, Epic epic, SubTask subTask) { //e) Обновление подзадачи
        if (validateTask(subTask)) {
            if (subtasks.containsKey(id)) {
                if (subtasks.get(id).getEndTime().isPresent())
                    if (subtasks.get(id).getEndTime().isPresent())
                        prioritizedTasks.remove(subtasks.get(id));
                historyManager.remove(id);
                epic.getSubTasksMap().put(id, subTask);
                subtasks.put(id, subTask);
                subTask.setId(id);
                subTask.setEpic(epic);
                updateEpicStatus(epic);
                epic.updateTime();
                if (subTask.getEndTime().isPresent()) {
                    prioritizedTasks.add(subTask);
                }
            }
        }
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.get(id).getEndTime().isPresent())
            prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        historyManager.remove(id);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            if (subtasks.get(subtaskId).getEndTime().isPresent())
                prioritizedTasks.remove(subtasks.get(subtaskId));
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        if (subtasks.get(id).getEndTime().isPresent())
            prioritizedTasks.remove(subtasks.get(id));
        SubTask subtask = subtasks.remove(id);
        historyManager.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic);
        epic.updateTime();
    }


    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }


    @Override
    public Optional<Task> getTask(int id) { //c) Получение задачи по идентификатору

        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return Optional.of(tasks.get(id));
        } else
            return Optional.empty();
    }

    @Override
    public Optional<Epic> getEpic(int id) { // c) Получение эпика с подзадачами по идентификатору

        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return Optional.of(epics.get(id));
        } else
            return Optional.empty();

    }

    @Override
    public Optional<SubTask> getSubTask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return Optional.of(subtasks.get(id));
        } else
            return Optional.empty();
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(int id) { // 3.a) Получение списка всех подзадач определенного эпика
        if (epics.containsKey(id)) {
            return epics.get(id).getSubTasks();
        } else {
            return null;
        }
    }


    public void updateEpicStatus(Epic epic) { //4.a) Управление статусами
        int marker1 = 0, marker2 = 0, marker3 = 0;
        for (int key : epic.getSubTasksMap().keySet()) {
            if ((epic.getSubTasksMap().get(key).getStatus() == Status.NEW) && (marker1 == 0)) {
                marker1 = 1;
            }
            if ((epic.getSubTasksMap().get(key).getStatus() == Status.DONE) && (marker2 == 0)) {
                marker2 = 1;
            }
            if ((epic.getSubTasksMap().get(key).getStatus() == Status.IN_PROGRESS) && (marker3 == 0)) {
                marker3 = 1;
            }
        }
        if (marker1 == 0 && marker2 == 1 && marker3 == 0) {
            epic.setStatus(Status.DONE);
        } else if ((marker1 == 1 && marker2 == 0 && marker3 == 0) || (marker1 == 0 && marker2 == 0 && marker3 == 0)) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            if (task.getEndTime().isPresent())
                prioritizedTasks.remove(task);
            historyManager.remove(task.getId());
        }
        tasks.clear();

    }

    @Override
    public void deleteSubtasks() {
        for (Task task : subtasks.values()) {
            if (task.getEndTime().isPresent())
                prioritizedTasks.remove(task);
            historyManager.remove(task.getId());
        }
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic);
            epic.updateTime();
        }
        subtasks.clear();

    }

    @Override
    public void deleteEpics() {
        for (Task task : epics.values()) {
            historyManager.remove(task.getId());
        }
        for (Task task : subtasks.values()) {
            if (task.getEndTime().isPresent())
                prioritizedTasks.remove(task);
            historyManager.remove(task.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    /*@Override
    public Set<Task> getPrioritizedTasks() {
        return Stream.concat(tasks.values().stream(),
                        subtasks.values().stream()).filter(task -> task.getStartTime() != null)
                        .collect(Collectors.toCollection(() -> new TreeSet<>((task1, task2) -> {
                    if (task1.getStartTime().isBefore(task2.getStartTime())) {
                        return -1;
                    } else if (task1.getStartTime().isAfter(task2.getStartTime()) || (task2.getStartTime() == null)) {
                        return 1;
                    } else {
                        return task1.getId() - task2.getId();
                    }
                })));
    }*/

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);

    }

    @Override
    public boolean validateTask(Task task) {
        List<Task> sortedList = getPrioritizedTasks();
        if (task.getStartTime() == null) {
            return true;
        } else {
            return sortedList.stream()
                    .filter(task1 -> !(task1.getEndTime().get().isBefore(task.getStartTime())
                            || task1.getEndTime().get().isEqual(task.getStartTime())))
                    .filter(task1 -> !(task1.getStartTime().isAfter(task.getEndTime().get())
                            || task1.getStartTime().isEqual(task.getEndTime().get())))
                    .collect(Collectors.toSet()).isEmpty();
        }
    }
}
