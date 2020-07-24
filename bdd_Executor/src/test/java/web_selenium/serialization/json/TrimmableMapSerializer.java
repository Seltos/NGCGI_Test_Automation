package web_selenium.serialization.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Customizes the JSON serialization of Map objects by limiting the maximum
 * length of map values.
 */
public class TrimmableMapSerializer implements JsonSerializer<Map> {

    private int logEntryMaxChars;

    private int logEntryTrimmedSize;

    public TrimmableMapSerializer(int logEntryMaxChars, int logEntryTrimmedSize) {
        this.logEntryMaxChars = logEntryMaxChars;
        this.logEntryTrimmedSize = logEntryTrimmedSize;
    }

    @Override
    public JsonElement serialize(Map srcMap, Type typeOfSrc, JsonSerializationContext context) {
        if (this.logEntryMaxChars == 0) {
            return context.serialize(new HashMap(srcMap));
        }

        JsonObject obj = new JsonObject();

        for (Object key : srcMap.keySet()) {
            JsonElement jsonElement = context.serialize(srcMap.get(key));

            if (jsonElement instanceof JsonPrimitive) {
                JsonPrimitive primitive = (JsonPrimitive) jsonElement;
                // Truncate string values, if too large
                if (primitive.isString()) {
                    String jsonElementStr = jsonElement.getAsString();
                    if (jsonElementStr.length() > this.logEntryMaxChars) {
                        jsonElement = new JsonPrimitive(jsonElementStr.substring(0, this.logEntryTrimmedSize) + " <<VALUE_WAS_TRUNCATED>>");
                    }
                }
            } else {
                // Truncate values of type object/array, if too large
                String jsonElementStr = jsonElement.toString();
                if (jsonElementStr.length() > this.logEntryMaxChars) {
                    jsonElement = new JsonPrimitive(jsonElementStr.substring(0, this.logEntryTrimmedSize) + " <<VALUE_WAS_TRUNCATED>>");
                }
            }

            obj.add(key.toString(), jsonElement);
        }

        return obj;
    }
}
