package com.inturn.pfit.domain.user.entity;

import com.inturn.pfit.domain.userrole.entity.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity(name = "user")
@Getter
@SuperBuilder
@NoArgsConstructor
public class UserEntity extends User{

	@ManyToOne
	@JoinColumn(name = "roleCode", updatable = false, insertable = false)
	public UserRole userRole;

	public static UserEntity signUpUser(String email, String password, String alarmYn, String roleCode, PasswordEncoder encoder) {
		//등록시 사용자명, 프로필명은 random으로 등록
		String name = RandomStringUtils.randomAlphanumeric(10);
		return UserEntity.builder()
				.email(email)
				.password(encoder.encode(password))
				.alarmYn(alarmYn)
				.roleCode(roleCode)
				.userPoint(0l)
				.userName(name)
				.profileName(name)
				.build();
	}

}
