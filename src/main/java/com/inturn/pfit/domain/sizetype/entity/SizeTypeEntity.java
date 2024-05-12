package com.inturn.pfit.domain.sizetype.entity;

import com.inturn.pfit.domain.size.entity.SizeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "size_type")
@Getter
@SuperBuilder
@NoArgsConstructor
public class SizeTypeEntity extends SizeType{

	@ManyToOne
	@JoinColumn(name = "sizeId", insertable = false, updatable = false)
	private SizeEntity size;
}
