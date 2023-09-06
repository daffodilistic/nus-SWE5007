package com.nus.project.capstone.idc.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true", matchIfMissing = true)
class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Value("${spring.secured}")
    @Autowired
    boolean isSecurityEnabled;

    private static final Logger logger = LogManager.getLogger(KeycloakConfig.class);

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        if (isSecurityEnabled) {
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/**/view**", "/**/upload**").hasAnyRole("participant", "judge")
                    .antMatchers("/**/create**", "/**/update**", "/**/delete**").hasRole("admin")
                    .antMatchers("/idcteam/qualify**").hasRole("judge")
                    .antMatchers("/idcteam/assign-user").hasRole("admin")
                    .antMatchers("/idcteam/assign-score").hasRole("judge")
                    .antMatchers("/idcmetrics/calculate").hasRole("judge")
                    .antMatchers("/idcgroup/assign-team").hasRole("admin")
                    .anyRequest().denyAll();
        } else {
            logger.info("Keycloak Auth is NOT turned on !!");
            http.csrf().disable()
                    .authorizeRequests()
                    .anyRequest().permitAll();
        }
    }
}
