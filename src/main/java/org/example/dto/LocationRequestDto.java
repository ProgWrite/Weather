package org.example.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LocationRequestDto {

    @NotBlank(message = "The location name can't be blank")
    String name;
}
