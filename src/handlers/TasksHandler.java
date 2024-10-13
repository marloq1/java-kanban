package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.*;
import tracker.model.SubTask;
import tracker.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static server.HttpTaskServer.getGson;
import static server.HttpTaskServer.taskManager;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String uri = exchange.getRequestURI().getPath();
        Gson gson = getGson();


        switch (method) {
            case "GET":

                if (uri.split("/").length == 2) {
                    try {
                        String tasksJson = gson.toJson(taskManager.getTasks());
                        sendText(exchange, tasksJson);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else if (uri.split("/").length == 3) {
                    try {
                        if (taskManager.getTask(Integer.parseInt(uri.split("/")[2])).isPresent()) {
                            sendText(exchange, gson.toJson(taskManager.getTask(Integer.parseInt(uri.split("/")[2])).get()));
                        } else {
                            sendCode(exchange, 404);
                        }
                    } catch (NumberFormatException e) {
                        sendCode(exchange, 404);
                    }
                } else {
                    sendCode(exchange, 404);
                }
                break;
            case "POST":
                if (uri.split("/").length == 2) {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task task;
                    try {
                        task = gson.fromJson(body, Task.class);
                    } catch (JsonSyntaxException e) {
                        sendCode(exchange, 400);
                        return;
                    }
                    if ((task != null) && (!(task instanceof SubTask))) {
                        if (task.getId() == 0) {
                            if (taskManager.taskPut(task) == 0) {
                                sendCode(exchange, 406);
                            } else {
                                sendCode(exchange, 201);
                            }
                        } else {
                            boolean wasReplaced = taskManager.taskReplace(task.getId(), task);
                            if (wasReplaced) {
                                sendCode(exchange, 201);
                            } else {
                                sendCode(exchange, 406);
                            }


                        }
                    } else {
                        sendCode(exchange, 400);
                    }
                } else {
                    sendCode(exchange, 404);
                }
                break;
            case "DELETE":
                if (uri.split("/").length == 3) {
                    try {
                        if (taskManager.getTask(Integer.parseInt(uri.split("/")[2])).isPresent()) {
                            Task taskToRemove = taskManager.getTask(Integer.parseInt(uri.split("/")[2])).get();
                            taskManager.deleteTask(Integer.parseInt(uri.split("/")[2]));
                            sendText(exchange, gson.toJson(taskToRemove));
                        } else {
                            sendCode(exchange, 404);
                        }
                    } catch (NumberFormatException e) {
                        sendCode(exchange, 404);
                    }
                }
                break;
            default:
                sendCode(exchange, 400);

        }


    }
}
