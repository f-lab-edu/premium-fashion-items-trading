package com.inturn.pfit.domain.item.entity;

import com.inturn.pfit.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemId;

	@Column(nullable = false)
	private String itemName;

	@Column(length = 512)
	private String itemComment;
	private String modelNo;
	private String gender;
	private String imgUrl;

	@Column(nullable = false)
	private Integer categoryId;

	@Column(nullable = false)
	private Integer brandId;

	@Column(nullable = false)
	private Long retailPrice;

	@Column(nullable = false, length = 1)
	private String displayYn;

	private LocalDate releaseDate;

}
