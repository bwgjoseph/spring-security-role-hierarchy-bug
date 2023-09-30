package com.bwgjoseph.springsecurityrolehierarchybug;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@EnableMethodSecurity
@EnableWebSecurity(debug = false)
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(httpReq -> httpReq.anyRequest().access(haveReadPermission()))
            .build();
    }

    // test fails when enable this as @Bean
    // @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        String roleHierarchyFromMap = """
                ROLE_ADMIN > ROLE_STAFF
                ROLE_STAFF > ROLE_USER
                ROLE_STAFF > ROLE_GUEST
                """;

        roleHierarchy.setHierarchy(roleHierarchyFromMap);
        return roleHierarchy;
    }

    private AuthorizationManager<RequestAuthorizationContext> haveReadPermission() {
        AuthorityAuthorizationManager<RequestAuthorizationContext> authority = AuthorityAuthorizationManager.hasAnyAuthority("ROLE_USER");
        authority.setRoleHierarchy(this.roleHierarchy());
        return authority;
    }
}
