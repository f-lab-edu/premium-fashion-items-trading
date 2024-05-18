package com.inturn.pfit.domain.address.entity;

import com.inturn.pfit.domain.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "address")
@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity extends Address {

	@ManyToOne
	@JoinColumn(name = "userId", updatable = false, insertable = false)
	private UserEntity user;
}
