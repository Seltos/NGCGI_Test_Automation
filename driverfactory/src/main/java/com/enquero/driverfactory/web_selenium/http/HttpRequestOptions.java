package com.enquero.driverfactory.web_selenium.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestOptions {

    public String basicAuthLogin;

    public String basicAuthPassword;

    public Map<String, String> headers;

    public boolean ignoreCert;

    public HttpVerb httpVerb;

    public String proxy;

    public String url;

    public HttpRequestOptions(String url, HttpVerb httpVerb) {
        this.basicAuthLogin = null;
        this.basicAuthPassword = null;
        this.url = url;
        this.httpVerb = httpVerb;
        this.proxy = null;
        this.headers = new HashMap<>();
        this.ignoreCert = false;
    }
}