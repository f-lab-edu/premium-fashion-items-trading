package com.inturn.pfit.domain.item.facade;

import com.inturn.pfit.domain.brand.service.BrandQueryService;
import com.inturn.pfit.domain.category.service.CategoryQueryService;
import com.inturn.pfit.domain.item.dto.request.CreateItemRequestDTO;
import com.inturn.pfit.domain.item.dto.response.CreateItemResponseDTO;
import com.inturn.pfit.domain.item.entity.ItemEntity;
import com.inturn.pfit.domain.item.service.ItemCommandService;
import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import com.inturn.pfit.domain.itemsize.service.ItemSizeCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateItemFacade {
	
	private final ItemCommandService itemCommandService;

	private final ItemSizeCommandService itemSizeCommandService;

	private final CategoryQueryService categoryQueryService;

	private final BrandQueryService brandQueryService;

	@Transactional
	public CreateItemResponseDTO createItem(final CreateItemRequestDTO req) {

		final ItemEntity saveItem = req.convertItem();

		validateCreateItem(req);

		ItemEntity item = itemCommandService.save(saveItem);

		List<ItemSizeEntity> itemSizeList = req.convertItemSizeList(item.getItemId());

		itemSizeCommandService.saveAll(itemSizeList);

		return CreateItemResponseDTO.from(item);
	}

	private void validateCreateItem(CreateItemRequestDTO req) {
		req.validateDuplicateItemSizeOrder();

		categoryQueryService.validateExistCategoryById(req.categoryId());

		brandQueryService.validateExistBrandById(req.brandId());
	}
}
