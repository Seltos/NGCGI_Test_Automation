package com.enquero.driverfactory.web_selenium.annotations;

import java.lang.annotation.Repeatable;

@Repeatable(TestActionArguments.class)
public @interface TestActionArgument {
    String defaultValue() default "N/A";
    String name();
    Type type();
    String description() default "";
    boolean optional();
}
