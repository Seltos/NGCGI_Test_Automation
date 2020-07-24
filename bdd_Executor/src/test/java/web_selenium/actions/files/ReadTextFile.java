package web_selenium.actions.files;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import web_selenium.base.TestAction;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ReadTextFile extends TestAction {

    @Override
    public void run() {
        super.run();

        // The "file" argument was deprecated in favor of "sourceFile"
        String sourceFile = this.readStringArgument("file", null);
        if (sourceFile == null) {
            sourceFile = this.readStringArgument("sourceFile");
        }

        // Valid encodings are the ones supported by java.nio.charset.Charset:
        // "US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16"
        String encoding = this.readStringArgument("encoding", "UTF-8");
        Boolean excludeBom = this.readBooleanArgument("excludeBom", Boolean.TRUE);

        try {
            FileInputStream fileInputStream = null;
            InputStream inputStream;

            if (excludeBom) {
                fileInputStream = new FileInputStream(sourceFile);
                inputStream = new BOMInputStream(fileInputStream);
            } else {
                inputStream = new FileInputStream(sourceFile);
            }

            

            String text = IOUtils.toString(inputStream, Charset.forName(encoding));
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            inputStream.close();

            this.writeOutput("text", text);
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to read file %s using the %s encoding",
                    sourceFile,
                    encoding), ex);
        }
    }
}
