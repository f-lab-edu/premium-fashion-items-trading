package com.inturn.pfit.domain.brand.repository;

import com.inturn.pfit.domain.brand.entity.QBrand;
import com.inturn.pfit.global.support.querydsl.PfitQuerydslRepositorySupport;

public class BrandRepositoryImpl extends PfitQuerydslRepositorySupport implements BrandRepositoryDsl {

	QBrand qBrand = QBrand.brand;

	public BrandRepositoryImpl() {
		super(QBrand.class);
	}
}
