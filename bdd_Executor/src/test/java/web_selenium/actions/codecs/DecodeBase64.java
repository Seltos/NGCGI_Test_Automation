package web_selenium.actions.codecs;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.IOUtils;
import web_selenium.base.TestAction;
import web_selenium.util.TypeUtil;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Base64;

public class DecodeBase64 extends TestAction {

    @Override
    public void run() {
        super.run();

        String sourceFile = readStringArgument("sourceFile", null);
        Object sourceBytesObj = this.readArgument("sourceBytes", null);
        String sourceString = this.readStringArgument("sourceString", null);
        String targetFile = readStringArgument("targetFile", null);

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
                        "You must either provide the \"sourceFile\" argument, the \"sourceBytes\" argument or "
                        + "the \"sourceString\" argument to indicate what is the Base64 data to be decoded.");
            }

            byte[] decodedBytes = Base64.getMimeDecoder().decode(sourceBytes);

            if (targetFile != null) {
                FileOutputStream outputStream
                        = new FileOutputStream(targetFile);
                IOUtils.copy(new ByteArrayInputStream(decodedBytes), outputStream);
                outputStream.close();
            }
            
            this.writeOutput("decodedBytes", decodedBytes);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to encode data to Base64", ex);
        }
    }
}
