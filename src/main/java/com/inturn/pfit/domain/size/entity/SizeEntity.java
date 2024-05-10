package com.inturn.pfit.domain.size.entity;

import com.inturn.pfit.domain.category.entity.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "size")
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class SizeEntity extends Size {

	@ManyToOne
	@JoinColumn(name = "categoryId", insertable = false, updatable = false)
	private Category category;

}
