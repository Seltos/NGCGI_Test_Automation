package com.enquero.driverfactory.web_selenium.base;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Undefined;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.yaml.snakeyaml.Yaml;
import com.enquero.driverfactory.web_selenium.contracts.ILogger;
import com.enquero.driverfactory.web_selenium.contracts.ITestActor;
import com.enquero.driverfactory.web_selenium.exceptions.CheckpointException;
import com.enquero.driverfactory.web_selenium.exceptions.IntentionalFailException;
import com.enquero.driverfactory.web_selenium.http.ContentType;
import com.enquero.driverfactory.web_selenium.http.HttpRequest;
import com.enquero.driverfactory.web_selenium.http.HttpRequestOptions;
import com.enquero.driverfactory.web_selenium.http.HttpVerb;
import com.enquero.driverfactory.web_selenium.logging.*;
import com.enquero.driverfactory.web_selenium.serialization.json.ScriptObjectMirrorSerializer;
import com.enquero.driverfactory.web_selenium.serialization.json.TrimmableMap;
import com.enquero.driverfactory.web_selenium.testdef.*;
import com.enquero.driverfactory.web_selenium.util.*;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.stream.Collectors;


/**
 * Implements the core functionality for a test actor type: reads the
 * configuration from a properties file, announces the actorType to the sync
 * service, then identifies, parses and executes the tests, as orchestrated by
 * the sync service.
 */
public class TestActor extends Observable implements ITestActor {

    /**
     * The string identifier of the actorType.
     */
    private String actorId;

    /**
     * The tags that were specified for this actor in the configuration file.
     */
    private Set<String> actorTags;

    /**
     * Identifies the type of actorType (GMA, NP6, etc.).
     */
    private final String actorType;

    private long announceCount = 0;

    private Thread announceThread;

    /**
     * Flags all threads and activities to shut down.
     */
    private boolean actorIsStopping;

    private Config config;

    /**
     * The test action instance that is currently being executed, or null.
     */
    private TestAction currentAction;

    /**
     * The number of the current test action in the current test segment being
     * executed.
     */
    private Integer currentActionNo;

    /**
     * Stores information about the test action currently executing. This info
     * is passed to the sync service when a test segment ends to be used for
     * reporting purposes.
     */
    private TestActionInfo currentActionInfo;

    /**
     * In a data-driven test, this stores the data table containing the data
     * records to iterate over.
     */
    private List<Object> currentDataSet;

    private String currentScript;

    /**
     * Stores information about the test actions in the current test segment.
     * This info is passed to the sync service to be used for reporting
     * purposes.
     */
    private List<TestActionInfo> currentSegmentActions;

    /**
     * The number of the current test segment in the current test being
     * executed.
     */
    private Integer currentSegmentNo;

    private boolean currentSegmentIsCompleted;

    /**
     * Stores information about the current test session, if any. If not null,
     * it means that this test actorType was acquired by the sync service and
     * allocated to a particular test session
     */
    private TestSessionStatus currentSessionStatus;

    private TestDefinition currentTest;

    /**
     * Caches the content of data files used in the tests, as soon as a test
     * first reads a value from a data file. The cache is reset at the beginning
     * of each test session.
     */
    private Map<String, Object> dataFileCache;

    private Encryptor encryptor;

    private String httpProxy;

    /**
     * Stores a list of partial file names that represent the JavaScript files
     * that were already included as part of the current test. This list is
     * needed in order to ensure that each script is only included once per
     * test.
     */
    private List<String> includedScripts;

    private ILogger log;

    /**
     * The output values for the last test action that was executed, if any.
     */
    private Map<String, Object> lastActionOutput;

    /**
     * The output values for the current macro action being executed, if any.
     */
    private Map<String, Object> lastMacroOutput;

    /**
     * Stores the data that is available to a test at the test actor level (as
     * opposed to shared data, that is stored by the sync service and is
     * available to all test actors).
     */
    private Map<String, Object> localData;

    /**
     * The arguments for the current macro action being executed (if any).
     */
    private Map<String, Object> macroArgs;

    /**
     * Caches macro definitions so we don't have to load them from disk every
     * time they're used.
     */
    private Map<String, MacroDefinition> macroCache;

    /**
     * Stores the names of the currently executing macros.
     */
    private Stack<String> macroStack;

    private ScriptEngine scriptEngine;

    private final String syncServerUrl;

    private File tempDir;

    private File outDir;

    /**
     * The file names of the image files for the screenshots that were captured
     * by this test actor.
     */
    private List<String> screenshots;

    private File screenshotsDir;

    private Thread screenshotUploadThread;

    private File workDir;

    public TestActor() {
        this(null);
    }

    public TestActor(Map<String, Object> configOverrides) {
        ConsoleLogger consoleLogger = new ConsoleLogger();
        this.log = consoleLogger;
        log.info("");

        Logger.setLogger(this.log);
        this.workDir = new File(System.getProperty("user.dir"));
        log.info(String.format("Running in working directory \"%s\"", this.workDir.getAbsolutePath()));

        // This will help avoid some very anoying console messages from the Apache HTTP client
        org.apache.log4j.Logger.getLogger("org.apache.commons.httpclient").setLevel(org.apache.log4j.Level.ERROR);

        try {
            this.config = Config.load("actor.yaml");
        } catch (Exception ex) {
            this.config = new Config();
            if (configOverrides == null) {
                log.warning("Failed to load config file", ex);
            }
        }

        if (configOverrides != null) {
            for (Map.Entry entry : configOverrides.entrySet()) {
                this.config.set(entry.getKey().toString(), entry.getValue());
            }
        }

        consoleLogger.setLevel(LogLevel.valueOf(this.config.getString("logLevel", "DEBUG")));

        logJarVersions();

        logExtensions();

        try {
            // The configuration is loaded from the main JAR file's resources, so
            // we are logging the main class name so we can troubleshoot later
            Class<?> mainClass = MainUtil.getMainClass();
            Logger.trace(String.format("The main class is %s", mainClass.getName()));
        } catch (Exception ex) {
        }

        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Logger.error(e);
            }
        });

        this.actorId = this.config.getString("actorId", null);
        this.actorTags = new HashSet<>();
        List configTags = this.config.getList("actorTags", new ArrayList<String>());
        this.actorTags.addAll(configTags);
        this.actorTags.remove("");
        this.actorType = this.config.getString("actorType");
        if (this.actorType.matches("(.*)[^A-Z0-9\\-_](.*)")) {
            throw new RuntimeException(String.format(
                    "Actor type %s in not valid. The actor type can only contain "
                    + "uppercase letters, numbers, dashes and uderscores.",
                    this.actorType));
        }
        this.httpProxy = config.getString("httpProxy", null);
        if (this.httpProxy != null) {
            Logger.debug(String.format("Using HTTP proxy server %s ", this.httpProxy));
        }
        this.lastActionOutput = new TrimmableMap<>();
        this.lastMacroOutput = new TrimmableMap<>();
        this.localData = new HashMap<>();
        this.macroStack = new Stack<>();
        String syncServerUrl = this.config.getString(
                "serverUrl",
                this.config.getString("syncServerUrl",
                        this.config.getString("syncServiceBaseUrl", null)));
        if (syncServerUrl == null) {
            throw new RuntimeException(
                    "The \"syncServerUrl\" configuration parameter is missing. Please "
                    + "populate it in the actor's config file with the full URL to the "
                    + "sync server endpoint (e.g. http://localgost:3000).");
        }
        this.syncServerUrl = syncServerUrl.replaceAll("^[\\s]+|[/\\s]+$", "");

        this.outDir = Paths.get(System.getProperty("user.dir"), "out").normalize().toFile();
        this.screenshots = new ArrayList<String>();
        this.screenshotsDir = Paths.get(outDir.getAbsolutePath(), "screenshots").toFile();
        screenshotsDir.mkdirs();
        this.tempDir = Paths.get(outDir.getAbsolutePath(), "temp").toFile();
        tempDir.mkdirs();

        if (this.actorId == null || this.actorId.isEmpty()) {
            SecureRandom random = new SecureRandom();
            this.actorId = new BigInteger(16, random).toString(10);
        }
        this.actorIsStopping = false;
        this.currentSessionStatus = null;

        this.scriptEngine = this.createScriptEngine();

        startAnnounceThread();
        startScreenShotUploadThread();
    }

    private void abandonSession() {
        this.currentSessionStatus = null;
    }

    /**
     * Announces the actorType to the sync service, so it can be used for
     * running a test session.
     */
    private void announce() {
        ++this.announceCount;

        try {
            HttpRequestOptions options = new HttpRequestOptions(
                    this.syncServerUrl + "/api/actor/announce",
                    HttpVerb.POST);
            ServerRequest request = new ServerRequest(options, config);

            Map<String, Object> content = new HashMap<>();
            content.put("actorId", actorId);
            content.put("actorType", actorType);
            content.put("actorTags", actorTags);

            String jsonContent = Factory.getGson().toJson(content);
            request.setContent(jsonContent, ContentType.APPLICATION_JSON);
            request.execute();

            int statusCode = request.getResponseStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException(String.format("Failed to announce actor %s%s to the sync service. The HTTP request was %s %s. The response status code was %s.",
                        this.actorType,
                        this.actorId != null ? String.format("(%s)", this.actorId) : "",
                        request.getHttpVerb(),
                        request.getUrl(),
                        statusCode));
            }

            String response = request.getResponseAsString();
            JsonElement jelement = new JsonParser().parse(response);
            JsonObject jsonResponse = jelement.getAsJsonObject();

            JsonElement testSessionIdElem = null;

            if (jsonResponse != null) {
                testSessionIdElem = jelement != null
                        ? jsonResponse.get("testSessionId")
                        : null;
            }

            if (testSessionIdElem != null && !testSessionIdElem.isJsonNull()) {
                String testSessionId = testSessionIdElem.getAsString();
                String currentSessionId = this.currentSessionStatus != null
                        ? this.currentSessionStatus.id
                        : null;

                if (this.currentSessionStatus == null || !testSessionId.equals(currentSessionId)) {
                    Logger.trace("Initializing test session information in the \"announce\" method...");
                    Logger.trace(String.format("Session ID in sync server response was %s; actor session ID was %s",
                            testSessionId,
                            currentSessionId));
                    this.currentSessionStatus = new TestSessionStatus(testSessionId);
                }
            } else {
                this.currentSessionStatus = null;
            }
        } catch (Exception ex) {
            System.out.println(String.format(
                    "Warning: failed to communicate with sync server. Original error was: %s",
                    ex.getMessage()));

            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex1) {
            }
        }
    }

    /**
     * Sets a flag that signals all threads and activities for this actorType to
     * shut down.
     */
    @Override
    public void close() {
        actorIsStopping = true;
    }

    /**
     * Initialize the JavaScript interpreter that will be used to evaluate
     * action arguments and run "script" test actions.
     */
    private ScriptEngine createScriptEngine() {
        NashornScriptEngineFactory nashornFactory = new NashornScriptEngineFactory();
        ScriptEngine engine = nashornFactory.getScriptEngine();

        try {
            InputStream inputStream;

            inputStream = this.getClass().getResourceAsStream("/js/helpers.js");
            engine.eval(new InputStreamReader(inputStream));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to evaluate JS code while preparing the JS interpreter", ex);
        }

        // Keeping a final reference to the current actorType instance, so that we
        // can access it from the anonymous classes below
        final TestActor actor = this;

        // $array
        engine.put("$array", new Function<Object, Object>() {
            @Override
            public Object apply(Object arrayLike) {
                return actor.toJsArray(arrayLike);
            }
        });

        // $base64
        engine.put("$base64", new Function<Object, String>() {
            @Override
            public String apply(Object value) {
                try {
                    byte[] encodedBytes = toBase64(value);
                    return new String(encodedBytes, "UTF-8");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // $config
        engine.put("$config", new Function<String, Object>() {
            @Override
            public Object apply(String propertyName) {
                Object propertyValue = actor.config.get(propertyName, null);
                return propertyValue;
            }
        });

        // $data
        engine.put("$data", new Function<String, Object>() {
            @Override
            public Object apply(String relativePath) {
                if (dataFileCache.containsKey(relativePath)) {
                    return dataFileCache.get(relativePath);
                } else {
                    InputStream dataFileStream;
                    try {
                        dataFileStream = getTestAsset("data", relativePath);
                    } catch (Exception ex) {
                        throw new RuntimeException(String.format("Failed to get data file \"%s\".",
                                relativePath), ex);
                    }

                    Object dataFileContent;
                    try {
                        Yaml yaml = new Yaml();
                        dataFileContent = yaml.load(dataFileStream);
                    } catch (Exception ex) {
                        throw new RuntimeException(String.format("Failed to parse data file \"%s\".",
                                relativePath), ex);
                    }

                    try {
                        dataFileCache.put(relativePath, dataFileContent);
                        dataFileContent = evalObject(dataFileContent, null);
                        return dataFileContent;
                    } catch (Exception ex) {
                        throw new RuntimeException(String.format("Failed to evaluate data file \"%s\".",
                                relativePath), ex);
                    }
                }
            }
        });

        // $decrypt
        engine.put("$decrypt", new Function<String, String>() {
            @Override
            public String apply(String text) {
                String decrypted = actor.getEncryptor().decrypt(text.toString());
                actor.log.addSecret(decrypted);
                return decrypted;
            }
        });

        // $delay
        engine.put("$delay", new Consumer<Object>() {
            @Override
            public void accept(Object durationMs) {
                int durationMsInt = Integer.valueOf(durationMs.toString());
                try {
                    Thread.sleep(durationMsInt);
                } catch (InterruptedException ex) {
                }
            }
        });

        // $encrypt
        engine.put("$encrypt", new Function<String, String>() {
            @Override
            public String apply(String text) {
                actor.log.addSecret(text);
                String encrypted = actor.getEncryptor().encrypt(text.toString());
                return encrypted;
            }
        });

        // $env
        engine.put("$env", new AbstractJSObject() {
            @Override
            public boolean isFunction() {
                return true;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                if (args.length > 0) {
                    String varName = args[0].toString();
                    return System.getenv(varName);
                } else {
                    return System.getenv();
                }
            }
        });

        // $eval
        engine.put("$eval", new Function<Object, Object>() {
            @Override
            public Object apply(Object expression) {
                return evalObject(expression, null);
            }
        });

        // $fail
        engine.put("$fail", new AbstractJSObject() {
            @Override
            public boolean isFunction() {
                return true;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                String errorMessage = "";

                if (args.length == 1 && args[0] != null) {
                    errorMessage = args[0].toString();
                }

                throw new IntentionalFailException(errorMessage);
            }
        });

        // $include
        engine.put("$include", new Consumer<String>() {
            @Override
            public void accept(String filePartialPath) {
                includeScriptFile(filePartialPath);
            }
        });

        // $image
        engine.put("$image", new Function<String, BufferedImage>() {
            @Override
            public BufferedImage apply(String fileName) {
                return getImage(fileName);
            }
        });

        // $json
        engine.put("$json", new Function<Object, Object>() {
            private Gson gson = Factory.getGsonBuilder()
                    .registerTypeAdapter(ScriptObjectMirror.class, new ScriptObjectMirrorSerializer(true))
                    .serializeNulls()
                    .create();

            @Override
            public Object apply(Object value) {
                try {
                    return gson.toJson(value);
                } catch (Exception ex) {
                    throw new RuntimeException(String.format(
                            "Failed to convert value to JSON. The object type "
                            + "was %s and the value was %s",
                            value.getClass().getName(),
                            value), ex);
                }
            }
        });

        // $include
        engine.put("$include", new Consumer<Object>() {
            @Override
            public void accept(Object includesSource) {
                evalIncludes(includesSource);
            }
        });

        // $localData
        engine.put("$localData", new AbstractJSObject() {
            @Override
            public boolean isFunction() {
                return true;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                String propertyName = args[0].toString();

                if (args.length == 1) {
                    return actor.localData.get(propertyName);
                } else if (args.length == 2) {
                    Object propertyValue = args[1];
                    actor.localData.put(propertyName, propertyValue);
                }

                return Undefined.getUndefined();
            }

            @Override
            public void setMember(String name, Object value) {
                actor.localData.put(name, value);
            }

            @Override
            public Object getMember(String name) {
                return actor.localData.get(name);
            }
        });

        // $_logDebug
        engine.put("$_logDebug", (Consumer<Object>) (Object text) -> {
            Logger.debug(String.valueOf(text));
        });

        // $_logError
        engine.put("$_logError", (Consumer<Object>) (Object text) -> {
            Logger.error(String.valueOf(text));
        });

        // $_logInfo
        engine.put("$_logInfo", (Consumer<Object>) (Object text) -> {
            Logger.info(String.valueOf(text));
        });

        // $_logTrace
        engine.put("$_logTrace", (Consumer<Object>) (Object text) -> {
            Logger.trace(String.valueOf(text));
        });

        // $_logWarn
        engine.put("$_logWarn", (Consumer<Object>) (Object text) -> {
            Logger.warning(String.valueOf(text));
        });

        // $log
        try {
            String script
                    = "$log = function(text) { $_logInfo(text); };"
                    + "$log.debug = $_logDebug;"
                    + "$log.error = $_logError;"
                    + "$log.info = $_logInfo;"
                    + "$log.warn = $_logWarn;"
                    + "$log.trace = $_logTrace;";
            engine.eval(script);
        } catch (Exception ex) {
        }

        // $macroArgs
        engine.put("$macroArgs", new AbstractJSObject() {
            @Override
            public boolean isFunction() {
                return true;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                if (args.length == 1) {
                    String argName = args[0].toString();
                    if (actor.macroArgs != null) {
                        try {
                            return actor.convertToJavaScriptType(actor.macroArgs.get(argName));
                        } catch (Exception ex) {
                            throw new RuntimeException(String.format(
                                    "Failed to read macro argument %s",
                                    argName), ex);
                        }

                    } else {
                        actor.log.warning("The $macroArgs API should only be called from macro actions.");
                        return null;
                    }
                } else {
                    throw new RuntimeException(String.format(
                            "You must provide exactly one argument to the $macroArgs function. You provided %s.",
                            args.length));
                }
            }

            @Override
            public void setMember(String name, Object value) {
                this.call(null, new Object[]{name, value});
            }

            @Override
            public Object getMember(String name) {
                return this.call(null, new Object[]{name});
            }
        });

        // $macroOutput
        engine.put("$macroOutput", new AbstractJSObject() {
            @Override
            public boolean isFunction() {
                return true;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                if (args.length == 2) {
                    String propName = args[0].toString();
                    Object propValue = args[1];

                    actor.lastMacroOutput.put(propName, propValue);
                } else {
                    throw new RuntimeException(String.format(
                            "You must provide exactly two arguments to the $macroOutput function. You provided %s.",
                            args.length));
                }

                return Undefined.getUndefined();
            }

            @Override
            public void setMember(String name, Object value) {
                this.call(null, new Object[]{name, value});
            }

            @Override
            public Object getMember(String name) {
                return this.call(null, new Object[]{name});
            }
        });

        // $maskSecret
        engine.put("$maskSecret", new Function<String, String>() {
            @Override
            public String apply(String secret) {
                actor.log.addSecret(secret);
                return secret;
            }
        });

        // $maskSecretByRegex
        engine.put("$maskSecretByRegex", new Consumer<String>() {
            @Override
            public void accept(String secretRegexStr) {
                actor.log.addSecretByRegex(secretRegexStr);
            }
        });

        // $output
        engine.put("$output", new AbstractJSObject() {
            @Override
            public boolean isFunction() {
                return true;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                if (args.length == 1) {
                    String propName = args[0].toString();

                    if (actor.lastActionOutput != null) {
                        return actor.lastActionOutput.get(propName);
                    } else {
                        throw new RuntimeException(
                                "There are no output values to read from. Please report "
                                + "this error to the dev team, including the relevant log "
                                + "files and information on how to reproduce the issue.");
                    }
                } else {
                    throw new RuntimeException(String.format(
                            "You must provide exactly one argument to the $output function. You provided %s.",
                            args.length));
                }
            }

            @Override
            public void setMember(String name, Object value) {
                this.call(null, new Object[]{name, value});
            }

            @Override
            public Object getMember(String name) {
                return this.call(null, new Object[]{name});
            }
        });

        // $readOutput
        try {
            engine.eval("$readOutput = $output");
        } catch (Exception ex) {
        }

        // $require
        engine.put("$require", new Function<String, Object>() {
            @Override
            public Object apply(String relativePath) {
                // TODO: Continue implementation

                return null;
            }
        });

        // $runAction
        engine.put("$runAction", new AbstractJSObject() {
            @Override
            public boolean isFunction() {
                return true;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                String actionClassName = (String) args[0];

                Map<String, Object> argsMap;
                if (args.length > 1) {
                    argsMap = (Map<String, Object>) args[1];
                } else {
                    argsMap = new HashMap<String, Object>();
                }

                try {
                    TestDefAction actionDef = new TestDefAction();
                    actionDef.action = actionClassName;
                    actionDef.args = argsMap;
                    return executeActionByDef(actionDef);
                } catch (Exception ex) {
                    throw new RuntimeException(String.format(
                            "Failed executing action %s %s",
                            actionClassName,
                            getArgsStringDescription(argsMap)), ex);
                }
            }
        });

        // $runMacro
        engine.put("$runMacro", new AbstractJSObject() {
            @Override
            public boolean isFunction() {
                return true;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                String macroName = (String) args[0];

                Map<String, Object> argsMap;
                if (args.length > 1) {
                    argsMap = (Map<String, Object>) args[1];
                } else {
                    argsMap = new HashMap<String, Object>();
                }

                try {
                    return executeMacroActionByName(macroName, argsMap);
                } catch (Exception ex) {
                    throw new RuntimeException(String.format(
                            "Failed executing macro %s %s",
                            macroName,
                            getArgsStringDescription(argsMap)), ex);
                }
            }
        });

        // $sessionData
        engine.put("$sessionData", new AbstractJSObject() {
            @Override
            public boolean isFunction() {
                return true;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                String propertyName = args[0].toString();

                if (args.length == 1) {
                    String url = String.format("%s/api/session/%s/data",
                            TestActor.this.syncServerUrl,
                            TestActor.this.currentSessionStatus.id);

                    try {
                        HttpRequestOptions options = new HttpRequestOptions(
                                url,
                                HttpVerb.GET);
                        ServerRequest request = new ServerRequest(options, config);

                        request.execute();
                        String responseBody = request.getResponseAsString();
                        Map<String, Object> sessionData = new Gson().fromJson(responseBody, Map.class);
                        return sessionData.get(propertyName);
                    } catch (Exception ex) {
                        throw new RuntimeException(String.format(
                                "There was an error while requesting the test session data "
                                + "from the sync service. HTTP request details: %s",
                                "GET " + url));
                    }
                } else if (args.length == 2) {
                    Object propertyValue = args[1];
                    try {
                        TestActor.this.publishSessionData(propertyName, propertyValue);
                    } catch (Exception ex) {
                        throw new RuntimeException(String.format(
                                "There was an error while publishing the test session "
                                + "data to the sync service."), ex);
                    }
                }

                return Undefined.getUndefined();
            }

            @Override
            public void setMember(String name, Object value) {
                this.call(null, new Object[]{name, value});
            }

            @Override
            public Object getMember(String name) {
                return this.call(null, new Object[]{name});
            }
        });

        // $sharedData
        engine.put("$sharedData", new AbstractJSObject() {
            @Override
            public boolean isFunction() {
                return true;
            }

            @Override
            public Object call(Object thiz, Object... args) {
                String propertyName = args[0].toString();

                if (args.length == 1) {
                    String url = String.format("%s/api/session/%s/test/%s/data",
                            TestActor.this.syncServerUrl,
                            TestActor.this.currentSessionStatus.id,
                            TestActor.this.currentSessionStatus.currentTestIndex);

                    try {
                        HttpRequestOptions options = new HttpRequestOptions(
                                url,
                                HttpVerb.GET);
                        ServerRequest request = new ServerRequest(options, config);

                        request.execute();
                        String responseBody = request.getResponseAsString();
                        Map<String, Object> sharedData = new Gson().fromJson(responseBody, Map.class);

                        if (sharedData.containsKey(propertyName)) {
                            return sharedData.get(propertyName);
                        } else {
                            log.warning(String.format(
                                    "Shared data property \"%s\" doesn't exist. Make sure you are not "
                                    + "reading the property in the same test segment it is created in "
                                    + "(e.g. if you publish some shared data property in segment 1, it "
                                    + "is only safe to read/use that property in segment 2 or later)",
                                    propertyName));
                            return null;
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(String.format(
                                "There was an error while requesting the test shared data from the "
                                + "sync service. HTTP request was: %s",
                                "GET " + url), ex);
                    }
                } else if (args.length == 2) {
                    Object propertyValue = args[1];
                    try {
                        TestActor.this.publishSharedData(propertyName, propertyValue);
                    } catch (Exception ex) {
                        throw new RuntimeException(String.format(
                                "There was an error while publishing the test shared data to the sync service."), ex);
                    }
                }

                return Undefined.getUndefined();
            }

            @Override
            public void setMember(String name, Object value) {
                this.call(null, new Object[]{name, value});
            }

            @Override
            public Object getMember(String name) {
                return this.call(null, new Object[]{name});
            }
        });

        // $tempDir
        engine.put("$tempDir", this.tempDir);

        // $test
        engine.put("$test", this.currentTest);

        // $writeMacroOutput (deprecated)
        engine.put("$writeMacroOutput", new BiConsumer<String, Object>() {
            @Override
            public void accept(String valName, Object value) {
                actor.lastMacroOutput.put(valName, value);
            }
        });

        // $writeOutput
        // This function is being populated dynamically by each script
        // action when it executes.
        engine.put("$writeOutput", null);

        return engine;
    }

    /**
     * Convert old test action names to their new name in order to avoid
     * breaking old tests.
     */
    private String curateActionName(String actionName) {
        String newActionName = actionName
                .replaceAll("^dtest.np6actions.", "com.mcd.opentest.np6.")
                .replaceAll("^dtest.mobileactions.", "org.getopentest.appium.")
                .replaceAll("^dtest.seleniumactions.", "org.getopentest.selenium.")
                .replaceAll("^dtest.actions.", "org.getopentest.actions.");
        return newActionName;
    }

    /**
     * Deal with recoverable error conditions related to test action arguments,
     * like deprecated arguments.
     */
    private void curateArguments(Map<String, Object> args) {
        // executeIf was deprecated in favor of $if
        if (args.containsKey("executeIf")) {
            args.put("$if", args.get("executeIf"));
            args.remove("executeIf");
        }

        // optional was deprecated in favor of $optional
        if (args.containsKey("optional")) {
            args.put("$optional", args.get("optional"));
            args.remove("optional");
        }
    }

    /**
     * Replace invalid property names for map objects that are about to be
     * converted into JSON and later on inserted into a database like MongoDB.
     */
    private Map<String, Object> curatePropertyNames(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();

        for (String propName : map.keySet()) {
            String newName = propName;

            if (propName.startsWith("$")) {
                // Escaping the "$" character in the replacement string is
                // necessary because otherwise it's going to be considered
                // a reference to a regex group
                newName = propName.replaceFirst("\\$", "d\\$");
            }

            result.put(newName, map.get(propName));
        }

        return result;
    }

    /**
     * Converts a value from a Java type to a JavaScript native type, by first
     * serializing it to a JSON string and then using JSON.parse. This is
     * necessary to allow the use of JS APIs on arrays and objects (e.g.
     * Array.prototype.join, Object.keys, etc.), instead of requiring test
     * automation developers to work with those values using Java APIs. Types
     * that map to native JS primitive values (number, string, etc.) apparently
     * cannot be converted to native JS objects.
     */
    private Object convertToJavaScriptType(Object value) {
        try {
            // Check if the value is a List or Map, but exclude ScriptObjectMirror
            // instances, because they are already JS native types
            if ((value instanceof List || value instanceof Map)
                    && !(value instanceof ScriptObjectMirror)) {

                Gson gson = new Gson();
                String json = gson.toJson(value);
                json = json.replace("\\", "\\\\");
                return this.scriptEngine.eval(String.format("JSON.parse('%s')", json));
            } else {
                return value;
            }
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to convert value %s to a native JavaScript type",
                    value), ex);
        }
    }

    /**
     * Converts ScriptObjectMirror instances to their equivalent standard Java
     * types. Objects that are already Java types are returned unchanged.
     */
    private Object convertToJavaType(Object inputObj) {
        if (inputObj instanceof ScriptObjectMirror) {
            ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) inputObj;
            if (scriptObjectMirror.isArray()) {
                List<Object> list = new ArrayList<>();
                for (Map.Entry<String, Object> entry : scriptObjectMirror.entrySet()) {
                    list.add(convertToJavaType(entry.getValue()));
                }
                return list;
            } else {
                Map<String, Object> map = new HashMap<>();
                for (Map.Entry<String, Object> entry : scriptObjectMirror.entrySet()) {
                    map.put(entry.getKey(), convertToJavaType(entry.getValue()));
                }
                return map;
            }
        } else {
            return inputObj;
        }
    }

    /**
     * Evaluates the "dataSet" property of a data-driven test and returns it as
     * an object of type List.
     */
    private List evalDataSet(Object dataSetSource) {
        if (dataSetSource != null) {
            if (dataSetSource instanceof String) {
                String expression = (String) dataSetSource;
                String cleanedExpression = expression.replaceAll("^\\s*\\$script\\s*", "");
                Object evaledObj;
                try {
                    evaledObj = this.scriptEngine.eval(cleanedExpression);
                } catch (Exception ex) {
                    throw new RuntimeException(String.format(
                            "Failed to evaluate the \"dataSet\" property of the data-driven test as a JavaScript expression. The expression we evaluated was >>> %s <<<.",
                            cleanedExpression), ex);
                }
                Object dataSetObj = this.convertToJavaType(evaledObj);
                if (dataSetObj instanceof List) {
                    return (List) dataSetObj;
                } else {
                    throw new RuntimeException(String.format(
                            "The \"dataSet\" property of the data-driven test did not evaluate to a collection type. The expression we evaluated was >>> %s <<<.",
                            cleanedExpression));
                }
            } else if (dataSetSource instanceof List) {
                return (List) dataSetSource;
            } else {
                throw new RuntimeException(String.format(
                        "The \"dataSet\" property of the data-driven test is not a collection type. The value we found was >>> %s <<<.",
                        Factory.getGson().toJson(dataSetSource)));
            }
        } else {
            return null;
        }
    }

    /**
     * Evaluates the script files provided in the "includes" property of a test
     * file.
     */
    private void evalIncludes(Object includesSource) {
        RuntimeException invalidIncludesException = new RuntimeException(String.format(
                "The \"includes\" property of the test has to be a single string, "
                + "or a list of strings. The value we found was >>> %s <<<.",
                Factory.getGson().toJson(includesSource)));

        if (includesSource != null) {
            List<String> includesList;

            if (includesSource instanceof String) {
                String expression = (String) includesSource;
                Object evaledObj;
                try {
                    evaledObj = this.evalString(expression);
                } catch (Exception ex) {
                    throw new RuntimeException(String.format(
                            "Failed to evaluate \"included\" JS file(s) as a JavaScript "
                            + "expression. The expression we evaluated was >>> %s <<<.",
                            includesSource), ex);
                }
                Object includesSetObj = this.convertToJavaType(evaledObj);
                if (includesSetObj instanceof String) {
                    includesList = Arrays.asList((String) includesSetObj);
                } else if (includesSetObj instanceof List) {
                    includesList = (List) includesSetObj;
                } else {
                    throw invalidIncludesException;
                }
            } else if (includesSource instanceof List) {
                includesList = (List) includesSource;
            } else if (includesSource instanceof ScriptObjectMirror) {
                ScriptObjectMirror includesSom = (ScriptObjectMirror) includesSource;
                includesList = includesSom.values().stream()
                        .map(s -> s.toString())
                        .collect(Collectors.toList());
            } else {
                throw invalidIncludesException;
            }

            if (includesList != null) {
                includesList.forEach((include) -> {
                    includeScriptFile(include);
                });
            } else {
                throw invalidIncludesException;
            }
        }
    }

    /**
     * Parse JSON data using the JS engine and return the corresponding JS
     * native value.
     */
    public Object parseJsonToNativeJsType(String jsonData) {
        try {
            String escapedJson = StringEscapeUtils.escapeJson(jsonData);
            String jsCode = String.format("JSON.parse(\"%s\")", escapedJson);
            Object jsNativeType = this.scriptEngine.eval(jsCode);
            return jsNativeType;
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "An error was encountered while parsing JSON data. The JSON data was: %s",
                    jsonData), ex);
        }
    }

    /**
     * Evaluate an object as JavaScript (if applicable) and return the evaluated
     * value. In the case of maps or arrays, we evaluate each one of their
     * element's values recursively.
     *
     * @param objValue The object to be evaluated.
     * @param skipProps A string array containing property names whose values
     * will not be evaluated. This is useful for deferring evaluation for
     * "special" properties (e.g. "$localData", "$sharedData", etc.) whose
     * values need to be evaluated only after a test action executes.
     */
    private Object evalObject(Object objValue, String[] skipProps) {
        if (objValue == null) {
            return null;
        }

        if (objValue instanceof Map) {
            Set<String> skipPropsSet = skipProps != null
                    ? new HashSet<String>(Arrays.asList(skipProps))
                    : null;
            Map<String, Object> mapValue = (Map) objValue;
            for (Map.Entry<String, Object> entry : mapValue.entrySet()) {
                if (skipPropsSet != null && skipPropsSet.contains(entry.getKey())) {
                    continue;
                }

                try {
                    Object result = evalObject(entry.getValue(), null);
                    mapValue.put(entry.getKey(), result);
                } catch (Exception ex) {
                    throw new RuntimeException(String.format(
                            "There was an error while evaluating property \"%s\"",
                            entry.getKey()), ex);
                }
            }
            return mapValue;
        } else if (objValue instanceof ArrayList) {
            // Evaluate all elements of the array
            ArrayList arrayValue = (ArrayList) objValue;
            for (int index = 0; index < arrayValue.size(); ++index) {
                arrayValue.set(index, evalObject(arrayValue.get(index), null));
            }
            return arrayValue;
        } else if (objValue instanceof String) {
            Object returnValue = (String) objValue;
            boolean evaluationFinished = false;
            int iterationCount = 0;
            Object evalResult;

            // Evaluate the JS expression, then evaluate the result of the
            // evaluation, until we no longer have a JS expression, but only
            // an ordinary string
            while (!evaluationFinished) {
                ++iterationCount;

                if (iterationCount >= 100) {
                    throw new RuntimeException(String.format(
                            "We iterated %s times while evaluating a JavaScript data property. We are giving up, since there is likely a circular dependency between data properties.",
                            iterationCount));
                }

                try {
                    if (returnValue instanceof String) {
                        // Dollar-prefixed values are normally interpreted as JS
                        // expressions. The "$string " prefix can be used to declare
                        // the value is a string and avoid evaluation.
                        if (((String) returnValue).matches("^\\s*\\$string\\s*.*")) {
                            return ((String) returnValue).replaceAll("^\\s*\\$string\\s*", "");
                        }

                        evalResult = evalString((String) returnValue);
                    } else {
                        evalResult = evalObject(returnValue, null);
                    }
                } catch (StackOverflowError e) {
                    throw new RuntimeException(String.format(
                            "We got a StackOverflowError while evaluating a data property. The property's value was \"%s\". This is typically caused by a circular dependency (e.g. property1 refercencing property2, which, in turn, references property1).",
                            objValue
                    ));
                }

                if (evalResult == returnValue) {
                    evaluationFinished = true;
                }

                returnValue = evalResult;
            }

            return returnValue;
        } else {
            return objValue;
        }
    }

    /**
     * Evaluate JS code and return the result.
     */
    public Object evalScript(String script) {
        // Remove the $script syntax from the beginning of the string, if present
        String cleanedExpression = script.replaceAll("^\\s*\\$script\\s*", "");

        try {
            // Check whether the string represents an object literal
            if (cleanedExpression.matches("^\\s*\\{[\\s\\S]*\\}\\s*$")) {
                // We must enclose the expression in parantheses, so that object
                // literals are propely evaluated
                return this.scriptEngine.eval(String.format("(%s)", cleanedExpression));
            } else {
                return this.scriptEngine.eval(cleanedExpression);
            }
        } catch (IntentionalFailException ex1) {
            throw ex1;
        } catch (Exception ex2) {
            // Try to find the line number that the JS error happened at by looking
            // for the last frame in the stack trace with a file name of "<eval>"
            String lineNoStr = "?";
            for (StackTraceElement frame : ex2.getStackTrace()) {
                if (frame.getFileName() == "<eval>") {
                    lineNoStr = Integer.toString(frame.getLineNumber());
                }
            }

            throw new RuntimeException(String.format(
                    "Failed to evaluate JavaScript code at line number %s. The script content was: \n%s",
                    lineNoStr,
                    cleanedExpression), ex2);
        }
    }

    private Object evalScriptFile(String scriptFileFullPath) throws ScriptException, FileNotFoundException {
        Path scriptPath = Paths.get(scriptFileFullPath);

        if (Files.exists(scriptPath)) {
            this.currentScript = scriptPath.toString();
        }

        return this.scriptEngine.eval(new FileReader(scriptPath.toString()));
    }

    /**
     * Used for evaluating the values of action arguments and data properties.
     * If the specified string is a JavaScript expression (dollar-prefixed
     * string), it evaluates it and returns the result. Otherwise, it returns
     * the plain string value unchanged.
     */
    private Object evalString(String expression) {
        // A string is considered to be a JS expression if it starts with
        // "$script" or any dollar-prefixed identifier
        if (expression.matches("(?s)^[\\s\\n]*(\\$[a-zA-Z]\\w+).*")) {
            // Remove the $script syntax from the beginning of the string, if present
            String cleanedExpression = expression.replaceAll("^\\s*\\$script\\s*", "");

            return evalScript(cleanedExpression);
        } else {
            // This is not JS code, so we just return the original string
            return expression;
        }
    }

    /**
     * Executes an action given the action instance and returns the output
     * values produced by the action.
     */
    private Map<String, Object> executeAction(TestAction action, Map<String, Object> args) {
        ILogger regularLogger = Logger.getLogger();
        if (args == null) {
            args = new HashMap<String, Object>();
        }

        boolean noLogs = args.containsKey("$noLogs") && args.get("$noLogs").equals(true);

        try {
            if (args.containsKey("$if") && args.get("$if").equals(false)) {
                this.currentActionInfo.result = "skipped";
                Logger.info(String.format("Skipping conditional action %s since the value of the $if argument was false.", action.getClass().getName()));
                return new HashMap<>();
            }

            action.setSession(new TestSessionStatus(this.currentSessionStatus));
            action.setActor(this);

            this.currentAction = action;

            // Populate the test action's arguments
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                action.writeArgument(entry.getKey(), entry.getValue());
            }

            Logger.trace(String.format("Initializing action %s...",
                    action.getClass().getName()));
            action.initialize();

            String argsDescription = noLogs
                    ? "" : getArgsStringDescription(action.getArgs());

            Logger.info(String.format("Executing action %s %s...",
                    action.getClass().getName(),
                    argsDescription
            ));

            if (noLogs) {
                NullLogger nullLogger = new NullLogger();
                Logger.setLogger(nullLogger);
                action.setLogger(nullLogger);
            } else {
                action.setLogger(this.log);
            }

            action.run();
        } catch (Exception ex) {
            Logger.setLogger(regularLogger);
            if (action.readBooleanArgument("$optional", false) == true) {
                Logger.info(String.format(
                        "Optional action %s failed. Execution will continue.",
                        action.getClass().getName()));
            } else {
                throw ex;
            }
        } finally {
            // Process the $screenshot argument. We also check for the value
            // "after" for now, for backwards compatibility.
            boolean takeScreenshotAfter = action.hasArgument("$screenshot")
                    && action.readStringArgument("$screenshot").equalsIgnoreCase("after");
            takeScreenshotAfter = takeScreenshotAfter || action.readBooleanArgument("$screenshot", false);
            if (takeScreenshotAfter) {
                File screenshot = this.takeScreenShot(action, this.currentSegmentNo, this.currentActionNo, "after");
                if (screenshot != null) {
                    this.currentActionInfo.screenshot = screenshot.getName();
                }
            }
        }

        Map<String, Object> outputValues = new TrimmableMap(action.getOutput());

        // Output values should not be written to the log if the $noLogs = true
        // argument was passed.
        if (!noLogs && (outputValues.size() > 0)) {
            Gson gson = Factory.getGsonBuilder(this.config)
                    .registerTypeAdapter(ScriptObjectMirror.class, new ScriptObjectMirrorSerializer(true))
                    .serializeNulls()
                    .create();
            StringBuilder outputValuesStr = new StringBuilder();

            for (Object key : outputValues.keySet()) {
                String outputValueJson;

                try {
                    outputValueJson = gson.toJson(outputValues.get(key));
                    int maxLogEntryLength = config.getInteger("maxLogEntryLength", 50000);
                    if (outputValueJson.length() > maxLogEntryLength) {
                        outputValueJson = outputValueJson.substring(0, maxLogEntryLength + 1)
                                + " <<VALUE_WAS_TRUNCATED>>";
                    }
                    outputValuesStr.append(String.format("\n\t>>>>> %s = %s", key, outputValueJson));
                } catch (Exception ex) {
                    log.warning(String.format("Failed serializing output value %s to JSON.", key), ex);
                }
            }

            Logger.debug(String.format("The output values from action %s were: %s",
                    action.getClass().getName(),
                    outputValuesStr.toString()));
        }

        Logger.setLogger(regularLogger);

        return outputValues;
    }

    /**
     * Executes an action given the action's Java class name and returns the
     * output values produced by the action.
     */
    private Map<String, Object> executeActionByClassName(String actionClassName, Map<String, Object> args) {
        actionClassName = curateActionName(actionClassName);
        this.curateArguments(args);

        try {
            Class<?> actionClass;
            actionClass = Class.forName(actionClassName);

            if (TestAction.class.isAssignableFrom(actionClass)) {
                TestAction actionInstance = (TestAction) actionClass.getConstructor().newInstance();

                executeAction(actionInstance, args);

                this.lastActionOutput = actionInstance.getOutput();
                return actionInstance.getOutput();
            } else {
                throw new RuntimeException(String.format(
                        "Class %s is not a test action class and cannot be used in test definitions",
                        actionClassName));
            }
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
            throw new RuntimeException(String.format(
                    "Failed to find test action class \"%s\". Most common causes to watch for are "
                    + "capitalization mistakes (e.g. using \"some.package.sampleAction\" "
                    + "instead \"some.package.SampleAction\") or spelling mistakes. If these "
                    + "look good, make sure the JAR file where the test action class is implemented "
                    + "exists in the CLASSPATH. A less common cause for this error is having a "
                    + "dependency conflict that prevents the test action class from instantiating.",
                    actionClassName), ex);
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(String.format(
                    "Failed to instantiate action class \"%s\"",
                    actionClassName), ex);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Executes an action given the action's definition and returns the output
     * values produced by the action.
     */
    private Map<String, Object> executeActionByDef(TestDefAction actionDef) {
        long startTimeNano = 0;
        long endTimeNano = 0;

        TestActionInfo localActionInfo = new TestActionInfo();
        boolean isCheckpoint = false;
        Map<String, Object> actionArgs = null;

        try {
            localActionInfo.actorType = this.actorType;
            localActionInfo.macroStack = new ArrayList(this.macroStack);
            localActionInfo.result = null;
            localActionInfo.segment = this.currentSegmentNo;

            // Added for backward compatibility to support using the "type" property
            // as an alias to "action" but will be removed eventually
            if (actionDef.action == null) {
                actionDef.action = actionDef.type;
                actionDef.type = null;
            }

            actionArgs = (Map) evalObject(
                    actionDef.args,
                    new String[]{"$localData", "$sessionData", "$sharedData"});
            if (actionArgs == null) {
                actionArgs = new HashMap<>();
            }

            localActionInfo.args = new TrimmableMap(actionArgs);
            isCheckpoint = TypeConverter.toBooolean(actionArgs.get("$checkpoint"), false);
            localActionInfo.isCheckpoint = isCheckpoint;

            String description;
            if (actionDef.description != null && !actionDef.description.trim().isEmpty()) {
                description = actionDef.description.trim();
            } else if (actionDef.action != null && !actionDef.action.trim().isEmpty()) {
                description = actionDef.action.trim();
            } else if (actionDef.script != null) {
                description = ScriptAction.class.getName();
            } else if (actionDef.macro != null) {
                description = actionDef.macro.trim();
            } else {
                description = "(No description provided)";
            }

            localActionInfo.description
                    = actionArgs.getOrDefault("$description", description).toString();

            Map<String, Object> outputValues = null;

            // The currentActionInfo member allows other methods to get
            // access to the action info object to read or write to it
            this.currentActionInfo = localActionInfo;

            startTimeNano = System.nanoTime();

            if (actionDef.script != null) {
                // SCRIPT ACTION
                localActionInfo.action = ScriptAction.class.getName();
                actionDef.action = ScriptAction.class.getName();
                ScriptAction scriptAction = new ScriptAction(this, actionDef.script);
                outputValues = this.executeAction(scriptAction, actionArgs);
            } else if (actionDef.macro != null) {
                // MACRO ACTION
                localActionInfo.action = MacroAction.class.getName();
                localActionInfo.macro = actionDef.macro;
                actionDef.action = MacroAction.class.getName();
                outputValues = this.executeMacroActionByName(actionDef.macro, actionArgs);
            } else if (actionDef.action != null) {
                // REGULAR ACTION
                localActionInfo.action = actionDef.action;
                outputValues = this.executeActionByClassName(actionDef.action, actionArgs);
            }

            endTimeNano = System.nanoTime();

            // Evaluate and publish values in the test's shared data. This code
            // section is just for backward compatibility, since the correct way
            // to share data is to use the $sharedData argument.
            if (actionDef.sharedData != null) {
                Map<String, Object> sharedData = new HashMap<>();

                for (Map.Entry<String, Object> entry : actionDef.sharedData.entrySet()) {
                    sharedData.put(entry.getKey(), this.evalObject(entry.getValue(), null));
                }

                publishSharedData(sharedData);
            }

            if (actionDef.args != null) {
                // Evaluate and publish values in the session data store
                if (actionDef.args.containsKey("$sessionData")) {
                    Object sessionDataObj = actionDef.args.get("$sessionData");
                    if (sessionDataObj instanceof Map) {
                        Map<String, Object> sharedDataOriginal = (Map) sessionDataObj;
                        Map<String, Object> sessionData = new HashMap<String, Object>();

                        for (Map.Entry<String, Object> entry : sharedDataOriginal.entrySet()) {
                            sessionData.put(entry.getKey(), this.evalObject(entry.getValue(), null));
                        }

                        publishSessionData(sessionData);
                    }
                }

                // Evaluate and publish values in the shared (test-scoped) data store
                if (actionDef.args.containsKey("$sharedData")) {
                    Object sharedDataObj = actionDef.args.get("$sharedData");
                    if (sharedDataObj instanceof Map) {
                        Map<String, Object> sharedDataOriginal = (Map) sharedDataObj;
                        Map<String, Object> sharedData = new HashMap<String, Object>();

                        for (Map.Entry<String, Object> entry : sharedDataOriginal.entrySet()) {
                            sharedData.put(entry.getKey(), this.evalObject(entry.getValue(), null));
                        }

                        publishSharedData(sharedData);
                    }
                }

                // Evaluate and publish values in the local (actor-scoped) data store
                if (actionDef.args.containsKey("$localData")) {
                    Object localDataObj = actionDef.args.get("$localData");
                    if (localDataObj instanceof Map) {
                        Map<String, Object> localData = new TrimmableMap((Map) localDataObj);

                        for (Map.Entry<String, Object> entry : localData.entrySet()) {
                            Object result = this.evalObject(entry.getValue(), null);
                            localData.put(entry.getKey(), result);
                            this.localData.put(entry.getKey(), result);
                        }

                        Gson gson = Factory.getGsonBuilder(this.config)
                                .registerTypeAdapter(
                                        ScriptObjectMirror.class,
                                        new ScriptObjectMirrorSerializer(false))
                                .create();
                        String jsonData = gson.toJson(localData);
                        Logger.debug(String.format("Published local data: %s", jsonData));
                    }
                }
            }

            if (outputValues == null) {
                outputValues = new HashMap<String, Object>();
            }

            localActionInfo.result = localActionInfo.result != null
                    ? localActionInfo.result
                    : "passed";

            return outputValues;
        } catch (Exception ex) {
            localActionInfo.result = "failed";
            localActionInfo.stackTrace = BaseLogger.getStackTrace(ex);

            // We don't take screenshots for macros, since the screenshot was
            // already taken for the action that failed within the macro
            if (this.currentAction != null && (localActionInfo.action != null) && !(localActionInfo.action.equals(MacroAction.class.getName()))) {
                File screenshotFile = this.takeScreenShot(this.currentAction, this.currentSegmentNo, this.currentActionNo, "error");
                if (screenshotFile != null) {
                    localActionInfo.screenshot = screenshotFile.getName();
                }
            }

            String actionType = actionDef.action != null
                    ? actionDef.action
                    : actionDef.macro != null
                            ? actionDef.macro
                            : actionDef.script != null ? "of type \"script\"" : "?";

            String actionArguments;
            if (this.currentAction != null) {
                actionArguments = getArgsStringDescription(actionArgs);
            } else {
                actionArguments = "(arguments not available because the current action instance was not populated)";
            }

            String errorMessage = String.format(
                    "Failed executing action %s %s",
                    actionType, actionArguments);
            if (isCheckpoint) {
                throw wrapCheckpointException(ex);
            } else {
                if (ex instanceof IntentionalFailException) {
                    throw (IntentionalFailException) ex;
                } else {
                    throw new RuntimeException(errorMessage, ex);
                }
            }
        } finally {
            if (endTimeNano <= 0) {
                endTimeNano = System.nanoTime();
            }

            if (startTimeNano > 0 && endTimeNano > 0) {
                localActionInfo.durationMs = (int) ((endTimeNano - startTimeNano) / 1000000.0);
            }

            this.currentSegmentActions.add(localActionInfo);
            this.currentActionInfo = null;
        }
    }

    private Map<String, Object> executeMacroActionByDef(
            String macroFullName,
            MacroDefinition macroDef,
            Map<String, Object> macroArgs) throws Exception {

        try {
            this.macroStack.push(macroFullName);

            Map<String, Object> previousMacroArgs = this.macroArgs;
            this.macroArgs = macroArgs;

            Map<String, Object> previousMacroOutput = this.lastMacroOutput;
            this.lastMacroOutput = new HashMap<>();

            if (macroArgs == null) {
                macroArgs = new HashMap<String, Object>();
            }

            if (macroArgs.containsKey("$if") && macroArgs.get("$if") != Boolean.TRUE) {
                Logger.info(String.format("Skipping conditional macro action %s", macroDef.fullName));
                this.currentActionInfo.result = "skipped";
                return new HashMap<>();
            }

            Logger.info(String.format("Executing macro %s %s...",
                    macroDef.fullName,
                    getArgsStringDescription(macroArgs)));

            evalIncludes(macroDef.includes);

            // Execute the actions for the current macro, in the order
            // they appear in the macro definition file
            if (macroDef.actions != null) {
                for (TestDefAction actionDef : macroDef.actions) {
                    try {
                        // Added for backward compatibility to support using the "type" property
                        // as an alias to "action" but will be removed eventually
                        if (actionDef.action == null) {
                            actionDef.action = actionDef.type;
                            actionDef.type = null;
                        }

                        Logger.info(String.format("Evaluating action %s/%s in macro %s%s...",
                                macroDef.actions.indexOf(actionDef) + 1,
                                macroDef.actions.size(),
                                macroDef.fullName,
                                actionDef.description != null
                                        ? String.format(" (%s)", actionDef.description)
                                        : ""));
                        this.lastActionOutput = executeActionByDef(actionDef);
                    } catch (Exception ex) {
                        Gson gson = Factory.getGsonBuilder().setPrettyPrinting().create();
                        String actionDefJson = gson.toJson(actionDef);

                        if (ex instanceof IntentionalFailException) {
                            throw (IntentionalFailException) ex;
                        } else if (ex instanceof CheckpointException) {
                            Throwable cause = ex.getCause();
                            if (cause instanceof IntentionalFailException) {
                                String userDefinedMessage = ex.getMessage();
                                String userDefinedMessageSuffix
                                        = (userDefinedMessage != null) && (!userDefinedMessage.isEmpty())
                                        ? String.format("The user-defined message was: \"%s\".", cause.getMessage())
                                        : "No user-defined message was passed.";
                                String errorMessage = String.format(
                                        "The action was failed intentionally using the $fail() function. %s",
                                        userDefinedMessageSuffix);
                                this.log.error(errorMessage);
                            } else {
                                this.log.error(String.format(
                                        "There was an error while executing checkpoint action no. %s (%s) in macro "
                                        + "%s.\nThe action definition was:\n%s",
                                        macroDef.actions.indexOf(actionDef) + 1,
                                        actionDef.action,
                                        macroDef.fullName,
                                        actionDefJson), ex);
                            }

                            Integer subtestIndex = null;
                            if (this.currentDataSet != null) {
                                subtestIndex = this.currentSessionStatus.currentDataRecordIndex;
                            }
                            reportCheckpointFailed(this.currentSessionStatus.id, this.currentSessionStatus.currentTestIndex, subtestIndex);
                        } else {
                            throw new RuntimeException(String.format(
                                    "There was an error while executing action no. %s (%s) in macro "
                                    + "%s.\nThe action definition was:\n%s",
                                    macroDef.actions.indexOf(actionDef) + 1,
                                    actionDef.action,
                                    macroDef.fullName,
                                    actionDefJson), ex);
                        }
                    }
                }
            }

            // Remember the current macro output, since we'll
            // have to return that to the caller later
            Map<String, Object> currentMacroOutput = this.lastMacroOutput;

            // Replace the macro state variables with their previous content. This
            // is necessary to properly handle the macro-calling-macro use case.
            this.macroArgs = previousMacroArgs;
            this.lastMacroOutput = previousMacroOutput;
            this.lastActionOutput = currentMacroOutput;

            if (currentMacroOutput.size() > 0) {
                Gson gson = Factory.getGsonBuilder().serializeNulls().create();
                Logger.debug(String.format("The output values from macro %s were: %s",
                        macroDef.fullName,
                        gson.toJson(currentMacroOutput)));
            }

            return currentMacroOutput;
        } finally {
            this.macroStack.pop();
        }
    }

    private Map<String, Object> executeMacroActionByName(String macroFullName, Map<String, Object> macroArgs) throws Exception {
        try {
            String macroRelativePath = macroFullName.replaceAll("[\\.\\\\]", "/").trim();
            MacroDefinition macroDef = this.getMacroDefinition(macroRelativePath);
            macroDef.fullName = macroRelativePath;
            return executeMacroActionByDef(macroFullName, macroDef, macroArgs);
        } catch (Exception ex) {
            if (ex instanceof IntentionalFailException) {
                throw (IntentionalFailException) ex;
            } else if (ex instanceof CheckpointException
                    && ex.getCause() instanceof IntentionalFailException) {
                throw (CheckpointException) ex;
            } else {
                throw new Exception(String.format(
                        "There was an error executing macro action %s %s",
                        macroFullName,
                        getArgsStringDescription(macroArgs)), ex);
            }
        }
    }

    /**
     * Execute an HTTP request for the specified number of times, until we get
     * the expected HTTP status code.
     */
    private void executeRequestWithRetries(HttpRequest request, int expectedStatusCode, int retries) {
        int retriesLeft = retries;
        while (retriesLeft > 0) {
            try {
                request.execute();
                if (request.getResponseStatusCode() == expectedStatusCode) {
                    break;
                } else {
                    retriesLeft--;
                }
            } catch (Exception ex) {
                retriesLeft--;
            }

            if (retriesLeft < 0) {
                throw new RuntimeException(String.format(
                        "Abandoning request %s after trying for %s times. Last HTTP "
                        + "status code received was %s.",
                        request.getUrl(),
                        retries,
                        request.getResponseStatusCode()));
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
        }
    }

    private void executeTestSegment(int segmentNumber) throws Exception {
        this.currentSegmentNo = segmentNumber;

        // Notify the sync service that the segment execution is starting
        HttpRequestOptions options = new HttpRequestOptions(
                String.format("%s/api/session/%s/actor/%s/test/%s/segment/%s",
                        this.syncServerUrl,
                        this.currentSessionStatus.id,
                        this.actorId,
                        this.currentSessionStatus.currentTestIndex,
                        segmentNumber),
                HttpVerb.PUT);
        ServerRequest segmentStatusRequest = new ServerRequest(options, config);
        segmentStatusRequest.setContent("{\"status\":\"started\",\"result\":\"pending\"}", ContentType.APPLICATION_JSON);
        segmentStatusRequest.execute();

        this.currentSegmentActions = new ArrayList<TestActionInfo>();

        try {
            TestDefSegment segment = null;

            for (TestDefActor currentActor : currentTest.actors) {
                if (currentActor.actorType.equals(this.actorType)) {
                    for (TestDefSegment currentSegment : currentActor.segments) {
                        if (currentSegment.segment.equals(segmentNumber)) {
                            segment = currentSegment;
                            break;
                        }
                    }
                }

                if (segment != null) {
                    break;
                }
            }

            if (segment != null && segment.actions != null) {
                log.info(String.format("Executing segment %s of test %s/%s...",
                        segmentNumber,
                        this.currentSessionStatus.currentTestPath,
                        this.currentSessionStatus.currentTestName));

                // Execute the actions for the current segment, in the order
                // they appear in the test definition file
                for (TestDefAction actionDef : segment.actions) {
                    this.currentActionNo = segment.actions.indexOf(actionDef) + 1;
                    String actionName = actionDef.action != null
                            ? actionDef.action
                            : actionDef.macro != null
                                    ? actionDef.macro
                                    : actionDef.script != null ? "script action" : "?";

                    try {
                        log.info(String.format("Evaluating action %s/%s (%s) in segment %s%s...",
                                segment.actions.indexOf(actionDef) + 1,
                                segment.actions.size(),
                                actionName,
                                segmentNumber,
                                actionDef.description != null
                                        ? String.format(" (%s)", actionDef.description)
                                        : ""));
                        this.lastActionOutput = executeActionByDef(actionDef);
                    } catch (Throwable ex) {
                        boolean lastActionIsCheckpoint = false;
                        if (ex instanceof CheckpointException) {
                            ex = ex.getCause();
                            lastActionIsCheckpoint = true;
                        }

                        if (ex instanceof IntentionalFailException) {
                            String userDefinedMessage = ex.getMessage();
                            String userDefinedMessageSuffix
                                    = (userDefinedMessage != null) && (!userDefinedMessage.isEmpty())
                                    ? String.format("The user-defined message was: \"%s\".", ex.getMessage())
                                    : "No user-defined message was passed.";
                            String errorMessage = String.format(
                                    "The action was failed intentionally using the $fail() function. %s",
                                    userDefinedMessageSuffix);

                            if (lastActionIsCheckpoint) {
                                Integer subtestIndex = null;
                                if (this.currentDataSet != null) {
                                    subtestIndex = this.currentSessionStatus.currentDataRecordIndex;
                                }
                                reportCheckpointFailed(this.currentSessionStatus.id, this.currentSessionStatus.currentTestIndex, subtestIndex);
                                this.log.error(errorMessage);
                            } else {
                                throw new RuntimeException(errorMessage);
                            }
                        } else {
                            Gson gson = Factory.getGsonBuilder().setPrettyPrinting().create();
                            String actionDefJson = gson.toJson(actionDef);

                            String errorMessage = String.format(
                                    "There was an error while executing action no. %s (%s) in segment %s of "
                                    + "test %s/%s.\nThe action definition was:\n%s",
                                    segment.actions.indexOf(actionDef) + 1,
                                    actionName,
                                    segmentNumber,
                                    this.currentSessionStatus.currentTestPath,
                                    this.currentSessionStatus.currentTestName,
                                    actionDefJson);

                            if (lastActionIsCheckpoint) {
                                Integer subtestIndex = null;
                                if (this.currentDataSet != null) {
                                    subtestIndex = this.currentSessionStatus.currentDataRecordIndex;
                                }
                                reportCheckpointFailed(this.currentSessionStatus.id, this.currentSessionStatus.currentTestIndex, subtestIndex);
                                this.log.error(errorMessage, ex);
                            } else {
                                throw new RuntimeException(errorMessage, ex);
                            }
                        }
                    }
                } // for actionDef in segmentActions
            }

            // Notify the sync service that the segment execution was completed successfully
            Map<String, Object> segmentInfo = new HashMap<String, Object>();
            segmentInfo.put("actions", this.currentSegmentActions);
            segmentInfo.put("status", "completed");
            segmentInfo.put("result", "passed");

            this.currentSegmentActions.stream().forEach((actioninfo) -> {
                if (actioninfo.args != null) {
                    actioninfo.args = new TrimmableMap(this.curatePropertyNames(actioninfo.args));
                }
            });

            Gson gson = Factory.getGson(this.config);
            String segmentInfoJson = gson.toJson(segmentInfo);
            segmentStatusRequest.setContent(segmentInfoJson, ContentType.APPLICATION_JSON);
            segmentStatusRequest.execute();
        } catch (Exception ex) {
            // Notify the sync service that the segment execution failed
            Map<String, Object> content = new HashMap<String, Object>();
            content.put("actions", this.currentSegmentActions);
            content.put("status", "completed");
            content.put("result", "failed");
            content.put("stackTrace", BaseLogger.getStackTrace(ex));

            this.currentSegmentActions.stream().forEach((actioninfo) -> {
                if (actioninfo.args != null) {
                    actioninfo.args = new TrimmableMap(this.curatePropertyNames(actioninfo.args));
                }
            });

            Gson gson = Factory.getGson(this.config);
            String reqPayload = gson.toJson(content);
            segmentStatusRequest.setContent(reqPayload, ContentType.APPLICATION_JSON);
            segmentStatusRequest.execute();

            Exception newException = new Exception(String.format("Failed executing test %s/%s, segment %s",
                    this.currentSessionStatus.currentTestPath,
                    this.currentSessionStatus.currentTestName,
                    segmentNumber), ex);
            throw newException;
        } finally {
            this.currentAction = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    /**
     * Returns a string description of the arguments provided to a test action,
     * that is intended to be used for logging.
     */
    private String getArgsStringDescription(Map<String, Object> argsMap) {
        String actionArguments;

        if (argsMap != null && argsMap.size() > 0) {
            Gson gson = Factory.getGsonBuilder(this.config)
                    .serializeNulls()
                    .create();
            actionArguments = String.format("with arguments %s",
                    gson.toJson(new TrimmableMap<String, Object>(argsMap)));
        } else {
            actionArguments = "(no arguments were provided)";
        }

        return actionArguments;
    }

    public Config getConfig() {
        return this.config;
    }

    private Encryptor getEncryptor() {
        if (this.encryptor == null) {
            String password = this.config.getString("encryptionPassword", System.getenv("OPENTEST_ENCRYPTION_PASSWORD"));
            if (password == null) {
                throw new RuntimeException(
                        "The decryption operation failed because no password was provided. The password "
                        + "can be defined using the \"encryptionPassword\" parameter in actor.yaml or "
                        + "using the \"OPENTEST_ENCRYPTION_PASSWORD\" environment variable.");
            }
            this.encryptor = new Encryptor(password);
        }

        return this.encryptor;
    }

    public File getOutDir() {
        return this.outDir;
    }

    public File getScreenshotsDir() {
        return this.screenshotsDir;
    }

    /**
     * Queries the sync service for the specified image file.
     */
    public BufferedImage getImage(String fileName) {
        File imageFile = new File(fileName);
        if (imageFile.isAbsolute()) {
            // For an absolute path the file must already exist on disk,
            // so we just read and return it. This is only recommended for
            // troubleshooting and quick POCs during test development. Always
            // use relative paths in production code and let the test actor
            // request the image file from the sync service.
            try {
                return ImageIO.read(imageFile);
            } catch (IOException ex) {
                throw new RuntimeException(String.format(
                        "Failed reading image file %s",
                        fileName), ex);
            }
        } else {
            // A relative path means that the file will be requested from the
            // sync server and cached on disk. Subsequently, the cached version
            // will be used instead, to optimize bandwidth and performance
            Path imagePath = Paths.get(
                    this.outDir.getAbsolutePath(),
                    "images",
                    "cached",
                    fileName);

            int fiveMinutesInSec = 5 * 60;
            int imageCacheTimeoutSec = this.config.getInteger("imageCacheTimeoutSec", fiveMinutesInSec);
            long timeThreshold = System.currentTimeMillis() - (imageCacheTimeoutSec * 1000);
            if (Files.exists(imagePath)
                    && imagePath.toFile().lastModified() > timeThreshold) {

                Logger.trace(String.format(
                        "Using cached version of image file stored at \"%s\"",
                        imagePath.normalize().toString()));
            } else {
                Logger.trace(String.format(
                        "Requesting image file \"%s\" from server",
                        fileName));
                try {
                    InputStream imageStream = getTestAsset("image", fileName);
                    imagePath.toFile().getParentFile().mkdirs();
                    OutputStream outputStream = new FileOutputStream(imagePath.toFile(), false);
                    IOUtils.copy(imageStream, outputStream);
                    outputStream.close();
                    imageStream.close();
                } catch (Exception ex) {
                    throw new RuntimeException(String.format(
                            "Failed reading image file %s from sync server",
                            fileName), ex);
                }
            }

            try {
                return ImageIO.read(imagePath.toFile());
            } catch (IOException ex) {
                throw new RuntimeException(String.format(
                        "Failed reading image file %s from sync server",
                        fileName), ex);
            }
        }
    }

    public File getTempDir() {
        return this.tempDir;
    }

    /**
     * Queries the sync service for the specified test asset file and returns an
     * input stream with the content of that asset.
     */
    public InputStream getTestAsset(String assetType, String partialPath) {
        // URL-encode the partial path
        String urlEncodedPartialPath = null;
        try {
            urlEncodedPartialPath = URLEncoder.encode(partialPath, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(String.format("Failed encoding partial path %s",
                    partialPath), ex);
        }

        String url;
        if (assetType.equals("data") && this.currentSessionStatus.environment != null) {
            // URL-encode the partial path
            String urlEncodedEnv = null;
            try {
                urlEncodedEnv = URLEncoder.encode(this.currentSessionStatus.environment, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(String.format("Failed encoding environment name %s",
                        this.currentSessionStatus.environment), ex);
            }

            url = String.format("%s/api/test-asset?type=%s&path=%s&env=%s",
                    this.syncServerUrl,
                    assetType,
                    urlEncodedPartialPath,
                    urlEncodedEnv);
        } else {
            url = String.format("%s/api/test-asset?type=%s&path=%s",
                    this.syncServerUrl,
                    assetType,
                    urlEncodedPartialPath);
        }

        HttpRequestOptions options = new HttpRequestOptions(url, HttpVerb.GET);
        ServerRequest request = new ServerRequest(options, config);

        int retries = 0;
        int maxRetries = 3;
        
        do {
            retries++;

            try {
                request.execute();
            } catch (Exception ex) {
                String errorMessage = String.format(
                        "An error occured while making the HTTP request to get %s asset %s from the sync service",
                        assetType,
                        partialPath);
                if (retries < maxRetries) {
                    log.warning(errorMessage, ex);
                } else {
                    throw new RuntimeException(errorMessage, ex);
                }
            }

            if (request.getResponseStatusCode() == 200) {
                try {
                    return request.getResponseAsStream();
                } catch (Exception ex) {
                    throw new RuntimeException(String.format(
                            "An error occured while retrieving the HTTP response input stream for %s asset %s from the sync service",
                            assetType,
                            partialPath), ex);
                }
            } else {
                String errorMessage = String.format("Failed to get %s asset %s from the sync service. The HTTP status code was: %s. The HTTP body was: %s",
                        assetType,
                        partialPath,
                        request.getResponseStatusCode(),
                        request.getResponseAsString());

                if (retries < maxRetries) {
                    log.warning(errorMessage);
                } else {
                    throw new RuntimeException(errorMessage);
                }
                
                // Sleep for a bit, to give the server time to recover
                // from a potential performance-related issue
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        } while (retries < maxRetries);

        // This line of code should never be reached; it's
        // just here to make the compiler happy.
        throw new RuntimeException();
    }

    public String getType() {
        return this.actorType;
    }

    /**
     * Get a macro definition from the sync service.
     */
    private MacroDefinition getMacroDefinition(String partialPath) {
        Yaml yaml = new Yaml();
        MacroDefinition def = yaml.loadAs(getTestAsset("macro", partialPath), MacroDefinition.class);
        return def;
    }

    private TestDefinition getTestDefinition(String partialPath) {
        Yaml yaml = new Yaml();
        TestDefinition def = yaml.loadAs(getTestAsset("test", partialPath), TestDefinition.class);

        // Account for usage of obsolete "index" and "step" properties in the segment
        // definition and transfer their value(s) to the "segment" property.
        for (TestDefActor actor : def.actors) {
            if (actor.actor == null) {
                actor.actor = actor.actorType;
            } else if (actor.actorType == null) {
                actor.actorType = actor.actor;
            }

            if (actor.segments == null) {
                actor.segments = actor.steps;
            } else if (actor.steps == null) {
                actor.steps = actor.segments;
            }

            for (TestDefSegment segment : actor.segments) {
                if (segment.segment == null) {
                    segment.segment = segment.step != null
                            ? segment.step
                            : segment.index;

                    segment.step = segment.index = segment.segment;
                }
            }
        }
        return def;
    }

    private SessionStatusResponse getTestSessionStatus() {
        try {
            HttpRequestOptions options = new HttpRequestOptions(
                    String.format("%s/api/session/%s/status",
                            this.syncServerUrl,
                            this.currentSessionStatus.id),
                    HttpVerb.GET);
            ServerRequest request = new ServerRequest(options, config);

            request.execute();
            int statusCode = request.getResponseStatusCode();

            if (statusCode < 300) {
                String responseString = request.getResponseAsString();
                if (responseString != null) {
                    JsonElement responseElement = new JsonParser().parse(responseString);
                    JsonObject responseObj = responseElement.getAsJsonObject();
                    SessionStatusResponse sessionStatus = new SessionStatusResponse();
                    sessionStatus.status = responseObj.get("status").getAsString();
                    if (sessionStatus.status.equals("started")) {
                        sessionStatus.currentTestIndex = responseObj.get("currentTestIndex").getAsInt();
                        if (responseObj.has("currentStepIndex")) {
                            sessionStatus.currentStepIndex = responseObj.get("currentStepIndex").getAsInt();
                            sessionStatus.currentSegmentIndex = sessionStatus.currentStepIndex;
                        } else if (responseObj.has("currentSegmentIndex")) {
                            sessionStatus.currentStepIndex = responseObj.get("currentSegmentIndex").getAsInt();
                            sessionStatus.currentSegmentIndex = sessionStatus.currentStepIndex;
                        }

                        if (responseObj.has("currentDataRecordIndex")) {
                            try {
                                sessionStatus.currentDataRecordIndex = responseObj.get("currentDataRecordIndex").getAsInt();
                            } catch (Exception ex) {
                                sessionStatus.currentDataRecordIndex = -1;
                            }
                        }

                        if (responseObj.has("environment")) {
                            sessionStatus.environment = responseObj.get("environment").getAsString();
                        }

                        sessionStatus.currentIteration = responseObj.get("currentIteration").getAsInt();
                        sessionStatus.currentTestPath = responseObj.get("currentTestPath").getAsString();
                        sessionStatus.currentTestName = responseObj.get("currentTestName").getAsString();
                    }

                    return sessionStatus;
                }
            } else if (statusCode == 404) {
                // When we receive a 404, it means the session died
                abandonSession();
            }
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to get or parse status data for session %s",
                    this.currentSessionStatus.id), ex);
        }

        return null;
    }

    private void includeScriptFile(String filePartialPath) {
        String normalizedPartialPath
                = Paths.get(filePartialPath).normalize().toString()
                        .replaceAll("^[\\/\\\\]+|[\\/\\\\]+$", "");

        boolean scriptWasAlreadyIncluded = includedScripts.stream()
                .parallel().anyMatch(normalizedPartialPath::equalsIgnoreCase);

        if (scriptWasAlreadyIncluded) {
            Logger.trace(String.format(
                    "Skipping inclusion of script file \"%s\" since it was already "
                    + "included for the current test.",
                    normalizedPartialPath));
            return;
        } else {
            Logger.info(String.format("Including script file \"%s\"...", normalizedPartialPath));
            includedScripts.add(normalizedPartialPath);
            String script = null;

            try {
                InputStream scriptFileStream;
                scriptFileStream = getTestAsset("script", normalizedPartialPath);
                StringWriter writer = new StringWriter();
                IOUtils.copy(scriptFileStream, writer, Charset.forName("UTF-8"));
                script = writer.toString();
            } catch (Exception ex) {
                throw new RuntimeException(String.format("Failed to get script file \"%s\".",
                        normalizedPartialPath), ex);
            }

            try {
                this.scriptEngine.eval(script);
            } catch (Exception ex) {
                throw new RuntimeException(
                        String.format("Failed to run script file %s",
                                normalizedPartialPath), ex);
            }
        }
    }

    /**
     * Add a variable into the JS engine's global scope.
     */
    public void injectVariable(String varName, Object varValue) {
        this.scriptEngine.put(varName, varValue);
    }

    private boolean isLastSegment(int currentSegment) {
        Optional<TestDefActor> testDefActorOpt = currentTest.actors.stream()
                .filter(a -> a.actorType.equals(this.actorType)).findFirst();
        Integer lastSegment = 0;
        for (TestDefSegment segment : testDefActorOpt.get().segments) {
            if (segment.segment > lastSegment) {
                lastSegment = segment.segment;
            }
        }
        if (currentSegment >= lastSegment) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Logs the names, versions and commit SHAs of relevant JAR files.
     */
    private void logExtensions() {
        try {
            Path userJarsPath = this.workDir.toPath().resolve("user-jars");
            Path[] jars = Files.walk(userJarsPath)
                    .filter((path) -> path.toString().endsWith(".jar"))
                    .toArray(Path[]::new);
            if (jars.length > 0) {
                Logger.debug("Extension JARs:");
                URI userJarsUri = userJarsPath.toUri();
                for (Path jar : jars) {
                    JarFile jarFile = new JarFile(jar.toString());
                    Logger.debug(URLDecoder.decode(
                            String.format("  %s: %s %s",
                                    new File(userJarsUri.relativize(jar.toUri()).toString()),
                                    JarUtil.getManifestAttribute(jarFile, "Build-Time"),
                                    JarUtil.getManifestAttribute(jarFile, "Implementation-Version")),
                            "UTF-8"));
                }
            }
        } catch (Exception exc) {
            Logger.warning("Failed to log extension JARs");
        }
    }

    /**
     * Logs the names, versions and commit SHAs of relevant JAR files.
     */
    private void logJarVersions() {
        Collection<File> jarFiles = null;

        List<JarFile> jars = new LinkedList<>();
        JarFile mainJar = JarUtil.getJarFile(TestActor.class);
        if (mainJar != null) {
            jars.add(mainJar);
            File mainJarFile = new File(mainJar.getName());
            jarFiles = FileUtils.listFiles(mainJarFile.getParentFile(), new String[]{"jar"}, true);
        } else {
            jarFiles = FileUtils.listFiles(new File("."), new String[]{"jar"}, true);
        }

        if (jarFiles != null && jarFiles.size() > 0) {
            Logger.debug("Test actor JAR versions:");

            for (File childFile : jarFiles) {
                if (childFile.getName().matches("dtest.+\\.jar|opentest.+\\.jar")) {
                    try {
                        JarFile jar = new JarFile(childFile);
                        Logger.debug(String.format("  %s: %s %s",
                                new File(jar.getName()).getName(),
                                JarUtil.getManifestAttribute(jar, "Build-Time"),
                                JarUtil.getManifestAttribute(jar, "Implementation-Version")));
                    } catch (IOException ex) {
                        Logger.warning(String.format("Failed to determine version for JAR %s",
                                childFile.getName()));
                    }
                }
            }
        }
    }

    /**
     * Populate $dataRecord and the other JavaScript variables related to
     * executing data-driven tests.
     */
    private void populateDataRecordJsApi() {
        if (this.currentDataSet != null && this.currentDataSet.size() > 0) {
            Object currentDataRecord = this.currentDataSet.get(
                    this.currentSessionStatus.currentDataRecordIndex);

            this.scriptEngine.put(
                    "$dataRecord",
                    this.convertToJavaScriptType(currentDataRecord));

            this.scriptEngine.put(
                    "$dataRecordIndex",
                    this.currentSessionStatus.currentDataRecordIndex);

            this.scriptEngine.put(
                    "$dataRecordNumber",
                    this.currentSessionStatus.currentDataRecordIndex + 1);

            this.scriptEngine.put(
                    "$dataRecordCount",
                    this.currentDataSet.size());

            this.scriptEngine.put(
                    "$dataSet",
                    this.currentDataSet);

            Logger.info(String.format(
                    "Using data record %s of %s: %s",
                    this.currentSessionStatus.currentDataRecordIndex + 1,
                    this.currentDataSet.size(),
                    Factory.getGson().toJson(currentDataRecord)));
        } else {
            this.scriptEngine.put("$dataRecord", null);
        }
    }

    private void publishSessionData(Map<String, Object> sharedData) throws IOException {
        HttpRequestOptions options = new HttpRequestOptions(
                String.format("%s/api/session/%s/data",
                        this.syncServerUrl,
                        this.currentSessionStatus.id),
                HttpVerb.PUT);
        ServerRequest request = new ServerRequest(options, config);

        Gson gson = Factory.getGsonBuilder().create();
        String jsonData = gson.toJson(sharedData);
        request.setContent(jsonData, ContentType.APPLICATION_JSON);

        Logger.debug(String.format("Publishing session data: %s", jsonData));
        request.execute();
    }

    private void publishSessionData(String name, Object value) throws IOException {
        Map<String, Object> sharedData = new HashMap<String, Object>();
        sharedData.put(name, value);
        publishSessionData(sharedData);
    }

    private void publishSharedData(Map<String, Object> sharedData) throws IOException {
        HttpRequestOptions options = new HttpRequestOptions(
                String.format("%s/api/session/%s/test/%s/data",
                        this.syncServerUrl,
                        this.currentSessionStatus.id,
                        this.currentSessionStatus.currentTestIndex),
                HttpVerb.PUT);
        ServerRequest request = new ServerRequest(options, config);

        Gson gson = Factory.getGsonBuilder().create();
        String jsonData = gson.toJson(sharedData);
        request.setContent(jsonData, ContentType.APPLICATION_JSON);

        Logger.debug(String.format("Publishing shared data: %s", jsonData));
        request.execute();
    }

    private void publishSharedData(String name, Object value) throws IOException {
        Map<String, Object> sharedData = new HashMap<String, Object>();
        sharedData.put(name, value);
        publishSharedData(sharedData);
    }

    /**
     * Makes a "checkpoint-failed" API call to the sync server to mark the fact
     * that the specified test has failed due to a isCheckpoint failure.
     */
    private void reportCheckpointFailed(String sessionId, int testIndex, Integer subtestIndex) {
        String url;
        if (subtestIndex == null) {
            url = String.format("%s/api/session/%s/checkpoint-failed?test=%s",
                    this.syncServerUrl,
                    sessionId,
                    testIndex);
        } else {
            url = String.format("%s/api/session/%s/checkpoint-failed?test=%s&subtest=%s",
                    this.syncServerUrl,
                    sessionId,
                    testIndex,
                    subtestIndex);
        }

        HttpRequestOptions options = new HttpRequestOptions(url, HttpVerb.PUT);
        ServerRequest request = new ServerRequest(options, config);

        try {
            request.execute();
        } catch (IOException ex) {
            Logger.error("Failed reporting checkpoint failure to the sync server", ex);
        }
    }

    /**
     * Waits for the actorType to be acquired by a test session and does the
     * work for that one session, then returns.
     *
     * @param maxWaitTime Maximum time this test actorType will wait to be
     * acquired by a test session
     * @throws Exception
     */
    @Override
    public void runOneSession(Duration maxWaitTime) {
        try {
            Duration waitTime = Duration.ofSeconds(0);

            if (this.currentSessionStatus == null) {
                String tags = "";

                if (this.actorTags.size() > 0) {
                    tags = String.format("with tag(s) [%s]", String.join(", ", this.actorTags));

                } else {
                    tags = "with no tags";
                }

                log.info(String.format("Actor %s of type %s %s is waiting to be acquired by a test session%s...",
                        this.actorId,
                        this.actorType,
                        tags,
                        maxWaitTime != null
                                ? String.format(" (timeout is %s seconds)", maxWaitTime.getSeconds())
                                : ""));
            }

            // Wait until this actorType is acquired by a test session. The
            // currentTestSession field is populated in the announce thread which
            // is started in the constructor.
            while (this.currentSessionStatus == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                waitTime = waitTime.plus(Duration.ofMillis(1000));
                if (maxWaitTime != null && waitTime.compareTo(maxWaitTime) > 0) {
                    log.info(String.format("The maximum wait time of %s seconds was exceeded while waiting for a test session to start",
                            maxWaitTime.getSeconds()));
                    return;
                }
            }

            // Reset data file and macro caches
            this.dataFileCache = new HashMap<>();
            this.macroCache = new HashMap<>();

            HashMap<String, String> actorLogContext = new HashMap<String, String>();
            actorLogContext.put("actorId", actorId);
            actorLogContext.put("actorType", actorType);
            HttpLogger logger = new HttpLogger(
                    this.syncServerUrl,
                    this.currentSessionStatus.id,
                    actorLogContext,
                    this.config);
            this.log = logger;
            logger.setMaskSecrets(this.config.getBoolean("maskSecrets", true));
            logger.setLevel(LogLevel.valueOf(this.config.getString("logLevel", "DEBUG")));

            this.log.info(String.format("Actor %s of type %s was acquired by test session %s",
                    this.actorId,
                    this.actorType,
                    this.currentSessionStatus.id));

            Logger.setLogger(this.log);
            logJarVersions();
            logExtensions();

            // Start querying the session status and executing the tests/segments
            while (this.currentSessionStatus != null) {
                SessionStatusResponse sessionStatus = null;

                try {
                    sessionStatus = this.getTestSessionStatus();
                    boolean mustSetEnvironment
                            = this.currentSessionStatus.environment == null
                            && sessionStatus.environment != null
                            && !sessionStatus.environment.trim().isEmpty();

                    if (mustSetEnvironment) {
                        this.currentSessionStatus.environment = sessionStatus.environment;
                        log.info(String.format("Test session %s is running in environment \"%s\"",
                                this.currentSessionStatus.id,
                                sessionStatus.environment));
                    }
                } catch (Exception ex) {
                    Logger.error("An error happened while reading the session status from the "
                            + "sync server, which shouldn't normally happen. Please provide all "
                            + "potentially relevant data to the dev team for a fix (logs, context, etc).", ex);
                    //TODO: Continue implementation to cover all edge cases and make the test actor more resilient
                    return;
                }

                // Determine if the current test has ended and notify all
                // observers interested in this event
                boolean weWillMoveToNextTest = this.currentSessionStatus.currentIteration < sessionStatus.currentIteration
                        || this.currentSessionStatus.currentDataRecordIndex < sessionStatus.currentDataRecordIndex
                        || this.currentSessionStatus.currentTestIndex < sessionStatus.currentTestIndex;
                boolean thisIsNotTheVeryFirstTest = this.currentSessionStatus.currentTestIndex >= 0;
                if (thisIsNotTheVeryFirstTest && (weWillMoveToNextTest)) {
                    this.setChanged();
                    log.trace("Firing TEST_COMPLETED event...");
                    this.notifyObservers(TestActorEvents.TEST_COMPLETED);
                }

                // TODO: If the session status returns null for a longer time, abandon session
                if (sessionStatus != null) {
                    if (sessionStatus.status.equals("started")) {
                        // Update the iteration number and reset current test, if necessary
                        if (this.currentSessionStatus.currentIteration < sessionStatus.currentIteration) {
                            this.currentSessionStatus.currentIteration = sessionStatus.currentIteration;
                            this.currentSessionStatus.currentTestIndex = -1;
                            this.currentSessionStatus.currentSegmentIndex = -1;
                            this.currentSessionStatus.currentDataRecordIndex = -1;
                            this.currentSegmentIsCompleted = false;
                        }

                        // Update the data record index and reset current test, if necessary
                        if (this.currentSessionStatus.currentDataRecordIndex < sessionStatus.currentDataRecordIndex) {
                            this.currentSessionStatus.currentIteration = sessionStatus.currentIteration;
                            this.currentSessionStatus.currentTestIndex = -1;
                            this.currentSessionStatus.currentSegmentIndex = -1;
                            this.currentSessionStatus.currentDataRecordIndex = sessionStatus.currentDataRecordIndex;
                            this.currentSegmentIsCompleted = false;
                        }

                        // Update the index of the current test for this actorType and
                        // load the test definition
                        if (this.currentSessionStatus.currentTestIndex < sessionStatus.currentTestIndex) {
                            this.currentTest = null;
                            this.currentSegmentIsCompleted = false;
                            this.currentSessionStatus.currentTestIndex = sessionStatus.currentTestIndex;
                            this.currentSessionStatus.currentTestPath = sessionStatus.currentTestPath;
                            this.currentSessionStatus.currentTestName = sessionStatus.currentTestName;
                            this.currentSessionStatus.currentSegmentIndex = sessionStatus.currentSegmentIndex;
                            this.currentSessionStatus.currentDataRecordIndex = sessionStatus.currentDataRecordIndex;

                            HttpRequestOptions options = new HttpRequestOptions(
                                    String.format("%s/api/session/%s/actor/%s/test/%s/step/%s",
                                            this.syncServerUrl,
                                            this.currentSessionStatus.id,
                                            this.actorId,
                                            this.currentSessionStatus.currentTestIndex,
                                            0),
                                    HttpVerb.PUT);
                            ServerRequest stepStatusRequest = new ServerRequest(options, config);

                            try {
                                // Initialize the script engine for the current test
                                this.scriptEngine = createScriptEngine();
                                log.clearSecrets();

                                // Identify and parse the test definition file
                                this.currentTest = getTestDefinition(String.format("%s/%s",
                                        this.currentSessionStatus.currentTestPath,
                                        this.currentSessionStatus.currentTestName));

                                this.includedScripts = new ArrayList<String>();
                                evalIncludes(this.currentTest.includes);
                                this.currentDataSet = evalDataSet(this.currentTest.dataSet);

                                // This is the suffix we are adding at the end of the test
                                // name for data-driven tests (e.g. "[1]")
                                String dataRecordRepresentation = this.currentDataSet != null
                                        ? String.format(
                                                " [%s]",
                                                this.currentSessionStatus.currentDataRecordIndex + 1)
                                        : "";

                                String testPath = this.currentSessionStatus.currentTestPath.length() > 0
                                        ? this.currentSessionStatus.currentTestPath + "/"
                                        : "";

                                log.info(String.format("==================== %s: %s%s%s ====================",
                                        this.actorType,
                                        testPath,
                                        this.currentSessionStatus.currentTestName,
                                        dataRecordRepresentation));

                                log.info(String.format("Actor %s started executing test %s%s%s...",
                                        this.actorType,
                                        testPath,
                                        this.currentSessionStatus.currentTestName,
                                        dataRecordRepresentation));

                                this.localData = new HashMap<>();

                                populateDataRecordJsApi();

                                // Start step 0
                                stepStatusRequest.setContent("{\"status\":\"started\",\"result\":\"pending\"}", ContentType.APPLICATION_JSON);
                                stepStatusRequest.execute();

                                // Notify the sync service that segment 0 of the test succeeded. Only
                                // send the dataRecordCount in segment 0 of test 0 and only for data-driven
                                // tests for the very first data record iteration.
                                String jsonContent;
                                if (this.currentDataSet != null
                                        && sessionStatus.currentSegmentIndex == 0
                                        && sessionStatus.currentDataRecordIndex == 0) {

                                    jsonContent = String.format(
                                            "{\"dataRecordCount\":%s,\"status\":\"completed\",\"result\":\"passed\"}",
                                            this.currentDataSet.size());
                                } else {
                                    jsonContent = String.format(
                                            "{\"status\":\"completed\",\"result\":\"passed\"}");
                                }
                                stepStatusRequest.setContent(jsonContent, ContentType.APPLICATION_JSON);
                                stepStatusRequest.execute();
                                this.currentSegmentIsCompleted = true;
                            } catch (Exception ex) {
                                StringWriter sw = new StringWriter();
                                ex.printStackTrace(new PrintWriter(sw));
                                log.error((sw.toString()));

                                // Notify the sync service that step 0 of the test failed
                                stepStatusRequest.setContent("{\"status\":\"completed\",\"result\":\"failed\"}", ContentType.APPLICATION_JSON);
                                stepStatusRequest.execute();
                            }
                        }

                        if (this.currentSessionStatus.currentTestIndex == sessionStatus.currentTestIndex) {
                            // Update the current step index for the actorType
                            if (this.currentSessionStatus.currentSegmentIndex < sessionStatus.currentSegmentIndex) {
                                this.currentSessionStatus.currentSegmentIndex = sessionStatus.currentSegmentIndex;
                                currentSegmentIsCompleted = false;
                            }

                            if (!currentSegmentIsCompleted && this.currentSessionStatus.currentSegmentIndex == sessionStatus.currentSegmentIndex) {
                                currentSegmentIsCompleted = true;

                                try {
                                    executeTestSegment(sessionStatus.currentSegmentIndex);
                                } catch (Exception ex) {
                                    Logger.error(ex);
                                }
                            }
                        }
                    } else if (sessionStatus.status.equals("completed")) {
                        this.currentSessionStatus = null;
                    }
                }

                // Wait for a little bit, to avoid overwhelming the sync server while actors
                // are waiting for other actors to finish the work for a specific segment
                if (sessionStatus.currentSegmentIndex > 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                }
            }

            log.info("Test session has completed");

            this.setChanged();
            log.trace("Firing TEST_COMPLETED event...");
            this.notifyObservers(TestActorEvents.TEST_COMPLETED);
            this.setChanged();
            log.trace("Firing SESSION_COMPLETED event...");
            this.notifyObservers(TestActorEvents.SESSION_COMPLETED);
        } catch (Exception ex) {
            Logger.error("The \"runOneSession\" method failed", ex);
        }
    }

    @Override
    public void runOneSession() {
        runOneSession(null);
    }

    private void startAnnounceThread() {
        stopAnnounceThread();

        announceThread = new Thread() {
            public void run() {
                while (!actorIsStopping) {
                    announce();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        };

        log.info(String.format("Connecting to sync service at %s...", syncServerUrl));
        announceThread.start();
    }

    private void stopAnnounceThread() {
        if (announceThread != null) {
            announceThread.interrupt();
        }
    }

    private void startScreenShotUploadThread() {
        stopScreenShotUploadThread();

        screenshotUploadThread = new Thread() {
            public void run() {
                while (true) {
                    uploadScreenshots();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        };

        screenshotUploadThread.start();
    }

    private void stopScreenShotUploadThread() {
        if (screenshotUploadThread != null) {
            screenshotUploadThread.interrupt();
        }
    }

    /**
     * Takes a screenshot by delegating to the "takeScreenshot" method of the
     * provided test action and returns the file object for the image file that
     * was created, or null, if no screenshot was captured.
     */
    private File takeScreenShot(TestAction action, Integer segmentNo, Integer actionNo, String screenShotType) {
        if (screenShotType == null) {
            screenShotType = "info";
        }

        try {
            InputStream screenshotStream = action.takeScreenshot();

            if (screenshotStream != null) {
                boolean foundFilename = false;
                int index = 1;
                File screenshotFile = null;

                while (!foundFilename) {
                    String suffix = index == 1 ? "" : String.format("_%02d", index);
                    screenshotFile = new File(
                            this.getScreenshotsDir().toString(),
                            String.format("SID%s_%s_T%02d_SG%02d_ST%02d_%s%s.png",
                                    this.currentSessionStatus.id,
                                    this.actorType,
                                    this.currentSessionStatus.currentTestIndex + 1,
                                    segmentNo != null ? segmentNo : 0,
                                    actionNo != null ? actionNo : 0,
                                    screenShotType,
                                    suffix));

                    if (!screenshotFile.exists()) {
                        foundFilename = true;
                    }

                    index++;
                }

                FileOutputStream outStream = new FileOutputStream(screenshotFile);
                IOUtils.copy(screenshotStream, outStream);
                screenshotStream.close();
                outStream.close();
                this.screenshots.add(screenshotFile.getName());
                Logger.info(String.format("Captured screenshot at %s", screenshotFile.getAbsolutePath()));
                return screenshotFile;
            } else {
                return null;
            }
        } catch (Exception ex) {
            String actionType = action != null ? action.getClass().getName() : "(N/A)";
            Logger.error(String.format("Failed to capture screenshot for action %s.", actionType), ex);
            return null;
        }
    }

    /**
     * Encodes the provided value to Base64. The value to be encoded can be a
     * string, input stream, byte array or JS native array.
     */
    public byte[] toBase64(Object value) {
        if (value instanceof String) {
            String stringValue = (String) value;
            byte[] encodedBytes = Base64.getMimeEncoder().encode(stringValue.getBytes());
            return encodedBytes;
        } else if (value instanceof InputStream) {
            try {
                InputStream streamValue = (InputStream) value;
                byte[] originalBytes = IOUtils.toByteArray(streamValue);
                byte[] encodedBytes = Base64.getMimeEncoder().encode(originalBytes);
                return encodedBytes;
            } catch (Exception ex) {
                throw new RuntimeException("Failed to encode input stream to base64", ex);
            }
        } else if (value instanceof byte[]) {
            byte[] originalBytes = (byte[]) value;
            byte[] encodedBytes = Base64.getMimeEncoder().encode(originalBytes);
            return encodedBytes;
        } else if (value instanceof ScriptObjectMirror) {
            ScriptObjectMirror scriptObj = (ScriptObjectMirror) value;
            if (scriptObj.isArray()) {
                byte[] originalBytes = TypeUtil.jsNumberArrayToJavaByteArray(scriptObj);
                byte[] encodedBytes = Base64.getMimeEncoder().encode(originalBytes);
                return encodedBytes;
            } else {
                throw new RuntimeException(
                        "Failed to encode JavaScript object to base64. Only "
                        + "strings and number arrays are supported.");
            }
        } else {
            throw new RuntimeException(String.format(
                    "Failed to encode value to base64. Input data type %s is not supported.",
                    value.getClass().getCanonicalName()));
        }
    }

    /**
     * Converts the provided argument to a native JavaScript array. Useful when
     * returning Java collection types from JS APIs, so the JS code can work
     * with the collection using the JS Array APIs. If the value is not
     * "array-like", the method will return a new array with the input value as
     * its only element.
     */
    public Object toJsArray(Object arrayLike) {
        if (this.scriptEngine == null) {
            throw new RuntimeException(
                    "Failed to convert value to a JavaScript array, because the "
                    + "actor's JavaScript engine was not initialized yet.");
        }

        try {
            if (arrayLike.getClass().isArray()) {
                if (arrayLike.getClass().getComponentType().isPrimitive()) {
                    // We need to convert arrays of primitive types to arrays of reference types,
                    // otherwise the call to Array.prototype.slice will return null
                    int arrlength = Array.getLength(arrayLike);
                    Object[] refTypeArray = new Object[arrlength];
                    for (int i = 0; i < arrlength; ++i) {
                        refTypeArray[i] = Array.get(arrayLike, i);
                    }

                    this.scriptEngine.put("$transientVarName", refTypeArray);
                    Object jsArray = this.scriptEngine.eval("Array.prototype.slice.call($transientVarName)");
                    return jsArray;
                } else {
                    this.scriptEngine.put("$transientVarName", arrayLike);
                    Object jsArray = this.scriptEngine.eval("Array.prototype.slice.call($transientVarName)");
                    return jsArray;
                }
            } else if (arrayLike instanceof List) {
                this.scriptEngine.put("$transientVarName", ((List) arrayLike).toArray());
                return this.scriptEngine.eval("Array.prototype.slice.call($transientVarName)");
            } else if (arrayLike instanceof ScriptObjectMirror) {
                ScriptObjectMirror scriptObject = (ScriptObjectMirror) arrayLike;
                if (scriptObject.isArray()) {
                    return scriptObject;
                } else {
                    this.scriptEngine.put("$transientVarName", arrayLike);
                    return this.scriptEngine.eval("[$transientVarName]");
                }
            } else {
                this.scriptEngine.put("$transientVarName", arrayLike);
                return this.scriptEngine.eval("[$transientVarName]");
            }
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed converting value %s to a JS array",
                    arrayLike.toString()), ex);
        }
    }

    /**
     * Converts the provided argument to a native JavaScript type.
     */
    public Object toJsType(Object inputValue) {
        if (this.scriptEngine == null) {
            throw new RuntimeException(
                    "Failed to convert value to a native JavaScript type, because "
                    + "the actor's JavaScript engine was not initialized yet.");
        }

        Object result = inputValue;

        try {
            if (inputValue instanceof String) {
                // Nothing to do here. We'll be returning the exact
                // same String instance that was passed-in.
            } else if (inputValue instanceof Map) {
                result = this.evalScript("(" + Factory.getGson().toJson(inputValue) + ")");
            } else if (inputValue.getClass().isArray()) {
                this.scriptEngine.put("$transientVarName", inputValue);
                result = this.scriptEngine.eval("Array.prototype.slice.call($transientVarName)");
            } else if (inputValue instanceof List) {
                this.scriptEngine.put("$transientVarName", ((List) inputValue).toArray());
                result = this.scriptEngine.eval("Array.prototype.slice.call($transientVarName)");
            } else if (inputValue instanceof ScriptObjectMirror) {
                ScriptObjectMirror scriptObject = (ScriptObjectMirror) inputValue;
                if (scriptObject.isArray()) {
                    result = scriptObject;
                } else {
                    this.scriptEngine.put("$__transientVarName", inputValue);
                    result = this.scriptEngine.eval("[$__transientVarName]");
                }
            } else {
                this.scriptEngine.put("$__transientVarName", inputValue);
                result = this.scriptEngine.eval("[$__transientVarName]");
            }

            this.scriptEngine.eval("delete $__transientVarName");
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed converting value %s to a native JavaScript object",
                    inputValue.toString()), ex);
        }

        return result;
    }

    private void uploadScreenshots() {
        int sleepInterval = 5000;

        while (screenshots.size() > 0) {
            String screenshot = screenshots.get(0);

            try {
                HttpClient httpClient = HttpClients.custom()
                        .setDefaultRequestConfig(RequestConfig.custom()
                                .setCookieSpec(CookieSpecs.STANDARD)
                                .build())
                        .build();

                File file = new File(this.screenshotsDir, screenshot);

                HttpEntity screenshotData = MultipartEntityBuilder.create()
                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                        .addBinaryBody("screenshot", file, org.apache.http.entity.ContentType.DEFAULT_BINARY, file.getName())
                        .build();

                String url = String.format("%s/api/screenshot",
                        this.syncServerUrl);

                HttpUriRequest request = RequestBuilder
                        .post(url)
                        .setEntity(screenshotData)
                        .build();

                ResponseHandler<Integer> responseHandler = response -> {
                    int status = response.getStatusLine().getStatusCode();
                    if (status != 200) {
                        HttpEntity entity = response.getEntity();
                        String textContent = entity != null ? EntityUtils.toString(entity) : null;
                        throw new RuntimeException(String.format(
                                "Received unexpected HTTP response status code %s "
                                + "while uploading screenshot file %s",
                                status,
                                screenshot));
                    }
                    return status;
                };
                httpClient.execute(request, responseHandler);
                screenshots.remove(0);
                sleepInterval = 5000;
            } catch (Exception ex) {
                this.log.error(String.format("Failed uploading screenshot %s",
                        screenshot), ex);

                if (sleepInterval < 30000) {
                    sleepInterval = sleepInterval * 2;
                }
            }

            try {
                Thread.sleep(sleepInterval);
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * Wraps an exception into a CheckpointException. If the provided exception
     * is already a CheckpointException, it's returned unchanged.
     */
    private CheckpointException wrapCheckpointException(Exception ex) {
        if (ex instanceof CheckpointException) {
            return (CheckpointException) ex;
        } else {
            return new CheckpointException(ex);
        }
    }
}