package edu.sdccd.cisc191.library.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * When serializing or deserializing date-time classes in Java 8, the InaccessibleObjectException is thrown. The Gson
 * docs recommend writing a custom TypeAdapter for the specific type that is causing the problem. The adapter (borrowed
 * from the link below) implements JsonSerializer and JsonDeserializer interfaces and provides the custom logic for
 * serializing and deserializing the instance.
 * */

// https://howtodoinjava.com/gson/gson-typeadapter-for-inaccessibleobjectexception/

public class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public JsonElement serialize(final LocalDate date, final Type typeOfSrc,
                                 final JsonSerializationContext context) {
        return new JsonPrimitive(date.format(formatter));
    }

    @Override
    public LocalDate deserialize(final JsonElement json, final Type typeOfT,
                                 final JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString(), formatter);
    }
}