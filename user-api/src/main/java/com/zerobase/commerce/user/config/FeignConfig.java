package com.zerobase.commerce.user.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

//	@Value(value = "${mailgun.key}")
//	private String mailgunKey;

	@Qualifier(value = "mailgun")
	@Bean
	public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
		return new BasicAuthRequestInterceptor("api", "98b3b318d6c46ac8c30d776a498d6bb4-30344472-4399951f");
	}

}
