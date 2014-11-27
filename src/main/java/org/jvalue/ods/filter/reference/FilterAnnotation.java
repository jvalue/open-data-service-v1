package org.jvalue.ods.filter.reference;


import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@BindingAnnotation @Target({ METHOD }) @Retention(RetentionPolicy.RUNTIME)
public @interface FilterAnnotation {
	String name();
	FilterType filterType();
}
