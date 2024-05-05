package com.inturn.pfit.support.annotation;

import com.inturn.pfit.support.security.PfitSecurityTestConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//선언할 경우 다음 내용만 스캔하도록 제한한다.
//@Controller
//@ControllerAdvice
//@JsonComponent
//Converter
//GenericConverter
//Filter
//WebMvcConfigurer
//WebSecurityConfigurerAdapter
//HandlerMethodArgumentResolver
@WebMvcTest(
		includeFilters = {
				//별도의 Test용 SecurityConfig를 생성하여 해당 객체를 통하여 테스트 가능하도록 한다.
				//TODO - FilterType.ASSIGNABLE_TYPE 확인
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = PfitSecurityTestConfig.class
				)}
)
public @interface PfitSecurityConfigTest {
	@AliasFor("controllers")
	Class<?>[] value() default {};
	@AliasFor("value")
	Class<?>[] controllers() default {};
}
