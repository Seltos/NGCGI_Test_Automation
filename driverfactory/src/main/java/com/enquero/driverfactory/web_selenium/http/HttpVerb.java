package com.enquero.driverfactory.web_selenium.http;

public enum HttpVerb {
    DELETE,
    /**
     * Will actually use DELETE verb, but also allow a payload to be sent. This
     * is to work around the Apache HTTP client removing the payload for DELETE
     * requests. This value is for internal use only and not to be used directly
     * when calling the HttpRequest action.
     */
    DELETE_WITH_BODY,
    GET,
    HEAD,
    OPTIONS,
    PATCH,
    POST,
    PUT
}