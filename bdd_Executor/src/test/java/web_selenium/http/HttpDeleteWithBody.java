package web_selenium.http;

import org.apache.http.client.methods.HttpPost;

import java.net.URI;

public class HttpDeleteWithBody extends HttpPost {

    HttpDeleteWithBody(String uri) {
        super(uri);
    }
    
    HttpDeleteWithBody(URI uri) {
        super(uri);
    }
    
    @Override
    public String getMethod() {
        return "DELETE";
    }
}
