package com.inturn.pfit.domain.brand.entity;

import com.inturn.pfit.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

@Entity(name = "brand")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Brand extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer brandId;

	@Column(nullable = false)
	private String brandName;

}
