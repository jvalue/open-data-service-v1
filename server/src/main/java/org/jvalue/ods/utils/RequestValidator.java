package org.jvalue.ods.utils;



import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
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


	public static <T> Set<ConstraintViolation<T>> validate(T obj) {
		return getInstance().validate(obj);
	}

}
