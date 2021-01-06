package com.darothub.clientmicroservice.entity;

import com.darothub.clientmicroservice.utils.ConstantUtils;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Size(min = 3, max = 46)
    @NotBlank(message = "First name must not be blank")
    @Pattern(regexp = ConstantUtils.CHAR_PATTERN, message = "Please enter characters only")
    private String firstName;
    @Size(min = 3, max = 46)
    @NotBlank(message = "Last name is blank")
    @NotNull
    private String lastName;
    private String thumbnail;
    @Email
    @NotBlank(message = "Email address must not be blank")
    @NotNull
    private String emailAddress;
    private String phoneNumber;
    @Pattern(regexp = ConstantUtils.GENDER_PATTERN, message = "Invalid gender type")
    private String gender;
    private String deliveryAddress;

}
