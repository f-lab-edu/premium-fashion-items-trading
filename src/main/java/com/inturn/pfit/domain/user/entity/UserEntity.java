package com.inturn.pfit.domain.user.entity;

import com.inturn.pfit.domain.userrole.entity.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

@Entity(name = "user")
@Getter
@SuperBuilder
@NoArgsConstructor
@DynamicUpdate
public class UserEntity extends User {

	@ManyToOne
	@JoinColumn(name = "roleCode", updatable = false, insertable = false)
	public UserRole userRole;

}
