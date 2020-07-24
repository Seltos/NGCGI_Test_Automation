package web_selenium.serialization.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Custom Gson serializer for Double numbers. This is necessary to avoid the
 * ".0" suffix for numbers that have no decimal part.
 */
public class DoubleSerializer implements JsonSerializer<Double> {

    @Override
    public JsonElement serialize(Double src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
        if (src == Math.ceil(src)) {
            return new JsonPrimitive(src.intValue());
        } else {
            return new JsonPrimitive(src);
        }
    }
}
