package com.example.demo.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIRequest {
	private String model;
	private List<OpenAIMessage> messages;


}
