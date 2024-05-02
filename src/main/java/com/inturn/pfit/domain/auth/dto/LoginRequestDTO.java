package com.inturn.pfit.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

/*
클라이언트 요청은 record class 사용
일반 class 와는 다르게 All 생성자, getter, hashCode, equals, toString을 제공
getter는 filed 명과 같이 생성 - email(O), getEmail(X)
field와 class는 모두 불변
*/

@Builder
public record LoginRequestDTO (
		/* 
		@NotNull vs @NotEmpty
		@NotNull은 null 여부만 검증, @NotEmpty 는 해당 값이 Null이 아니고, 빈 스트링("")이 아닌지 검증(" "는 허용됨)
		이에 @NotEmpty를 사용
		*/
		@NotEmpty
		@Email
		String email,
		@NotEmpty
		String password
) {

}
