package web_selenium.actions.codecs;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import web_selenium.base.TestAction;
import web_selenium.util.TypeUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DecodeUtf8 extends TestAction {

    @Override
    public void run() {
        super.run();

        Object sourceBytesObj = this.readArgument("sourceBytes");

        byte[] sourceBytes;

        if (sourceBytesObj instanceof byte[]) {
            sourceBytes = (byte[]) sourceBytesObj;
        } else if (sourceBytesObj instanceof ScriptObjectMirror) {
            sourceBytes = TypeUtil.jsNumberArrayToJavaByteArray(
                    (ScriptObjectMirror) sourceBytesObj);
        } else {
            throw new RuntimeException(
                    "The \"sourceBytes\" argument must be either a Java "
                    + "native byte array or a JavaScript number array.");
        }

        String decodedString = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(sourceBytes)).toString();

        this.writeOutput("decodedString", decodedString);
    }
}
