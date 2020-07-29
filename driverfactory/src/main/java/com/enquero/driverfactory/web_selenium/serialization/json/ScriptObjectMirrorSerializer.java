package com.enquero.driverfactory.web_selenium.serialization.json;

import com.google.gson.*;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import com.enquero.driverfactory.web_selenium.util.Factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom Gson serializer for ScriptObjectMirror instances. Without using a
 * custom serializer, Gson will serialize JavaScript arrays as objects (the same
 * way it serializes Java maps).
 */
public class ScriptObjectMirrorSerializer implements JsonSerializer<ScriptObjectMirror> {

    private Gson gson;

    public ScriptObjectMirrorSerializer(boolean serializeNulls) {
        if (this.gson == null) {
            GsonBuilder builder = Factory.getGsonBuilder();

            if (serializeNulls) {
                builder = builder.serializeNulls();
            }

            this.gson = builder
                    .registerTypeAdapter(ScriptObjectMirror.class, this)
                    .create();
        }
    }

    @Override
    public JsonElement serialize(ScriptObjectMirror src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
        if (src.isArray()) {
            return this.gson.toJsonTree(src.values());
        } else {
            Map<String, Object> map = new HashMap<>();
            map.putAll(src);
            return this.gson.toJsonTree(map);
        }
    }
}