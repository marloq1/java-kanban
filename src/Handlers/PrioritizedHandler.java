package Handlers;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import static server.HttpTaskServer.gson;


import java.io.IOException;

import static server.HttpTaskServer.taskManager;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {


        String method = exchange.getRequestMethod();
        String uri = exchange.getRequestURI().getPath();

        if (method.equals("GET")) {
            if (uri.split("/").length == 2) {
                String tasksJson = gson.toJson(taskManager.getPrioritizedTasks());
                sendText(exchange, tasksJson);
            }
        } else {
            sendCode(exchange, 400);
        }

    }
}
