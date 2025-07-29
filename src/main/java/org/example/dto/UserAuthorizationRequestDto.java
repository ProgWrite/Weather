package org.example.dto;


import lombok.*;
import org.example.validation.ExistingLogin;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserAuthorizationRequestDto {

    @ExistingLogin
    private String login;

    private String password;


}
