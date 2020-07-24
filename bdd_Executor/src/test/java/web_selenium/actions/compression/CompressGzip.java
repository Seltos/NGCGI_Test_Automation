package web_selenium.actions.compression;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.IOUtils;
import web_selenium.base.TestAction;
import web_selenium.util.TypeUtil;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class CompressGzip extends TestAction {

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
                        "You must either provide the \"sourceFile\" argument, the \"sourceBytes\" argument or the "
                        + "\"sourceString\" argument to indicate what is the source data to work with.");
            }

            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzOutputStream = new GZIPOutputStream(byteOutputStream);
            gzOutputStream.write(sourceBytes);
            gzOutputStream.close();

            byte[] compressedBytes = byteOutputStream.toByteArray();

            if (targetFile != null) {
                FileOutputStream outputStream
                        = new FileOutputStream(targetFile);
                IOUtils.copy(new ByteArrayInputStream(compressedBytes), outputStream);
                outputStream.close();
            }

            this.writeOutput("compressedBytes", compressedBytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
