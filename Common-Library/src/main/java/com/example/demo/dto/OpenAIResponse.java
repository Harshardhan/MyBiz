package com.example.demo.dto;

import java.awt.Choice;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OpenAIResponse {

	private List<Choice> choices;

    // Nested Choice class
    public static class Choice {
        private OpenAIMessage message;

        public OpenAIMessage getMessage() {
            return message;
        }

        public void setMessage(OpenAIMessage message) {
            this.message = message;
        }
    }
}
