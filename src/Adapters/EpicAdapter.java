package Adapters;

import com.google.gson.TypeAdapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import tracker.model.Epic;
import tracker.model.SubTask;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EpicAdapter extends TypeAdapter<Epic> {


    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id");
        jsonWriter.value(epic.getId());
        jsonWriter.name("name");
        jsonWriter.value(epic.getName());
        if (epic.getStatus() != null) {
            jsonWriter.name("status");
            jsonWriter.value(epic.getStatus().toString());
        }
        jsonWriter.name("description");
        jsonWriter.value(epic.getDescription());
        jsonWriter.name("subtasks");
        jsonWriter.beginArray();
        for (SubTask subTask : epic.getSubTasks()) {
            jsonWriter.beginObject();
            jsonWriter.name("id");
            jsonWriter.value(subTask.getId());
            jsonWriter.name("epic");
            jsonWriter.value(subTask.getEpicId());
            jsonWriter.name("name");
            jsonWriter.value(subTask.getName());
            jsonWriter.name("status");
            jsonWriter.value(subTask.getStatus().toString());
            jsonWriter.name("description");
            jsonWriter.value(subTask.getDescription());
            if (subTask.getStartTime() != null) {
                jsonWriter.name("startTime");
                jsonWriter.value(subTask.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyyг")));
                jsonWriter.name("duration");
                jsonWriter.value(subTask.getDuration().toMinutes());
            }
            jsonWriter.endObject();

        }
        jsonWriter.endArray();
        if (epic.getStartTime() != null) {
            jsonWriter.name("startTime");
            jsonWriter.value(epic.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyyг")));
            jsonWriter.name("duration");
            jsonWriter.value(epic.getDuration().toMinutes());
        }
        jsonWriter.endObject();
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        int id = 0;
        String name = null;
        String description = null;
        Epic epic = null;
        List<SubTask> subTasks = new ArrayList<>();
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
                    case "description":
                        description = jsonReader.nextString();
                        break;
                    default:
                        jsonReader.skipValue();
                }
        }
        jsonReader.endObject();
        if ((name != null) && (description != null)) {
            epic = new Epic(name, description);
            if (id != 0) {
                epic.setId(id);
            }


        }
        return epic;
    }

}
