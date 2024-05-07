package com.inturn.pfit.domain.category.entity;

import com.inturn.pfit.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

@Entity(name = "category")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Category extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoryId;

	@Column(nullable = false)
	private String categoryName;

	@Column(nullable = false)
	private Integer categorySort;
}