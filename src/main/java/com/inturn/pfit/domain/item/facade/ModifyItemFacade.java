package com.inturn.pfit.domain.item.facade;

import com.inturn.pfit.domain.brand.service.BrandQueryService;
import com.inturn.pfit.domain.category.service.CategoryQueryService;
import com.inturn.pfit.domain.item.dto.request.ModifyItemRequestDTO;
import com.inturn.pfit.domain.item.dto.response.ItemResponseDTO;
import com.inturn.pfit.domain.item.entity.ItemEntity;
import com.inturn.pfit.domain.item.service.ItemCommandService;
import com.inturn.pfit.domain.item.service.ItemQueryService;
import com.inturn.pfit.domain.itemsize.dto.request.ModifyItemSizeRequestDTO;
import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import com.inturn.pfit.domain.itemsize.exception.DuplicateItemSizeOrderException;
import com.inturn.pfit.domain.itemsize.exception.ItemSizeNotFoundException;
import com.inturn.pfit.domain.itemsize.service.ItemSizeCommandService;
import com.inturn.pfit.domain.itemsize.service.ItemSizeQueryService;
import com.inturn.pfit.global.common.vo.CUDRequestCommand;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModifyItemFacade {

	private final ItemCommandService itemCommandService;

	private final ItemQueryService itemQueryService;

	private final ItemSizeCommandService itemSizeCommandService;

	private final ItemSizeQueryService itemSizeQueryService;

	private final CategoryQueryService categoryQueryService;
	private final BrandQueryService brandQueryService;

	@Transactional
	public ItemResponseDTO modifyItem(ModifyItemRequestDTO req) {

		//해당 size가 존재하는지 확인
		ItemEntity item = itemQueryService.getItemById(req.itemId());

		//validate 처리
		validateModifyItem(req, item);

		//size 편집
		ItemEntity modSize = itemCommandService.save(req.convertItem(item));

		//itemSize에 대한 CUD 처리
		itemSizeCommandService.saveCUDAll(item, req.itemSizeList());

		//TODO - 별도로 조회하지 않을 수 있는 방법이 있을지 추후 확인.
		//itemSize 목록 조회 - 1차 캐시 영역에 존재하는 데이터가 아니기에 조회하여 set
		List<ItemSizeEntity> itemSizeList = itemSizeQueryService.getItemSizeListBySizeId(item.getItemId());

		return ItemResponseDTO.from(modSize, itemSizeList);
	}

	private void validateModifyItem(ModifyItemRequestDTO req, ItemEntity item) {

		//TODO - 이미 해당 카테고리로 상품이 등록된 경우는 validate 추가 -> 상품 개발 후 해당 내용 개발 진행
		categoryQueryService.validateExistCategoryByOrder(req.categoryId());

		brandQueryService.validateExistBrandById(req.brandId());

		List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList = req.itemSizeList();

		//수정 / 삭제할 itemSize이 기 등록된 데이터가 아닐 경우 validate
		validateExistItemSizeForRequestUpdateAndDelete(item, modifyItemSizeRequestDTOList);

		validateDuplicateItemSize(item, modifyItemSizeRequestDTOList);
	}

	private void validateDuplicateItemSize(ItemEntity item, List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList) {
		HashSet<Integer> itemSizeOrderMap = new HashSet<>();
		//기 등록된 itemSizeOrder와 수정 요청온 itemSizeOrder 가 중복되는지 확인.
		for(ItemSizeEntity itemSize : item.getItemSizeList()){
			Integer itemSizeOrder = findItemSizeOrder(modifyItemSizeRequestDTOList, itemSize);
			if(ObjectUtils.isNotEmpty(itemSizeOrder) && !itemSizeOrderMap.add(itemSizeOrder)) {
				throw new DuplicateItemSizeOrderException();
			}
		}

		//CREATE는 별도 비교
		if(!modifyItemSizeRequestDTOList.stream().filter(o -> CUDRequestCommand.CREATE.equals(o.cudRequestCommand())).map(ModifyItemSizeRequestDTO::itemSizeOrder)
				.allMatch(itemSizeOrderMap::add)) {
			throw new DuplicateItemSizeOrderException();
		}
	}

	private void validateExistItemSizeForRequestUpdateAndDelete(ItemEntity item, List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList) {
		boolean isExistItemSize = modifyItemSizeRequestDTOList.stream().filter(o -> ObjectUtils.isNotEmpty(o.itemSizeId())).
				allMatch(reqItemSize -> item.getItemSizeList().stream().anyMatch(itemSize -> itemSize.getItemSizeId().equals(reqItemSize.itemSizeId())));
		//모두 일치한다면(true) 통과, 아니라면 Exception 발생
		if(!isExistItemSize) {
			throw new ItemSizeNotFoundException();
		}
	}

	private Integer findItemSizeOrder(List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList, ItemSizeEntity itemSize) {
		Optional<ModifyItemSizeRequestDTO> modifyItemSizeOpt = modifyItemSizeRequestDTOList.stream().filter(o -> itemSize.getItemSizeId().equals(o.itemSizeId())).findFirst();
		return modifyItemSizeOpt.isPresent() ? CUDRequestCommand.UPDATE.equals(modifyItemSizeOpt.get().cudRequestCommand()) ? modifyItemSizeOpt.get().itemSizeOrder() : null
				: itemSize.getItemSizeOrder();
	}
}
