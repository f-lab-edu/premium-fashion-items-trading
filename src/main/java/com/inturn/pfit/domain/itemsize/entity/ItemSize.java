package com.inturn.pfit.domain.itemsize.entity;

import com.inturn.pfit.global.common.entity.BaseTimeEntity;
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
public class ItemSize extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemSizeId;

	@Column(nullable = false)
	private Long itemId;

	@Column(nullable = false)
	private String itemSizeName;

	@Column(nullable = false)
	private Integer itemSizeOrder;

}
