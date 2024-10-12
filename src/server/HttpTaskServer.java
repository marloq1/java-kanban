package server;

import adapters.EpicAdapter;
import adapters.TaskAdapter;
import handlers.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.model.Epic;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    public static TaskManager taskManager;
    public static HttpServer httpServer;
    public static Gson gson = new GsonBuilder().registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .registerTypeAdapter(SubTask.class, new TaskAdapter()).setPrettyPrinting().create();

    public HttpTaskServer(TaskManager manager) {
        taskManager = manager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public static void main(String[] args) throws IOException {
        taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    public void start() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            httpServer.createContext("/tasks", new TasksHandler());
            httpServer.createContext("/subtasks", new SubTasksHandler());
            httpServer.createContext("/epics", new EpicHandler());
            httpServer.createContext("/history", new HistoryHandler());
            httpServer.createContext("/prioritized", new PrioritizedHandler());
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        httpServer.stop(1);
    }

    public static Gson getGson() {
        return gson;
    }


}
