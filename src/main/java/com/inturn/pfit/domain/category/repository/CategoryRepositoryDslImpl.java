package com.inturn.pfit.domain.category.repository;

import com.inturn.pfit.domain.category.dto.request.CategoryPagingRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CategoryPagingResponseDTO;
import com.inturn.pfit.domain.category.entity.QCategory;
import com.inturn.pfit.global.support.querydsl.PfitQuerydslRepositorySupport;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CategoryRepositoryDslImpl extends PfitQuerydslRepositorySupport implements CategoryRepositoryDsl{

	QCategory qCategory = QCategory.category;

	public CategoryRepositoryDslImpl() {
		super(QCategory.class);
	}

	@Override
	public Page<CategoryPagingResponseDTO> getPagingList(CategoryPagingRequestDTO req, Pageable pageable) {
		return pagingList(from(qCategory)
				.where(
						eq(qCategory.categoryId, req.getCategoryId()),
						eq(qCategory.categoryName, req.getCategoryName()),
						eq(qCategory.categorySort, req.getCategorySort())
				)
				.select(Projections.fields(CategoryPagingResponseDTO.class,
						qCategory.categoryId,
						qCategory.categoryName,
						qCategory.categorySort
						))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(getOrderSpecifiers(qCategory, pageable.getSort())), pageable);
	}
}
