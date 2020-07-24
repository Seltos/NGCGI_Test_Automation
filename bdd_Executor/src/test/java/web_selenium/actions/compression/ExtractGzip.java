package web_selenium.actions.compression;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.IOUtils;
import web_selenium.base.TestAction;
import web_selenium.util.TypeUtil;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class ExtractGzip extends TestAction {

    @Override
    public void run() {
        super.run();

        String sourceFile = readStringArgument("sourceFile", null);
        Object sourceBytesObj = this.readArgument("sourceBytes", null);
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
            } else {
                throw new RuntimeException(
                        "You must either provide the \"sourceFile\" argument or the \"sourceBytes\" "
                        + "argument to indicate what is the source data to work with.");
            }

            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(sourceBytes);
            GZIPInputStream gzInputStream = new GZIPInputStream(byteInputStream);
            byte[] extractedBytes = IOUtils.toByteArray(gzInputStream);

            if (targetFile != null) {
                FileOutputStream outputStream
                        = new FileOutputStream(targetFile);
                IOUtils.copy(new ByteArrayInputStream(extractedBytes), outputStream);
                outputStream.close();
            }

            this.writeOutput("extractedBytes", extractedBytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
