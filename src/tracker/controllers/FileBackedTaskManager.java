package tracker.controllers;

import tracker.exeptions.ManagerSaveException;
import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {


    @Override
    public int taskPut(Task task) {
        int id = super.taskPut(task);
        save();
        return id;
    }

    @Override
    public int epicsPut(Epic epic) {
        int id = super.epicsPut(epic);
        save();
        return id;
    }

    @Override
    public int subTaskPut(Epic epic, SubTask subTask) {
        int id = super.subTaskPut(epic, subTask);
        save();
        return id;
    }

    @Override
    public void taskReplace(int id, Task task) {
        super.taskReplace(id, task);
        save();
    }

    @Override
    public void epicReplace(int id, Epic epic) {
        super.epicReplace(id, epic);
        save();
    }

    @Override
    public void subTaskReplace(int id, Epic epic, SubTask subTask) {
        super.subTaskReplace(id, epic, subTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    public void save() {
        try (FileWriter writer = new FileWriter("resources\\TasksMemory")) {
            writer.write("id,type,name,status,description,epic,startTime,duration\n");
            for (Task task : tasks.values()) {
                writer.write(task.toString());
            }
            for (Epic epic : epics.values()) {
                writer.write(epic.toString());
            }

            for (SubTask subTask : subtasks.values()) {
                writer.write(subTask.toString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(file.toString()))) {

            List<String> lines = bufferedReader.lines().toList();
            for (String s : lines) {
                String[] parameters = s.split(",");
                switch (parameters[1]) {
                    case "TASK":
                        Task task = Task.fromString(s);
                        task.setId(Integer.parseInt(parameters[0]));
                        taskManager.tasks.put(Integer.parseInt(parameters[0]), task);
                        if (task.getEndTime().isPresent()) {
                            taskManager.prioritizedTasks.add(task);
                        }
                        break;
                    case "EPIC":
                        Epic epic = Epic.fromString(s);
                        epic.setId(Integer.parseInt(parameters[0]));
                        taskManager.epics.put(Integer.parseInt(parameters[0]), epic);
                        break;
                    case "SUBTASK":
                        SubTask subTask = SubTask.fromString(s);
                        subTask.setEpic(taskManager.epics.get(Integer.parseInt(parameters[5])));
                        taskManager.epics.get(Integer.parseInt(parameters[5])).getSubTasksMap().put(Integer.parseInt(parameters[0]), subTask);
                        subTask.setId(Integer.parseInt(parameters[0]));
                        taskManager.subtasks.put(Integer.parseInt(parameters[0]), subTask);
                        if (subTask.getEndTime().isPresent()) {
                            taskManager.prioritizedTasks.add(subTask);
                        }
                        break;
                }
                if (!parameters[0].equals("id")&&taskManager.generatorId < Integer.parseInt(parameters[0])) {
                    taskManager.generatorId = Integer.parseInt(parameters[0]);
                }
            }
            for (Epic epic : taskManager.epics.values()) {
                taskManager.updateEpicStatus(epic);
                epic.updateTime();
            }


        } catch (IOException | IndexOutOfBoundsException e) {
            throw new ManagerSaveException();
        }
        return taskManager;
    }
}
