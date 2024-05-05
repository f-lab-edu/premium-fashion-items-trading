package com.inturn.pfit.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record LoginRequestDTO (
		@NotEmpty
		@Email
		String email,
		@NotEmpty
		String password
) {

}
