package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageRequestDTO {
    @NotBlank(message = "Message text cannot be blank")
    @Size(min = 5, max = 2000, message = "Message must be between 5 and 2000 characters")
    private String text;

}