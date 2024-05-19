package com.inturn.pfit.domain.item.entity;

import com.inturn.pfit.domain.brand.entity.Brand;
import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity(name = "item")
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ItemEntity extends Item {

	@ManyToOne
	@JoinColumn(name = "categoryId", insertable = false, updatable = false)
	private Category category;


	@ManyToOne
	@JoinColumn(name = "brandId", insertable = false, updatable = false)
	private Brand brand;

	@OneToMany(mappedBy = "item")
	private List<ItemSizeEntity> itemSizeList;

}
