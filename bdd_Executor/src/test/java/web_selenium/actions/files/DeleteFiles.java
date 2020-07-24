package web_selenium.actions.files;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import web_selenium.base.TestAction;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;


public class DeleteFiles extends TestAction {

    @Override
    public void run() {
        super.run();

        String directoryPath = readStringArgument("directory");
        String pattern = readStringArgument("pattern", null);
        String fileName = readStringArgument("fileName", null);

        File directory = new File(directoryPath);
        int fileCounter = 0;

        if (directory.exists()) {
            if (fileName != null) {
                File file = new File(directoryPath, fileName);
                if (file.exists()) {
                    file.delete();
                    if (file.exists()) {
                        throw new RuntimeException(String.format(
                                "Failed to delete file \"%s\"", file.getAbsolutePath()));
                    }
                    log.info("We deleted 1 file");
                } else {
                    log.warning(String.format("File \"%s\" doesn't exist. There's nothing to delete.", file.getAbsolutePath()));
                }
            } else if (pattern != null) {
                FileFilter fileFilter = new WildcardFileFilter(pattern);
                File[] files = directory.listFiles(fileFilter);
                List<String> failedFiles = new ArrayList<>();
                for (File file : files) {
                    log.debug(String.format(
                            "Deleting file \"%s\"...", file.getAbsolutePath()));
                    file.delete();
                    if (file.exists()) {
                        failedFiles.add(file.getAbsolutePath());
                    } else {
                        fileCounter++;
                    }
                }
                
                log.info(String.format("We deleted %s file(s)", fileCounter));

                if (failedFiles.size() > 0) {
                    throw new RuntimeException(String.format(
                            "Failed to delete file(s) %s",
                            String.join(", ", failedFiles)));
                }
            } else {
                throw new RuntimeException(
                        "Neither the \"filename\" argument, nor the \"pattern\" "
                        + "argument were provided.");
            }
        } else {
            throw new RuntimeException(String.format(
                    "Directory \"%s\" doesn't exist", directoryPath));
        }
    }
}
