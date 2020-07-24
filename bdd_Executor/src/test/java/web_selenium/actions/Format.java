package web_selenium.actions;

import web_selenium.base.TestAction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds a string, starting from an initial template that interpolates JS
 * expressions and substituting the expressions with their evaluated result.
 */
public class Format extends TestAction {

    @Override
    public void run() {
        super.run();

        String template = this.readStringArgument("template");

        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer(template.length());
        while (matcher.find()) {
            String text = matcher.group(1);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(this.getActor().evalScript(text).toString()));
        }
        matcher.appendTail(sb);

        this.writeOutput("text", sb.toString());
    }
}
