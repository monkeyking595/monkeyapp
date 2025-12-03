package com.thaimei.myapp.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserInfoDto {
    @NotBlank(message="Name cannot be empty")
    private String fullname;
    @Email(message="Invalid email format")
    private String email;
    @NotBlank(message="Phone cannot be empty")
    @Pattern(regexp="\\d{10}", message="phone must be exactly 10 digits")
    private String phone;
    @NotNull(message="Age cannot be empty")
    @Min(value=1, message="Age must be at least 1")
    @Max(value=120, message="Age must be at most 120")
    private int age;
    @NotBlank(message="Gender cannot be empty")
    private String gender;
    @NotBlank(message="Country cannot be empty")
    private String country;
    @NotBlank(message="City cannot be empty")
    private String city;
    @NotBlank(message="State cannot be empty")
    private String state;
    @NotBlank(message="zip cannot be empty")
    private String zip;
    @NotBlank(message="Locality cannot be empty")
    private String locality;

    
}
