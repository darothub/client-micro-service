package com.darothub.clientmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
//    private String thumbnail;
    private String emailAddress;
    private String phoneNumber;
    private String gender;
    private String deliveryAddress;
//    private String username;
}
