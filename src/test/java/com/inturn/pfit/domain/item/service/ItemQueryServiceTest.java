package com.inturn.pfit.domain.item.service;

import com.inturn.pfit.domain.item.dto.response.ItemResponseDTO;
import com.inturn.pfit.domain.item.entity.ItemEntity;
import com.inturn.pfit.domain.item.exception.ItemNotFoundException;
import com.inturn.pfit.domain.item.repository.ItemRepository;
import com.inturn.pfit.domain.item.vo.ItemErrorCode;
import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import com.inturn.pfit.global.support.utils.PfitConsts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ItemQueryServiceTest {

	@InjectMocks
	ItemQueryService itemQueryService;

	@Mock
	ItemRepository itemRepository;

	@Test
	@DisplayName("상품 데이터가 존재하는 상품 ID로 상품 조회를 호출하면 성공한다")
	void getItem_Success(){

		//given
		final long itemId = 1l;
		List<ItemSizeEntity> itemSizeList = List.of(
				ItemSizeEntity.builder().itemSizeId(1l).itemSizeName("275").itemSizeOrder(1).build(),
				ItemSizeEntity.builder().itemSizeId(2l).itemSizeName("280").itemSizeOrder(2).build(),
				ItemSizeEntity.builder().itemSizeId(3l).itemSizeName("285").itemSizeOrder(3).build()
		);

		final ItemEntity item = ItemEntity.builder()
				.itemId(itemId)
				.itemName("나이키 에어포스 WHITE")
				.modelNo("CW2288-111")
				.gender("MEN")
				.categoryId(1)
				.brandId(1)
				.retailPrice(139000l)
				.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
				.releaseDate(LocalDate.now())
				.itemSizeList(itemSizeList)
				.build();

		when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

		//when
		ItemResponseDTO res = itemQueryService.getItem(itemId);

		//then
		assertEquals(res.getItemId(), itemId);
		assertEquals(res.getItemSizeList().size(), itemSizeList.size());

		//verify
		verify(itemRepository, times(1)).findById(itemId);
	}

	@Test
	@DisplayName("존재하지 않는 상품 ID로 상품 조회를 호출하면 실패한다.")
	void getItem_Fail() {
		//given
		long itemNotExist = 0l;
		when(itemRepository.findById(itemNotExist)).thenReturn(Optional.empty());

		//when
		final ItemNotFoundException result =  assertThrows(ItemNotFoundException.class, () -> itemQueryService.getItem(itemNotExist));

		//then
		assertEquals(result.getMessage(), ItemErrorCode.ITEM_NOT_FOUND_EXCEPTION.getErrorMessage());

		//verify
		verify(itemRepository, times(1)).findById(itemNotExist);
	}

}