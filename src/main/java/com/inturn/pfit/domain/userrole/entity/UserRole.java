package com.inturn.pfit.domain.userrole.entity;

import com.inturn.pfit.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "user_role")
public class UserRole extends BaseTimeEntity {

	@Id
	private String roleCode;

	private String roleName;
}
