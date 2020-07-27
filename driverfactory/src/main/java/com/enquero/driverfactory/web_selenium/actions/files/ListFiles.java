package com.enquero.driverfactory.web_selenium.actions.files;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import com.enquero.driverfactory.web_selenium.base.TestAction;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.stream.Collectors;

public class ListFiles extends TestAction {

    @Override
    public void run() {
        super.run();

        String directoryPath = readStringArgument("directory");
        String pattern = readStringArgument("pattern", "*");
        // Some valid orderBy values: "date", "date, desc", "name", "size"
        String orderBy = readStringArgument("orderBy", "name");

        List<File> files;
        File directory = new File(directoryPath);
        if (directory.exists()) {
            FileFilter fileFilter = new WildcardFileFilter(pattern);
            files = Arrays.asList(directory.listFiles(fileFilter));
        } else {
            log.warning(String.format("Directory \"%s\" doesn't exist", directoryPath));
            files = new ArrayList<File>(0);
        }

        if (orderBy != null) {
            if (orderBy.matches("^\\s*date.*")) {
                files = files.stream().sorted(Comparator.comparing(File::lastModified)).collect(Collectors.toList());
            } else if (orderBy.matches("^\\s*name.*")) {
                files = files.stream().sorted(Comparator.comparing(File::getName)).collect(Collectors.toList());
            } else if (orderBy.matches("^\\s*size.*")) {
                files = files.stream().sorted(Comparator.comparing(File::length)).collect(Collectors.toList());
            }

            if (orderBy.matches(".*,\\s*desc\\s*$")) {
                Collections.reverse(files);
            }
        }

        this.writeOutput("files", files);
        if (files.size() > 0) {
            this.writeOutput("firstFile", files.size() > 0 ? files.get(0) : null);
            this.writeOutput("lastFile", files.size() > 0 ? files.get(files.size() - 1) : null);
        }
    }
}
