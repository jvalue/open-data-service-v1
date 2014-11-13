package org.jvalue.ods.main;


import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

@BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RetentionPolicy.RUNTIME)
public @interface GrabberUpdateInterval { }
