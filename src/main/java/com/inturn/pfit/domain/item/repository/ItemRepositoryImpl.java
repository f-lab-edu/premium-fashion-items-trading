package com.inturn.pfit.domain.item.repository;


import com.inturn.pfit.domain.brand.entity.QBrand;
import com.inturn.pfit.domain.category.entity.QCategory;
import com.inturn.pfit.domain.item.dto.request.ItemPagingRequestDTO;
import com.inturn.pfit.domain.item.dto.response.ItemResponseDTO;
import com.inturn.pfit.domain.item.entity.QItemEntity;
import com.inturn.pfit.domain.size.entity.QSize;
import com.inturn.pfit.global.support.querydsl.PfitQuerydslRepositorySupport;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ItemRepositoryImpl extends PfitQuerydslRepositorySupport implements ItemRepositoryDsl {

	QItemEntity qItem = QItemEntity.itemEntity;

	QCategory qCategory = QCategory.category;
	QBrand qBrand = QBrand.brand;

	public ItemRepositoryImpl() {
		super(QSize.class);
	}

	@Override
	public Page<ItemResponseDTO> getPagingList(ItemPagingRequestDTO req, Pageable pageable) {
		return pagingList(from(qItem)
				.where(
						like(qItem.itemName, req.itemName()),
						like(qItem.modelNo, req.modelNo()),
						eq(qItem.gender, req.gender()),
						like(qItem.imgUrl, req.imgUrl()),
						eq(qItem.categoryId, req.categoryId()),
						eq(qItem.brandId, req.brandId()),
						eq(qItem.displayYn, req.displayYn()),
						//TODO - release 데이터 가능한지 확인
						eq(qItem.releaseDate, req.releaseDate())
				)
				.select(Projections.fields(ItemResponseDTO.class,
						qItem.itemId,
						qItem.itemName,
						qItem.itemComment,
						qItem.modelNo,
						qItem.gender,
						qItem.imgUrl,
						qItem.categoryId,
						qCategory.categoryName,
						qItem.brandId,
						qBrand.brandName,
						qItem.retailPrice,
						qItem.displayYn,
						qItem.releaseDate,
						qItem.createdAt,
						qItem.updatedAt
				))
				.innerJoin(qCategory)
				.on(qCategory.categoryId.eq(qItem.categoryId))
				.innerJoin(qBrand)
				.on(qBrand.brandId.eq(qItem.brandId))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(getOrderSpecifiers(qItem, pageable.getSort())), pageable);
	}
}
