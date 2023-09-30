package com.bwgjoseph.springsecurityrolehierarchybug;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
// https://docs.spring.io/spring-security/reference/5.8/servlet/test/method.html#test-method-meta-annotations
public @interface WithMockUser {

	String username() default "rob";

	String email() default "rob@google.com";

	String[] roles() default {};

	String[] authorities() default {};
}
