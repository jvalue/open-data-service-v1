package org.jvalue.ods.processor.reference;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = ChainReferenceValidator.class)
@Documented
public @interface ValidChainReference {

	String message() default "fail";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}