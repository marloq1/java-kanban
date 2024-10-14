package adapters;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
        if (localDate != null) {
            jsonWriter.value(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            jsonWriter.nullValue();
        }

    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
