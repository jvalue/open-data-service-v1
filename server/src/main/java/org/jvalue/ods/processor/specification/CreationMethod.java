/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.specification;


import org.jvalue.ods.api.processors.ProcessorType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target({ METHOD }) @Retention(RetentionPolicy.RUNTIME)
public @interface CreationMethod {
	String name();
	ProcessorType filterType();
}
