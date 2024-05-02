package com.inturn.pfit.domain.userrole.entity;

import com.inturn.pfit.global.common.entity.CommonEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "user_role")
public class UserRole extends CommonEntity {

	@Id
	private String roleCode;

	private String roleName;
}
