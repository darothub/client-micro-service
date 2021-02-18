package com.darothub.clientmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artisan {
    private Long id;
    private String firstName;
    private String lastName;
    private String category;
    private String thumbnail;
    private String emailAddress;
    private String phoneNumber;
    private String gender;
    private String country;
}
