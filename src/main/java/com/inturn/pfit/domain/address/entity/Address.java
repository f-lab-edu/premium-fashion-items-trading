package com.inturn.pfit.domain.address.entity;

import com.inturn.pfit.global.common.entity.BaseTimeEntity;
import com.inturn.pfit.global.support.utils.PfitConsts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String recipients;

	@Column(nullable = false, length = 13)
	private String phone;

	@Column(nullable = false, length = 6)
	private String postCode;

	@Column(nullable = false)
	private String address;

	private String addressDetail;

	@Column(nullable = false, length = 1)
	private String defaultYn;
	
	public void setDefaultN() {
		this.defaultYn = PfitConsts.CommonCodeConsts.YN_N;
	}
}
