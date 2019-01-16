/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.utils;


import org.jvalue.commons.rest.RestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class RequestValidator {

	private static Validator validator;

	public static Validator getInstance() {
		if (validator == null) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
		}

		return validator;
	}


	public static <T> void validate(T obj) {
		Set<ConstraintViolation<T>> violations = getInstance().validate(obj);

		if (!violations.isEmpty()) {
			StringBuilder violationStringBuilder = new StringBuilder();
			for (ConstraintViolation<T> violation : violations) {
				violationStringBuilder.append(violation.getPropertyPath().toString())
					.append(" ")
					.append(violation.getMessage())
					.append(System.lineSeparator());
			}
			throw RestUtils.createJsonFormattedException("Malformed " + obj.getClass().getSimpleName() + ": "  + violationStringBuilder.toString(), 400);
		}
	}

}
