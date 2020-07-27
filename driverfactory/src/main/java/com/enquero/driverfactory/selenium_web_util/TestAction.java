package com.enquero.driverfactory.selenium_web_util;

import java.util.Map;

public abstract class TestAction {

    private Map<String, Object> args;

    public void run() {
    }

    public void writeArgument(String key, Object value) {
        args.put(key, value);
    }
}
