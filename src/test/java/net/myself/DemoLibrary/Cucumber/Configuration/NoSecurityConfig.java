package net.myself.DemoLibrary.Cucumber.Configuration;

import org.junit.jupiter.api.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Profile("test")
@Configuration
public class NoSecurityConfig
{
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
						.csrf(csrf -> csrf.disable())
						.authorizeHttpRequests((auth) -> auth
										.requestMatchers("/**").permitAll()
										.anyRequest().permitAll());
		return http.build();
	}
}
