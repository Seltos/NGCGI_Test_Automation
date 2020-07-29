package com.enquero.driverfactory.web_selenium.actions;

import com.enquero.driverfactory.web_selenium.base.TestAction;
import com.enquero.driverfactory.web_selenium.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An action that executes a regular expression against the specified text.
 */
public class ExecuteRegex extends TestAction {

    @Override
    public void run() {
        super.run();

        String text = this.readStringArgument("text");
        Pattern regexArg = this.readRegexArgument("regex");
        boolean globalFlagPresent = RegexUtil
                .getRegexFlags(this.readStringArgument("regex"))
                .contains("g");

        // The following four arguments are deprecated in favor of using the
        // JavaScript-style regex pattern in the "regex" argument
        boolean global = this.readBooleanArgument("global", globalFlagPresent);
        boolean caseInsensitive = this.readBooleanArgument("caseInsensitive", false);
        boolean dotallMode = this.readBooleanArgument("dotallMode", false);
        boolean multiline = this.readBooleanArgument("multiline", false);

        int regexFlags = 0;
        if (caseInsensitive) {
            regexFlags |= Pattern.CASE_INSENSITIVE;
        }
        if (dotallMode) {
            regexFlags |= Pattern.DOTALL;
        }
        if (multiline) {
            regexFlags |= Pattern.MULTILINE;
        }

        Pattern pattern = Pattern.compile(regexArg.pattern(), regexArg.flags() | regexFlags);
        Matcher matcher = pattern.matcher(text);

        if (!global) {
            List<String> groups = new ArrayList<>();

            if (matcher.find()) {
                this.writeOutput("fullMatch", matcher.group(0));

                // Publish one output value per each capture group with names like
                // "group1", "group2", etc. The value "group0" represents the whole
                // text that was matched.
                for (int groupNumber = 0; groupNumber <= matcher.groupCount(); groupNumber++) {
                    String outputName = String.format("group%d", groupNumber);
                    this.writeOutput(outputName, matcher.group(groupNumber));
                    this.markOutputAsDeprecated(outputName);
                    groups.add(matcher.group(groupNumber));
                }
            } else {
                throw new RuntimeException("The regular expression didn't match the specified text.");
            }

            this.writeOutput("groups", groups);
        } else {
            List<Map<String, Object>> matches = new ArrayList<>();

            while (matcher.find()) {
                List<String> groups = new ArrayList<>();

                Map<String, Object> match = new HashMap<>();
                match.put("fullMatch", matcher.group(0));

                for (int groupNumber = 0; groupNumber <= matcher.groupCount(); groupNumber++) {
                    match.put(String.format("group%d", groupNumber), matcher.group(groupNumber));
                    groups.add(matcher.group(groupNumber));
                }
                match.put("groups", groups);
                matches.add(match);
            }

            if (matches.size() == 0) {
                throw new RuntimeException("The regular expression didn't match the specified text.");
            }

            this.writeOutput("matches", matches);
        }

        this.writeOutput("isFullMatch", matcher.hitEnd());
    }
}