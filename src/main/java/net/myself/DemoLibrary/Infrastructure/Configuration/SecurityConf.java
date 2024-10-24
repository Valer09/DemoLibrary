package net.myself.DemoLibrary.Infrastructure.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EnableMethodSecurity
@Configuration
public class SecurityConf{
	@Value("${auth0.domain}")
	private String domain;
	@Value("${frontend.domain}")
	private String frontEndDomain;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
						.cors(Customizer.withDefaults())
						.authorizeHttpRequests((authz) -> authz
										.requestMatchers("/authentication/login").permitAll()
										.anyRequest().authenticated())
						.csrf(AbstractHttpConfigurer::disable)
						.oauth2ResourceServer(oauth2 -> oauth2.jwt((conv) -> conv.jwtAuthenticationConverter(jwtAuthenticationConverter())));
		return http.build();
	}
	@Bean
	WebMvcConfigurer corsConfigurer()
	{
		return new WebMvcConfigurer()
		{
			@Override
			public void addCorsMappings(CorsRegistry registry)
			{
				registry.addMapping("/**").allowedOrigins(frontEndDomain);
			}
		};
	}
	
	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(customAuthoritiesConverter());
		return jwtAuthenticationConverter;
	}
	
	private Converter<Jwt, Collection<GrantedAuthority>> customAuthoritiesConverter()
	{
		return jwt -> {
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			Collection<String> roles = jwt.getClaimAsStringList(domain+"/roles");
			if (roles != null)
				roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
			
			Collection<String> permissions = jwt.getClaimAsStringList("permissions");
			if (permissions != null)
				permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
			
			return authorities;
		};
	}
}