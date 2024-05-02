package com.inturn.pfit.domain.user.entity;


import com.inturn.pfit.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class User extends CommonEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	private String userPhone;

	private String userName;

	private String profileName;

	private String profileUrl;

	@Column(nullable = false)
	private Long userPoint;

	@Column(nullable = false, length = 1)
	private String alarmYn;

	@Column(nullable = false)
	private String roleCode;

	public void changeUserInfo(String userPhone, String userName, String profileName, String profileUrl, String alarmYn){
		this.userPhone = userPhone;
		this.userName = userName;
		this.profileName = profileName;
		this.profileUrl = profileUrl;
		this.alarmYn = alarmYn;
	}

	public void changePassword(String password, PasswordEncoder encoder) {
		this.password = encoder.encode(password);

	}
}
