package com.inturn.pfit.domain.category.entity;

import com.inturn.pfit.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "category")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoryId;

	@Column(nullable = false)
	private String categoryName;

	@Column(nullable = false)
	private Integer categorySort;

	public static Category createCategory(String categoryName, Integer categorySort) {
		return Category.builder()
				.categoryName(categoryName)
				.categorySort(categorySort)
				.build();
	}

	public void modifyCategory(String categoryName, Integer categorySort) {
		this.categoryName = categoryName;
		this.categorySort = categorySort;
	}
}
