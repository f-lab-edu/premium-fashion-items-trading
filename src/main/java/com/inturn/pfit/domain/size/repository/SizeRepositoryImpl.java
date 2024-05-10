package com.inturn.pfit.domain.size.repository;


import com.inturn.pfit.domain.size.dto.request.SizePagingRequestDTO;
import com.inturn.pfit.domain.size.dto.response.SizePagingResponseDTO;
import com.inturn.pfit.domain.size.entity.QSize;
import com.inturn.pfit.global.support.querydsl.PfitQuerydslRepositorySupport;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SizeRepositoryImpl extends PfitQuerydslRepositorySupport implements SizeRepositoryDsl {

	QSize qSize = QSize.size;

	public SizeRepositoryImpl() {
		super(QSize.class);
	}

	@Override
	public Page<SizePagingResponseDTO> getPagingList(SizePagingRequestDTO req, Pageable pageable) {
		return pagingList(from(qSize)
				.where(
						eq(qSize.sizeId, req.sizeId()),
						like(qSize.sizeName, req.sizeName())
				)
				.select(Projections.fields(SizePagingResponseDTO.class,
						qSize.sizeId,
						qSize.sizeName,
						qSize.createdAt,
						qSize.updatedAt
				))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(getOrderSpecifiers(qSize, pageable.getSort())), pageable);
	}
}
