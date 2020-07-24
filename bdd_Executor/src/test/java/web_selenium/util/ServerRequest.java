package web_selenium.util;


import web_selenium.http.HttpRequest;
import web_selenium.http.HttpRequestOptions;

/**
 * HTTP request to the OpenTest server that is automatically configured as per
 * the passed-in Config instance for proxy, authentication, etc.
 */
public class ServerRequest extends HttpRequest {

    public ServerRequest(HttpRequestOptions options, Config config) {
        super(options);

        // Set HTTP basic auth, if credentials present in config
        String serverLogin = config.getString("serverLogin", null);
        if (serverLogin != null) {
            String serverPassword = config.getString("serverPassword", null);
            if (serverPassword != null) {
                this.setBasicAuth(serverLogin, serverPassword);
            }
        }

        String httpProxy = config.getString("httpProxy", null);
        if (httpProxy != null) {
            this.setProxy(httpProxy);
        }
    }
}
