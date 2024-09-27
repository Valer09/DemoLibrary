package net.myself.DemoLibrary.Infrastructure.Configuration;

import com.google.gson.Gson;
import net.myself.DemoLibrary.Infrastructure.CorrelationIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private CorrelationIdInterceptor correlationIdInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(correlationIdInterceptor);
	}
}
