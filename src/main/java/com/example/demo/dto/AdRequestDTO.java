package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100)
    private String title;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double priceEur;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Seller name is required")
    private String sellerName;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000)
    private String description;


}