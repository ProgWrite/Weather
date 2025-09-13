package org.example.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LocationRequestDto {
    @NotBlank(message = "The location name can't be blank")
    @Size(max = 60, message = "The location name must not exceed 60 characters.")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\\-]+$", message = "The location name must contain only letters (English or Russian) or hyphen")
    String name;
}
