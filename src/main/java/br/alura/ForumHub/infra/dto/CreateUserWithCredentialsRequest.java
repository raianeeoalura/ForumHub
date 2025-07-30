package br.alura.ForumHub.infra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserWithCredentialsRequest(
    @NotBlank(message = "Name cannot be blank") String name,

    @NotBlank(message = "Username cannot be blank") @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "Username must be 3-20 characters long and can only contain letters, numbers, and underscores") String username,

    @Email(message = "Email must be valid") @NotBlank(message = "Email cannot be blank") String email,
    @Min(message = "Password must be at least 6 characters long", value = 6) @NotBlank(message = "Password cannot be blank") String password) {
}
