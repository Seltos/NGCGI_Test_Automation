package com.enquero.driverfactory.web_selenium.actions.codecs;


import com.enquero.driverfactory.web_selenium.base.TestAction;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncodeUtf8 extends TestAction {

    @Override
    public void run() {
        super.run();

        String string = this.readStringArgument("sourceString");

        ByteBuffer encodedBytesBuffer = StandardCharsets.UTF_8.encode(string);
        byte[] encodedBytes = Arrays.copyOfRange(
                encodedBytesBuffer.array(),
                encodedBytesBuffer.arrayOffset(),
                encodedBytesBuffer.limit());

        this.writeOutput("encodedBytes", encodedBytes);
    }
}
