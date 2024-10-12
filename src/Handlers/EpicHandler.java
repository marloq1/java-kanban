package Handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.model.Epic;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static server.HttpTaskServer.gson;
import static server.HttpTaskServer.taskManager;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String uri = exchange.getRequestURI().getPath();


        switch (method) {
            case "GET":

                if (uri.split("/").length == 2) {
                    String tasksJson = gson.toJson(taskManager.getEpics());
                    sendText(exchange, tasksJson);
                } else if (uri.split("/").length == 3) {
                    try {
                        if (taskManager.getEpic(Integer.parseInt(uri.split("/")[2])).isPresent()) {
                            sendText(exchange, gson.toJson(taskManager
                                    .getEpic(Integer.parseInt(uri.split("/")[2])).get()));
                        } else {
                            sendCode(exchange, 404);
                        }
                    } catch (NumberFormatException e) {
                        sendCode(exchange, 404);
                    }
                } else if (uri.split("/").length == 4) {
                    if (uri.split("/")[3].equals("subtasks")) {
                        try {
                            if (taskManager.getEpic(Integer.parseInt(uri.split("/")[2])).isPresent()) {
                                sendText(exchange, gson.toJson(taskManager
                                        .getSubTasksOfEpic(Integer.parseInt(uri.split("/")[2]))));
                            } else {
                                sendCode(exchange, 404);
                            }
                        } catch (NumberFormatException e) {
                            sendCode(exchange, 404);
                        }
                    } else {
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
                    Epic epic;
                    try {
                        epic = gson.fromJson(body, Epic.class);
                    } catch (JsonSyntaxException e) {
                        sendCode(exchange, 400);
                        return;
                    }
                    if ((epic != null)) {
                        if (epic.getId() == 0) {
                            if (taskManager.epicsPut(epic) == 0) {
                                sendCode(exchange, 406);
                            } else {
                                sendCode(exchange, 201);
                            }
                        } else {
                            taskManager.epicReplace(epic.getId(), epic);
                            sendCode(exchange, 201);
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
                        if (taskManager.getEpic(Integer.parseInt(uri.split("/")[2])).isPresent()) {
                            Epic epicToRemove = taskManager.getEpic(Integer.parseInt(uri.split("/")[2])).get();
                            taskManager.deleteEpic(Integer.parseInt(uri.split("/")[2]));
                            sendText(exchange, gson.toJson(epicToRemove));
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
