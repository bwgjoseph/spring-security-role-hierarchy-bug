package com.bwgjoseph.springsecurityrolehierarchybug;

import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class WithMockCustomUserSecurityContextFactory
	implements WithSecurityContextFactory<WithMockUser> {
	@Override
	public SecurityContext createSecurityContext(WithMockUser customUser) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		MyUserDetails principal =
			new MyUserDetails(customUser.username(), customUser.email(), Arrays.asList(customUser.roles()), Arrays.asList(customUser.authorities()));
		Authentication auth = new PreAuthenticatedAuthenticationToken(principal, "N/A", principal.getAuthorities());
		context.setAuthentication(auth);
		return context;
	}
}
