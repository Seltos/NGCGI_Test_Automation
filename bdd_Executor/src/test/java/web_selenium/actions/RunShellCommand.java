package web_selenium.actions;

import org.apache.commons.io.IOUtils;
import web_selenium.base.TestAction;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Runs a command in the system's shell.
 */
public class RunShellCommand extends TestAction {

    @Override
    public void run() {
        String commandLine = readStringArgument("commandLine");
        Map<String, Object> envVarsMap = this.readMapArgument("envVars", null);
        String workDirStr = readStringArgument("workDir", null);

        try {
            String[] envVars = null;
            if (envVarsMap != null) {
                envVars = envVarsMap.entrySet().stream()
                        .map(e -> e.getKey() + "=" + e.getValue().toString())
                        .toArray(String[]::new);
            }

            File workDir = workDirStr != null ? new File(workDirStr) : null;

            Process process = Runtime.getRuntime().exec(
                    commandLine,
                    envVars,
                    workDir);

            int exitCode = process.waitFor();
            String stderr = IOUtils.toString(process.getErrorStream(), StandardCharsets.UTF_8);
            String stdout = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);

            this.writeOutput("exitCode", exitCode);
            this.writeOutput("stderr", stderr);
            this.writeOutput("stdout", stdout);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }
}
