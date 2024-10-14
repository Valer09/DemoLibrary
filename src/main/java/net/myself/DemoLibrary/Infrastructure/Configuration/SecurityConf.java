package net.myself.DemoLibrary.Infrastructure.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConf{
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
						.authorizeHttpRequests((authz) -> authz
										.requestMatchers("/authentication/login").permitAll()
										.anyRequest().authenticated())
						.csrf(AbstractHttpConfigurer::disable)
						.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
		return http.build();
	}
}