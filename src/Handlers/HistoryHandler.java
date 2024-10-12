package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import static server.HttpTaskServer.taskManager;
import static server.HttpTaskServer.gson;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String uri = exchange.getRequestURI().getPath();

        if (method.equals("GET")) {
            if (uri.split("/").length == 2) {
                String tasksJson = gson.toJson(taskManager.getHistoryManager().getHistory());
                sendText(exchange, tasksJson);
            }
        } else {
            sendCode(exchange, 400);
        }
    }
}
