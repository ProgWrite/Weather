package org.example.dto;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.validation.UniqueLogin;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRegistrationRequestDto {


    @NotBlank(message = "The name can't be blank")
    @Size(min = 3, max = 30, message = "The name must be between 3 and 30 characters long")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ]+$", message = "The name must contain only letters of the English or Russian alphabet")
    @UniqueLogin
    private String login;


    @NotBlank(message = "The password can't be blank")
    @Size(min = 3, max = 30, message = "The password must be between 3 and 30 characters long")
    private String password;

    @NotBlank(message = "The password can't be blank")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9]+$", message = "The password must contain only letters and numbers")
    private String confirmPassword;

    @AssertTrue(message = "Passwords don't match! Please try again")
    public boolean isPasswordsMatch() {
        return  password != null && password.equals(confirmPassword);
    }

}
