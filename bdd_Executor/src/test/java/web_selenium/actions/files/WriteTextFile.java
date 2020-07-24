package web_selenium.actions.files;

import web_selenium.base.TestAction;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class WriteTextFile extends TestAction {

    @Override
    public void run() {
        super.run();

        // The "file" argument was deprecated in favor of "targetFile"
        String targetFile = this.readStringArgument("file", null);
        if (targetFile == null) {
            targetFile = this.readStringArgument("targetFile");
        }
        String text = this.readStringArgument("text");

        // Valid encodings are the ones supported by java.nio.charset.Charset:
        // "US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16"
        String encoding = this.readStringArgument("encoding", "UTF-8");
        Boolean append = this.readBooleanArgument("append", false);

        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(targetFile, append), encoding));

            out.append(text);

            out.flush();
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to write text file %s using the %s encoding",
                    targetFile,
                    encoding), ex);
        }
    }
}
