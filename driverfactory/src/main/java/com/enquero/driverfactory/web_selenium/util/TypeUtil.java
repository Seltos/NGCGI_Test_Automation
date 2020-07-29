package com.enquero.driverfactory.web_selenium.util;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class TypeUtil {

    public static byte[] jsNumberArrayToJavaByteArray(ScriptObjectMirror scriptObj) {
        if (scriptObj.isArray()) {
            byte[] javaByteArray = new byte[scriptObj.values().size()];
            int currentIndex = 0;
            for (Object currentValue : scriptObj.values()) {
                if (currentValue instanceof Integer) {
                    Integer intValue = (Integer) currentValue;
                    if (intValue >= 0 && intValue <= 255) {
                        javaByteArray[currentIndex] = (byte) intValue.intValue();
                    } else {
                        throw new RuntimeException(String.format(
                                "Failed to convert JS array to Java byte array because array "
                                + "element %s is not a number between 0 and 255.",
                                intValue));
                    }
                } else {
                    throw new RuntimeException(String.format(
                            "Failed to encode JS array to base64. Array "
                            + "element %s is not a number.",
                            currentValue));
                }
                currentIndex++;
            }

            return javaByteArray;
        } else {
            throw new RuntimeException(
                    "The provided JavaScript native value was not an array.");
        }
    }
}