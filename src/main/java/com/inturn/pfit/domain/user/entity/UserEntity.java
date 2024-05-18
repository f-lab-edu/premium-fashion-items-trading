package com.inturn.pfit.domain.user.entity;

import com.inturn.pfit.domain.address.entity.AddressEntity;
import com.inturn.pfit.domain.userrole.entity.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity(name = "user")
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class UserEntity extends User {

	@ManyToOne
	@JoinColumn(name = "roleCode", updatable = false, insertable = false)
	private UserRole userRole;

	@OneToMany(mappedBy = "user")
	private List<AddressEntity> addressList;


}
