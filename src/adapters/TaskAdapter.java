package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static server.HttpTaskServer.taskManager;

public class TaskAdapter extends TypeAdapter<Task> {


    @Override
    public void write(JsonWriter jsonWriter, Task task) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id");
        jsonWriter.value(task.getId());
        jsonWriter.name("name");
        jsonWriter.value(task.getName());
        jsonWriter.name("status");
        jsonWriter.value(task.getStatus().toString());
        jsonWriter.name("description");
        jsonWriter.value(task.getDescription());
        if (task instanceof SubTask) {
            jsonWriter.name("epic");
            SubTask subTask = (SubTask) task;
            jsonWriter.value(subTask.getEpicId());
        }
        if (task.getStartTime() != null) {
            jsonWriter.name("startTime");
            jsonWriter.value(task.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyyг")));
            jsonWriter.name("duration");
            jsonWriter.value(task.getDuration().toMinutes());
        }
        jsonWriter.endObject();
    }

    @Override
    public Task read(JsonReader jsonReader) throws IOException {
        int id = 0;
        String name = null;
        Status status = null;
        String description = null;
        LocalDateTime startTime = null;
        Duration duration = null;
        Epic epic = null;
        boolean isSubTask = false;
        Task task = null;
        SubTask subTask = null;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();
            String fieldname = null;

            if (token.equals(JsonToken.NAME)) {
                fieldname = jsonReader.nextName();
            }
            if (fieldname != null)
                switch (fieldname) {
                    case "id":
                        id = jsonReader.nextInt();
                        break;
                    case "name":
                        name = jsonReader.nextString();
                        break;
                    case "status":
                        String strStatus = jsonReader.nextString();
                        switch (strStatus) {
                            case "NEW":
                                status = Status.NEW;
                                break;
                            case "DONE":
                                status = Status.DONE;
                                break;
                            case "IN_PROGRESS":
                                status = Status.IN_PROGRESS;
                                break;
                        }
                        break;
                    case "description":
                        description = jsonReader.nextString();
                        break;
                    case "epic":
                        isSubTask = true;
                        int epicId = jsonReader.nextInt();
                        if (taskManager.getEpic(epicId).isPresent()) {
                            epic = taskManager.getEpic(epicId).get();
                        }

                        break;
                    case "startTime":
                        startTime = LocalDateTime.parse(jsonReader.nextString(), DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyyг"));
                        break;
                    case "duration":
                        duration = Duration.ofMinutes(jsonReader.nextLong());
                        break;
                    default:
                        jsonReader.skipValue();
                }
        }
        jsonReader.endObject();

        if ((name != null) && (description != null) && (status != null)) {
            if (!isSubTask) {
                if ((startTime != null) && (duration != null)) {
                    task = new Task(name, description, status, startTime, duration);
                } else {
                    task = new Task(name, description, status);
                }

                if (id > 0) {
                    task.setId(id);
                }
                return task;
            } else {
                if ((startTime != null) && (duration != null)) {
                    subTask = new SubTask(name, description, status, startTime, duration);
                } else {
                    subTask = new SubTask(name, description, status);
                }

                if (id > 0) {
                    subTask.setId(id);
                }
                if (epic != null) {
                    subTask.setEpic(epic);
                    return subTask;
                } else {
                    return null;
                }
            }

        }
        return null;
    }
}
