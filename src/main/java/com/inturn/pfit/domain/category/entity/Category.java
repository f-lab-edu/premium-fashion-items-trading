package com.inturn.pfit.domain.category.entity;

import com.inturn.pfit.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "category")
@Getter
@SuperBuilder
@NoArgsConstructor
public class Category extends CommonEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoryId;

	@Column(nullable = false)
	private String categoryName;

	@Column(nullable = false)
	private Integer categorySort;

}
