package handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.model.SubTask;
import tracker.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static server.HttpTaskServer.taskManager;
import static server.HttpTaskServer.gson;

public class SubTasksHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String uri = exchange.getRequestURI().getPath();


        switch (method) {
            case "GET":

                if (uri.split("/").length == 2) {
                    String subTasksJson = gson.toJson(taskManager.getSubtasks());
                    sendText(exchange, subTasksJson);

                } else if (uri.split("/").length == 3) {
                    try {
                        if (taskManager.getSubTask(Integer.parseInt(uri.split("/")[2])).isPresent()) {
                            sendText(exchange, gson.toJson(taskManager.getSubTask(Integer.parseInt(uri.split("/")[2])).get()));
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
                        task = gson.fromJson(body, SubTask.class);
                    } catch (JsonSyntaxException | ClassCastException e) {
                        sendCode(exchange, 400);
                        return;
                    }
                    if (task instanceof SubTask subTask) {
                        if (subTask.getId() == 0) {
                            if (taskManager.subTaskPut(subTask.getEpic(), subTask) == 0) {
                                sendCode(exchange, 406);
                            } else {
                                sendCode(exchange, 201);
                            }
                        } else {
                            boolean wasReplaced = taskManager.subTaskReplace(subTask.getId(), subTask.getEpic(), subTask);
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
                        if (taskManager.getSubTask(Integer.parseInt(uri.split("/")[2])).isPresent()) {
                            SubTask subTaskToRemove = taskManager.getSubTask(Integer.parseInt(uri.split("/")[2])).get();
                            taskManager.deleteSubtask(Integer.parseInt(uri.split("/")[2]));
                            sendText(exchange, gson.toJson(subTaskToRemove));
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
