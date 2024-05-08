package com.inturn.pfit.domain.user.entity;


import com.inturn.pfit.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
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
}
