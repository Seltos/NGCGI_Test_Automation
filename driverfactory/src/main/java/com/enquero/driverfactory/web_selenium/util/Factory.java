package com.enquero.driverfactory.web_selenium.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import com.enquero.driverfactory.web_selenium.contracts.IImageFinder;
import com.enquero.driverfactory.web_selenium.serialization.json.*;
import com.enquero.driverfactory.web_selenium.serialization.yaml.SkipNullRepresenter;
import com.enquero.driverfactory.web_selenium.testdef.TestDefAction;
import com.enquero.driverfactory.web_selenium.visual.ImageFinder;

import java.awt.image.BufferedImage;

public class Factory {

    private static Config defaultConfig;

    private static Gson defaultGson;

    private static Config getDefaultConfig() {
        if (defaultConfig == null) {
            defaultConfig = new Config();
            defaultConfig.set("logEntryMaxChars", 2000);
            defaultConfig.set("logEntryTrimmedSize", 100);
        }

        return defaultConfig;
    }

    public static IImageFinder getImageFinder() {
        return new ImageFinder();
    }

    /**
     * Returns a Gson object pre-configured with some defaults (disable HTML
     * escaping, custom ScriptObjectMirror serializer, etc.).
     */
    public static Gson getGson() {
        if (defaultGson == null) {
            defaultGson = Factory.getGsonBuilder(getDefaultConfig())
                    .registerTypeAdapter(ScriptObjectMirror.class, new ScriptObjectMirrorSerializer(false))
                    .create();
        }

        return defaultGson;
    }

    /**
     * Returns a Gson object pre-configured with some defaults (disable HTML
     * escaping, custom ScriptObjectMirror serializer, etc.).
     */
    public static Gson getGson(Config config) {
        return Factory.getGsonBuilder(config)
                .registerTypeAdapter(ScriptObjectMirror.class, new ScriptObjectMirrorSerializer(false))
                .create();
    }

    /**
     * Returns a Gson builder object pre-configured with some defaults (disable
     * HTML escaping, custom ScriptObjectMirror serializer, etc.).
     */
    public static GsonBuilder getGsonBuilder() {
        return getGsonBuilder(getDefaultConfig());
    }

    /**
     * Returns a Gson builder object pre-configured with some defaults (disable
     * HTML escaping, custom ScriptObjectMirror serializer, etc.).
     */
    public static GsonBuilder getGsonBuilder(Config config) {
        GsonBuilder builder = new GsonBuilder()
                .addDeserializationExclusionStrategy(new DuplicateFieldExclusionStrategy())
                .addSerializationExclusionStrategy(new DuplicateFieldExclusionStrategy())
                .disableHtmlEscaping()
                .registerTypeAdapter(Double.class, new DoubleSerializer())
                .registerTypeAdapter(BufferedImage.class, new BufferedImageSerializer())
                .registerTypeAdapter(
                        TrimmableMap.class,
                        new TrimmableMapSerializer(
                                config.getInteger("logEntryMaxChars", Factory.getDefaultConfig().getInteger("logEntryMaxChars")),
                                config.getInteger("logEntryTrimmedSize", Factory.getDefaultConfig().getInteger("logEntryTrimmedSize"))));
        return builder;
    }

    /**
     * Returns a YAML parser object pre-configured with some defaults.
     */
    public static Yaml getYaml() {
        final DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        options.setPrettyFlow(true);

        SkipNullRepresenter representer = new SkipNullRepresenter();
        representer.addClassTag(TestDefAction.class, Tag.MAP);

        return new Yaml(representer, options);
    }

    public static TesseractOcr getTesseractOcr() {
        return new TesseractOcr();
    }
}
