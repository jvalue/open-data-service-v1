package org.jvalue.ods.filter.description;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target({ METHOD }) @Retention(RetentionPolicy.RUNTIME)
public @interface FilterArgument {
	String value();
}
