package com.inturn.pfit.domain.category.repository;

import com.inturn.pfit.domain.category.dto.request.CategoryPagingRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CategoryPagingResponseDTO;
import com.inturn.pfit.domain.category.entity.QCategory;
import com.inturn.pfit.global.support.querydsl.PfitQuerydslRepositorySupport;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CategoryRepositoryDslImpl extends PfitQuerydslRepositorySupport implements CategoryRepositoryDsl{

	QCategory qCategory = QCategory.category;
	public CategoryRepositoryDslImpl() {
		super(QCategory.class);
	}

	@Override
	public Page<CategoryPagingResponseDTO> getPagingList(CategoryPagingRequestDTO req, Pageable pageable) {
		JPQLQuery<CategoryPagingResponseDTO> query = from(qCategory)
				.where(
						eq(qCategory.categoryId, req.getCategoryId()),
						eq(qCategory.categoryName, req.getCategoryName()),
						eq(qCategory.categorySort, req.getCategorySort())
				)
				.select(Projections.bean(CategoryPagingResponseDTO.class,
						qCategory.categoryId,
						qCategory.categoryName,
						qCategory.categorySort
						))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(getOrderSpecifiers(qCategory, pageable.getSort()));

		return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
	}
}
