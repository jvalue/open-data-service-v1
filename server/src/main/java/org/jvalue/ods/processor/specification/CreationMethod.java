package org.jvalue.ods.processor.specification;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target({ METHOD }) @Retention(RetentionPolicy.RUNTIME)
public @interface CreationMethod {
	String name();
	ProcessorType filterType();
}
