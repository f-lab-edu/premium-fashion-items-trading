package com.inturn.pfit.support.annotation;

import com.inturn.pfit.support.vo.TestTypeConsts;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@ActiveProfiles("junit")
@AutoConfigureMockMvc
@Tag(TestTypeConsts.INTEGRATION_TEST)
public @interface IntegrationTest {
}
