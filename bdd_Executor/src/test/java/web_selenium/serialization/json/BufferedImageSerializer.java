package web_selenium.serialization.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.awt.image.BufferedImage;

public class BufferedImageSerializer implements JsonSerializer<BufferedImage> {

    public BufferedImageSerializer() {
        super();
    }

    @Override
    public JsonElement serialize(BufferedImage src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
        JsonPrimitive jsonString = new JsonPrimitive(String.format(
                "\"Image, res: %s x %s\"",
                src.getWidth(),
                src.getHeight()));
        return jsonString;
    }
}
