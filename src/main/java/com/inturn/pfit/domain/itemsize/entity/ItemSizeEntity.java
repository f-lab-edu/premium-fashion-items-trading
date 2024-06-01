package com.inturn.pfit.domain.itemsize.entity;

import com.inturn.pfit.domain.item.entity.ItemEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "item_size")
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ItemSizeEntity extends ItemSize {

	@ManyToOne
	@JoinColumn(name = "itemId", insertable = false, updatable = false)
	private ItemEntity item;

}
