package handlers;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.IOException;

import static server.HttpTaskServer.*;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = getGson();
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
