package com.inturn.pfit.domain.size.entity;

import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity(name = "size")
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class SizeEntity extends Size {

	@ManyToOne
	@JoinColumn(name = "categoryId", insertable = false, updatable = false)
	private Category category;

	//sizeType은 size에 기준으로 항상 조회되는 요소이기에 OneToMany로 사용
	//N + 1은 default_batch_size를 통하여 IN으로 조회되도록 처리
	@OneToMany(mappedBy = "size", cascade = CascadeType.REFRESH)
	private List<SizeTypeEntity> sizeTypeList;

}
