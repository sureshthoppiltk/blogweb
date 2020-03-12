package com.blog.blogservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.blog.blogservice.service.AdminServiceImpl;

@SpringBootApplication
public class BlogServiceApplication {

	private static Logger logger = LoggerFactory.getLogger(BlogServiceApplication.class);
	@Bean
	public FilterRegistrationBean<CorsFilter> processCorsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);

		final FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	public static void main(String[] args) {
		SpringApplication.run(BlogServiceApplication.class, args);
	}
   
	public void run(ApplicationArguments args) throws Exception {
	
       
        boolean containsOption = args.containsOption("jwt.security");
        logger.info("jwt.security : " + containsOption);
       
	}
	
}
