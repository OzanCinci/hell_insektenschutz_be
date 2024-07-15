package com.ozan.be.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.ozan.be.auth.JwtAuthenticationFilter;
import com.ozan.be.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;
  private final String[] managementRoles = {Role.ADMIN.name(), Role.MANAGER.name()};
  private final String[] allRoles = {Role.ADMIN.name(), Role.MANAGER.name(), Role.USER.name()};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    publicRequests(http);
    secureExternalProducts(http);
    secureManagements(http);
    secureUserOperations(http);
    secureReviewOperations(http);
    secureOrderOperations(http);

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(req -> req.anyRequest().authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(
            logout ->
                logout
                    .logoutUrl("/api/auth/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler(
                        (request, response, authentication) ->
                            SecurityContextHolder.clearContext()));

    return http.build();
  }

  private void publicRequests(HttpSecurity http) throws Exception {
    String[] WHITE_LIST_URL = {"/api/auth/**", "/api/product/*", "/api/product/**"};

    http.authorizeHttpRequests((requests) -> requests.requestMatchers(WHITE_LIST_URL).permitAll());
  }

  private void secureExternalProducts(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        (requests) ->
            requests
                .requestMatchers(HttpMethod.GET, "/api/external-products/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/external-products/**")
                .permitAll());
  }

  private void secureManagements(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        (requests) ->
            requests
                .requestMatchers(HttpMethod.GET, "/api/management/**")
                .hasAnyRole(managementRoles)
                .requestMatchers(HttpMethod.PUT, "/api/management/**")
                .hasAnyRole(managementRoles)
                .requestMatchers(HttpMethod.DELETE, "/api/management/**")
                .hasAnyRole(managementRoles));
  }

  private void secureUserOperations(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        (requests) ->
            requests
                .requestMatchers(HttpMethod.GET, "/api/users/*")
                .hasAnyRole(allRoles)
                .requestMatchers(HttpMethod.PUT, "/api/users/change-password")
                .hasAnyRole(allRoles));
  }

  private void secureReviewOperations(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        (requests) ->
            requests
                .requestMatchers(HttpMethod.POST, "/api/review")
                .hasAnyRole(allRoles)
                .requestMatchers(HttpMethod.GET, "api/review")
                .permitAll());
  }

  private void secureOrderOperations(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        (requests) ->
            requests.requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole(allRoles));
  }
}
