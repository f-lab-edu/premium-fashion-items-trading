package com.inturn.pfit.domain.brand.repository;

import com.inturn.pfit.domain.brand.dto.request.BrandPagingRequestDTO;
import com.inturn.pfit.domain.brand.dto.response.BrandPagingResponseDTO;
import com.inturn.pfit.domain.brand.entity.QBrand;
import com.inturn.pfit.global.support.querydsl.PfitQuerydslRepositorySupport;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BrandRepositoryImpl extends PfitQuerydslRepositorySupport implements BrandRepositoryDsl {

	QBrand qBrand = QBrand.brand;

	public BrandRepositoryImpl() {
		super(QBrand.class);
	}

	@Override
	public Page<BrandPagingResponseDTO> getPagingList(BrandPagingRequestDTO req, Pageable pageable) {
		return pagingList(from(qBrand)
				.where(
						eq(qBrand.brandId, req.brandId()),
						like(qBrand.brandName, req.brandName())
				)
				.select(Projections.fields(BrandPagingResponseDTO.class,
						qBrand.brandId,
						qBrand.brandName,
						qBrand.createdDt,
						qBrand.updatedDt
				))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(getOrderSpecifiers(qBrand, pageable.getSort())), pageable);
	}
}
