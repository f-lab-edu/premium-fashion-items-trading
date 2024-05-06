package com.inturn.pfit.domain.user.entity;


import com.inturn.pfit.domain.user.dto.request.ChangeUserInfoRequestDTO;
import com.inturn.pfit.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class User extends BaseTimeEntity {

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

	public void changeUserInfo(ChangeUserInfoRequestDTO req){
		this.userPhone = req.userPhone();
		this.userName = req.userName();
		this.profileName = req.profileName();
		this.profileUrl = req.profileUrl();
		this.alarmYn = req.alarmYn();
	}

	public void changePassword(String password, PasswordEncoder encoder) {
		this.password = encoder.encode(password);
	}
}
