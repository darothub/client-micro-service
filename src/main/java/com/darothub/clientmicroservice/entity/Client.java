package com.darothub.clientmicroservice.entity;

import com.darothub.clientmicroservice.utils.ConstantUtils;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_seq", initialValue = 1, allocationSize=1)
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    @Pattern(regexp = ConstantUtils.CHAR_PATTERN, message = "Please enter characters only")
    private String firstName;
    @Size(min = 3, max = 46)
    @NotNull
    private String lastName;
    @Size(min = 3, max = 20)
    @Email
    @NotBlank(message = "Email address must not be blank")
    @NotNull
    private String emailAddress;
    private String phoneNumber;
    @Pattern(regexp = ConstantUtils.GENDER_PATTERN, message = "Invalid gender type")
    private String gender;
    private String deliveryAddress;
    //    @NotNull
//    private String username;
    //    @Pattern(regexp = ConstantUtils.PASSWORD, message = "Password does not match")
//    @NotNull
//    private String password;
//    private String thumbnail;

}
