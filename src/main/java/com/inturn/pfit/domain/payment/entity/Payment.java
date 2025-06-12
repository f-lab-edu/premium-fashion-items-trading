package com.inturn.pfit.domain.payment.entity;

import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "payment")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends CommonTimeDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String cardNumber;

	@Column(nullable = false)
	private String cardCompany;

	@Column(nullable = false)
	private String installmentPlanMonths;

	@Column(nullable = false)
	private String approveNo;

	private Long paymentAmount;

	private String cardType;

	private String ownerType;

	@Column(nullable = false)
	private String acquireStatusCd;

}
