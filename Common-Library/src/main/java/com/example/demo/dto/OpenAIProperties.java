package com.example.demo.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Configuration
@ConfigurationProperties(prefix = "openai.api")
public class OpenAIProperties {
	private String key;
	private String url;

}
