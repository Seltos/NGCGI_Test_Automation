package web_selenium.annotations;

import java.lang.annotation.Repeatable;

@Repeatable(TestActionOutputs.class)
public @interface TestActionOutput {
    String name();
    Type type();
    String description() default "";
}