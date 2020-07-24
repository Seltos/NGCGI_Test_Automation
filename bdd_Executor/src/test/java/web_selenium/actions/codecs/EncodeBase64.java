package web_selenium.actions.codecs;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.IOUtils;
import web_selenium.base.TestAction;
import web_selenium.util.TypeUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

public class EncodeBase64 extends TestAction {

    @Override
    public void run() {
        super.run();

        String sourceFile = readStringArgument("sourceFile", null);
        Object sourceBytesObj = this.readArgument("sourceBytes", null);
        String sourceString = this.readStringArgument("sourceString", null);

        try {
            byte[] sourceBytes;

            if (sourceFile != null) {
                InputStream sourceStream = new FileInputStream(sourceFile);
                sourceBytes = IOUtils.toByteArray(sourceStream);
            } else if (sourceBytesObj != null) {
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
            } else if (sourceString != null) {
                sourceBytes = sourceString.getBytes("UTF-8");
            } else {
                throw new RuntimeException(
                        "You must either provide the \"sourceFile\" argument, the \"sourceBytes\" argument "
                        + "or the \"sourceString\" argument to indicate what is the data to be encoded.");
            }

            byte[] encodedBytes = Base64.getMimeEncoder().encode(sourceBytes);

            this.writeOutput("encodedString", new String(encodedBytes, "UTF-8"));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to encode data to Base64", ex);
        }
    }
}
