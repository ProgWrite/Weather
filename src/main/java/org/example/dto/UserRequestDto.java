package org.example.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRequestDto {

    @NotBlank(message = "The name can't be blank")
    @Size(min = 3, max = 30, message = "The name must be between 3 and 30 characters long")
    private String login;


    @NotBlank(message = "The password can't be blank")
    @Size(min = 3, max = 30, message = "The password must be between 3 and 30 characters long")
    private String password;

    @NotBlank(message = "The password can't be blank")
    private String confirmPassword;

}
