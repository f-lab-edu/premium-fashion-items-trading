package com.inturn.pfit.domain.size.repository;


import com.inturn.pfit.domain.category.entity.QCategory;
import com.inturn.pfit.domain.size.dto.request.SizePagingRequestDTO;
import com.inturn.pfit.domain.size.dto.response.SizeResponseDTO;
import com.inturn.pfit.domain.size.entity.QSize;
import com.inturn.pfit.domain.size.entity.QSizeEntity;
import com.inturn.pfit.global.support.querydsl.PfitQuerydslRepositorySupport;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SizeRepositoryImpl extends PfitQuerydslRepositorySupport implements SizeRepositoryDsl {

	QSizeEntity qSize = QSizeEntity.sizeEntity;

	QCategory qCategory = QCategory.category;

	public SizeRepositoryImpl() {
		super(QSize.class);
	}

	@Override
	public Page<SizeResponseDTO> getPagingList(SizePagingRequestDTO req, Pageable pageable) {
		return pagingList(from(qSize)
				.where(
						eq(qSize.sizeId, req.sizeId()),
						like(qSize.sizeName, req.sizeName()),
						eq(qSize.categoryId, req.categoryId())
				)
				.select(Projections.fields(SizeResponseDTO.class,
						qSize.sizeId,
						qSize.categoryId,
						qCategory.categoryName,
						qSize.sizeName,
						qSize.createdAt,
						qSize.updatedAt
				))
				.innerJoin(qCategory)
				.on(qCategory.categoryId.eq(qSize.categoryId))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(getOrderSpecifiers(qSize, pageable.getSort())), pageable);
	}
}
