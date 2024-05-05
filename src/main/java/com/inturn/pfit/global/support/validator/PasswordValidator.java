package com.inturn.pfit.global.support.validator;

import com.inturn.pfit.global.support.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

public class PasswordValidator implements ConstraintValidator<Password, String> {

	private final Integer MIN = 8;
	private final Integer MAX = 15;

	//영어 대문자, 특수문자, 숫자 포함 8이상 Password 정규식
	private final String PASSWORD_REGEXP = "^(?=.*[A-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{" + MIN + "," + MAX +"}$";

	private final String message = String.format("%d자 이상의 %d자 이하의 숫자, 영문 대/소문자, 특수문자를 포함한 비밀번호를 입력해주세요", MIN, MAX);

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if(StringUtils.isEmpty(password))   return true;
		boolean isValidPassword = password.matches(PASSWORD_REGEXP);
		if (!isValidPassword) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
		}
		return isValidPassword;
	}
}
