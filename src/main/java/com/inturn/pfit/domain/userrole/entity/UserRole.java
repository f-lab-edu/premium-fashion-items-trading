package com.inturn.pfit.domain.userrole.entity;

import com.inturn.pfit.global.common.entity.CommonEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity(name = "user_role")
public class UserRole extends CommonEntity {

	@Id
	private String roleCode;

	private String roleName;
}
