package com.inturn.pfit.global.support.annotation;

import com.inturn.pfit.global.support.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.TYPE,
		ElementType.FIELD,
		ElementType.ANNOTATION_TYPE,
		ElementType.CONSTRUCTOR,
		ElementType.PARAMETER,
		ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

	String message() default "Password is not allow";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
