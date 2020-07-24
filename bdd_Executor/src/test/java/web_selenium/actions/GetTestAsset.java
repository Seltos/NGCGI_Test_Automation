package web_selenium.actions;

import web_selenium.base.TestAction;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Reads a test asset from the sync service and saves it to a local file.
 */
public class GetTestAsset extends TestAction {

    @Override
    public void run() {
        super.run();

        String assetType = this.readStringArgument("assetType");
        String assetRelativePath = this.readStringArgument("assetRelativePath");
        String targetFilePath = this.readStringArgument("targetFile");
        Boolean overwrite = this.readBooleanArgument("overwrite", false);

        File targetFile = new File(targetFilePath);
        if (!targetFile.isAbsolute()) {
            targetFile = Paths.get(this.getActor().getTempDir().getAbsolutePath(), targetFilePath).toFile();
        }

        targetFile.getParentFile().mkdirs();

        try {
            InputStream assetStream = this.getActor().getTestAsset(assetType, assetRelativePath);

            if (overwrite) {
                Files.copy(assetStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(assetStream, targetFile.toPath());
            }
            
            this.writeOutput("targetFilePath", targetFile.getAbsolutePath());
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to transfer data from the test asset input stream into file %s",
                    targetFilePath), ex);
        }
    }
}
