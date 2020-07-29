package com.enquero.driverfactory.web_selenium.actions;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import com.enquero.driverfactory.web_selenium.annotations.TestActionArgument;
import com.enquero.driverfactory.web_selenium.annotations.TestActionClass;
import com.enquero.driverfactory.web_selenium.annotations.Type;
import com.enquero.driverfactory.web_selenium.base.TestAction;
import com.enquero.driverfactory.web_selenium.http.ContentType;
import com.enquero.driverfactory.web_selenium.http.HttpRequestOptions;
import com.enquero.driverfactory.web_selenium.http.HttpVerb;
import com.enquero.driverfactory.web_selenium.serialization.json.TrimmableMap;
import com.enquero.driverfactory.web_selenium.util.Factory;

import javax.net.ssl.SSLHandshakeException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@TestActionClass(description = "Performs an HTTP request.")
@TestActionArgument(
        name = "url", type = Type.STRING, optional = false,
        description = "Request URL.")
@TestActionArgument(
        name = "verb", type = Type.STRING, optional = true,
        description = "HTTP verb (GET, POST, PUT, DELETE, etc.). Default: GET.")
@TestActionArgument(
        name = "headers", type = Type.MAP, optional = true,
        description = "HTTP headers to send with the request, as a list of key-value pairs.")
@TestActionArgument(
        name = "body", type = Type.STRING, optional = true,
        description = "HTTP request payload.")
@TestActionArgument(
        name = "successStatusCode", type = Type.STRING, optional = true,
        description = "The HTTP status code that is expected in the response. The "
        + "action will fail if a different status code is received. Default: null.")
@TestActionArgument(
        name = "contentType", type = Type.STRING, optional = true,
        description = "The content type of the payload. This value will be used "
        + "as the Content-Type HTTP header. Default: text/plain.")
@TestActionArgument(
        name = "outputFile", type = Type.STRING, optional = true,
        description = "The full path to a file that the response body will be "
        + "written to. This is particularly useful for downloading files. Default: null.")
@TestActionArgument(
        name = "ignoreCert", type = Type.BOOLEAN, optional = true,
        description = "Boolean flag that instructs the HTTP client to ignore the "
        + "web server certificate. Useful when working with servers that are using "
        + "self-signed certificates. Default: false.")
@TestActionArgument(
        name = "proxy", type = Type.STRING, optional = true,
        description = "HTTP proxy server to be used for the request. Both the server "
        + "name and IP and port number can be specified, separated by \":\" (e.g. "
        + "10.0.0.1:8888). Default: null")
@TestActionArgument(
        name = "parseBody", type = Type.BOOLEAN, optional = true,
        description = "Determines whether the response body should be parsed "
        + "from JSON automatically, to make it easier to work with in subsequent "
        + "steps. Default: true.")

/**
 * Performs an HTTP request.
 */
public class HttpRequest extends TestAction {

    @Override
    public void run() {
        super.run();

        String url = this.readStringArgument("url");
        HttpVerb httpVerb = HttpVerb.valueOf(this.readStringArgument("verb", "GET"));
        Map<String, Object> headers = this.readMapArgument("headers", new HashMap<String, Object>());
        String body = this.readStringArgument("body", null);
        String contentType = this.readStringArgument("contentType", ContentType.TEXT_PLAIN);
        String outputFilePath = this.readStringArgument("outputFile", null);
        Boolean ignoreCert = this.readBooleanArgument("ignoreCert", this.getActor().getConfig().getBoolean("api.ignoreCert", false));
        String proxy = this.readStringArgument("proxy", this.getActor().getConfig().getString("api.httpProxy", null));
        Integer successStatusCode = this.readIntArgument("successStatusCode", null);
        Boolean parseBody = this.readBooleanArgument("parseBody", null);
        boolean noLogs = this.readBooleanArgument("$noLogs", false);

        if (httpVerb == HttpVerb.DELETE && body != null) {
            httpVerb = HttpVerb.DELETE_WITH_BODY;
        }

        HttpRequestOptions options = new HttpRequestOptions(url, httpVerb);
        options.proxy = proxy;
        options.ignoreCert = ignoreCert;

        if (ignoreCert) {
            log.warning(
                    "The SSL certificate is being ignored. The hostname verification and certificate "
                    + "signature verification will be skipped. If the request is made over a network "
                    + "outside of your control, the data exchanged with the HTTP server may be "
                    + "accessible to a malicious third party.");
        }

        com.enquero.driverfactory.web_selenium.http.HttpRequest httpRequest = new com.enquero.driverfactory.web_selenium.http.HttpRequest(options);
        if (body != null) {
            httpRequest.setContent(body, contentType);
        }
        if (headers != null) {
            headers.forEach((headerName, headerValue) -> {
                try {
                    httpRequest.setHeader(headerName, headerValue.toString());
                } catch (Exception ex) {
                    throw new RuntimeException(String.format(
                            "Failed to set HTTP header \"%s\" with value \"%s\". %s",
                            headerName,
                            headerValue,
                            ex.getMessage()));
                }
            });
        }

        int durationMs = 0;

        try {
            long startTime = System.nanoTime();
            httpRequest.execute();
            long endTime = System.nanoTime();
            durationMs = (int) ((endTime - startTime) / 1e6);
        } catch (SSLHandshakeException ex) {
            throw new RuntimeException(
                    "An SSL error occured. If the HTTPS service you are calling uses a "
                    + "self-signed certificate, you can provide the \"ignoreCert\" argument "
                    + "to instruct the HttpRequest action to skip certificate validation. "
                    + "The syntax is: \"ignoreCert: true\".", ex);
        } catch (Exception ex) {
            throw new RuntimeException("HTTP request failed", ex);
        }

        String responseString = "";

        if (outputFilePath == null) {
            responseString = httpRequest.getResponseAsString();
        } else {
            try {
                responseString = "<<RESPONSE PAYLOAD WAS SAVED INTO OUTPUT FILE>>";
                InputStream responseInputStream = httpRequest.getResponseAsStream();
                File outputFile = new File(outputFilePath);
                outputFile.createNewFile();
                FileOutputStream outputFileStream = new FileOutputStream(outputFile);
                IOUtils.copy(responseInputStream, outputFileStream);
                responseInputStream.close();
                outputFileStream.close();
            } catch (Throwable ex) {
                throw new RuntimeException(String.format(
                        "Failed to save response payload into file \"%s\"",
                        outputFilePath), ex);
            }
        }

        int responseStatusCode = httpRequest.getResponseStatusCode();

        Gson gson = Factory.getGson(this.getActor().getConfig());

        log.debug(String.format(
                "The server returned HTTP status code %s.",
                responseStatusCode));

        // Validate status code, if a success status code was specified
        if (successStatusCode != null && !successStatusCode.equals(responseStatusCode)) {
            String exceptionMessage = String.format(
                    "The HTTP request returned status code %s, but we expected %s.",
                    responseStatusCode,
                    successStatusCode);
            if (!noLogs) {
                // This variable stores a partial representation of the response that
                // will be used for logging. This way we can avoid cluttering the test
                // session log with huge response payloads.
                String responseStringTruncated = responseString;
                int maxLogEntryLength = this.getActor().getConfig().getInteger("maxLogEntryLength", 50000);
                if (responseStringTruncated.length() > maxLogEntryLength) {
                    responseStringTruncated = responseStringTruncated.substring(0, maxLogEntryLength + 1)
                            + " <<RESPONSE_WAS_TRUNCATED>>";
                }

                Map<String, Object> responseDetails = new HashMap();
                responseDetails.put("statusCode", responseStatusCode);
                responseDetails.put("durationMs", durationMs);
                responseDetails.put("headers", new TrimmableMap(httpRequest.getResponseHeaders()));
                responseDetails.put("body", responseStringTruncated);

                exceptionMessage += String.format(
                        "\n\tThe response details were: %s",
                        gson.toJson(responseDetails));
            }
            throw new RuntimeException(exceptionMessage);
        }

        this.writeOutput("durationMs", durationMs);
        this.writeOutput("statusCode", responseStatusCode);

        Map<String, String> responseHeaders = httpRequest.getResponseHeaders();
        this.writeOutput("headers", responseHeaders);

        String contentTypeHeader = httpRequest.getFirstHeader("Content-Type");

        if (parseBody == Boolean.FALSE) {
            this.writeOutput("body", responseString);
        } else {
            if (parseBody == Boolean.TRUE || payloadIsJson(contentTypeHeader)) {
                try {
                    Object jsNativeValue = this.getActor().parseJsonToNativeJsType(responseString);
                    this.writeOutput("body", jsNativeValue);
                } catch (Exception ex) {
                    // JSON parsing failed, so we'll just output the payload as string
                    this.log.error("Failed  to parse response body as JSON data", ex);
                    this.writeOutput("body", responseString);
                }
            } else {
                this.writeOutput("body", responseString);
            }
        }
    }

    private boolean payloadIsJson(String contentTypeHeader) {
        if (contentTypeHeader != null) {
            contentTypeHeader = contentTypeHeader.toLowerCase();

            if (contentTypeHeader.contains("/json")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}