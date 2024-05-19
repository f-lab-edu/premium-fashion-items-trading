package com.inturn.pfit.domain.item.facade;

import com.inturn.pfit.domain.brand.entity.Brand;
import com.inturn.pfit.domain.brand.exception.BrandNotFoundException;
import com.inturn.pfit.domain.brand.service.BrandQueryService;
import com.inturn.pfit.domain.brand.vo.BrandErrorCode;
import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.domain.category.exception.CategoryNotFoundException;
import com.inturn.pfit.domain.category.service.CategoryQueryService;
import com.inturn.pfit.domain.category.vo.CategoryErrorCode;
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
import com.inturn.pfit.domain.itemsize.vo.ItemSizeErrorCode;
import com.inturn.pfit.global.common.vo.CUDRequestCommand;
import com.inturn.pfit.global.support.utils.PfitConsts;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ModifyItemFacadeTest {

	@InjectMocks
	ModifyItemFacade modifyItemFacade;

	@Mock
	ItemQueryService itemQueryService;

	@Mock
	ItemSizeQueryService itemSizeQueryService;

	@Mock
	ItemCommandService itemCommandService;

	@Mock
	ItemSizeCommandService itemSizeCommandService;

	@Mock
	CategoryQueryService categoryQueryService;

	@Mock
	BrandQueryService brandQueryService;

	static Integer categoryId;

	static Integer brandId;

	static Long itemId;
	static String itemName;


	static Category category;
	static Brand brand;


	@BeforeAll
	static void initTest () {
		Random random = new Random();

		categoryId = random.nextInt();
		brandId = random.nextInt();
		itemId = random.nextLong();
		itemName = RandomStringUtils.random(10);

		category = Category.builder()
				.categoryId(categoryId)
				.categoryName("신발")
				.categoryOrder(1)
				.build();

		brand = Brand.builder()
				.brandId(brandId)
				.brandName("NIKE")
				.build();
	}

	@Test
	@DisplayName("정상적인 요청 상황에서 상품 편집을 실행하면 성공한다.")
	void modifyItem_Success() {

		//given
		ModifyItemRequestDTO modifyItemRequestDTO = getModifyItemRequestDTO(itemId);

		ItemEntity item = getItemEntity();

		//기존 저장된 상품 사이즈에 편집된 데이터를 merge
		List<ItemSizeEntity> modifyItemSizeList = item.getItemSizeList().stream().collect(Collectors.toList());

		for(ModifyItemSizeRequestDTO itemSizeReq : modifyItemRequestDTO.itemSizeList()) {
			ItemSizeEntity modItemSizeEntity = itemSizeReq.convertItemSize(item.getItemId(), item.getItemSizeList());
			switch (itemSizeReq.cudRequestCommand()) {
				case CREATE -> modifyItemSizeList.add(modItemSizeEntity);
				case UPDATE -> {
					modifyItemSizeList.removeIf(o -> modItemSizeEntity.getItemSizeId().equals(o.getItemSizeId()));
					modifyItemSizeList.add(modItemSizeEntity);
				}
				case DELETE -> modifyItemSizeList.removeIf(o -> modItemSizeEntity.getItemSizeId().equals(o.getItemSizeId()));
			}
		}

		ItemEntity modItem = modifyItemRequestDTO.convertItem(item);

		when(categoryQueryService.getCategoryById(categoryId)).thenReturn(Optional.of(category));
		when(brandQueryService.getBrandById(brandId)).thenReturn(Optional.of(brand));

		when(itemQueryService.getItemById(itemId)).thenReturn(item);
		when(itemCommandService.save(any())).thenReturn(modItem);
		when(itemSizeQueryService.getItemSizeListBySizeId(itemId)).thenReturn(modifyItemSizeList);

		//when
		ItemResponseDTO res = modifyItemFacade.modifyItem(modifyItemRequestDTO);

		//then
		assertEquals(res.getItemId(), itemId);
		assertEquals(res.getItemName(), itemName.repeat(2));
		assertEquals(res.getItemSizeList().size(), modifyItemSizeList.size());

		//verify
		verify(categoryQueryService, times(1)).getCategoryById(categoryId);
		verify(brandQueryService, times(1)).getBrandById(brandId);
		verify(itemQueryService, times(1)).getItemById(itemId);
		verify(itemCommandService, times(1)).save(any());
		verify(itemSizeCommandService, times(1)).saveCUDAll(any(), anyList());
		verify(itemSizeQueryService, times(1)).getItemSizeListBySizeId(itemId);
	}

	@Test
	@DisplayName("존재하지 않는 카테고리 ID로 상품 편집을 실행하면 실패한다.")
	void modifyItem_Fail_CategoryNotFound() {

		//given
		ModifyItemRequestDTO modifyItemRequestDTO = getModifyItemRequestDTO(itemId);

		ItemEntity item = getItemEntity();

		when(categoryQueryService.getCategoryById(categoryId)).thenReturn(Optional.empty());
		when(itemQueryService.getItemById(itemId)).thenReturn(item);

		//when
		final CategoryNotFoundException result = assertThrows(CategoryNotFoundException.class, () -> modifyItemFacade.modifyItem(modifyItemRequestDTO));

		//then
		assertEquals(result.getMessage(), CategoryErrorCode.CATEGORY_NOT_FOUND_EXCEPTION.getErrorMessage());

		//verify
		verify(itemQueryService, times(1)).getItemById(itemId);
		verify(categoryQueryService, times(1)).getCategoryById(categoryId);
		verify(brandQueryService, times(0)).getBrandById(brandId);
		verify(itemSizeQueryService, times(0)).getItemSizeListBySizeId(itemId);
		verify(itemCommandService, times(0)).save(any());
		verify(itemSizeCommandService, times(0)).saveCUDAll(any(), anyList());
	}

	@Test
	@DisplayName("존재하지 않는 브랜드 ID로 상품 편집을 실행하면 실패한다.")
	void modifyItem_Fail_BrandNotFound() {

		//given
		ModifyItemRequestDTO modifyItemRequestDTO = getModifyItemRequestDTO(itemId);

		ItemEntity item = getItemEntity();

		when(categoryQueryService.getCategoryById(categoryId)).thenReturn(Optional.of(category));
		when(brandQueryService.getBrandById(brandId)).thenReturn(Optional.empty());
		when(itemQueryService.getItemById(itemId)).thenReturn(item);

		//when
		final BrandNotFoundException result = assertThrows(BrandNotFoundException.class, () -> modifyItemFacade.modifyItem(modifyItemRequestDTO));

		//then
		assertEquals(result.getMessage(), BrandErrorCode.BRAND_NOT_FOUND_EXCEPTION.getErrorMessage());

		//verify
		verify(itemQueryService, times(1)).getItemById(itemId);
		verify(categoryQueryService, times(1)).getCategoryById(categoryId);
		verify(brandQueryService, times(1)).getBrandById(brandId);
		verify(itemSizeQueryService, times(0)).getItemSizeListBySizeId(itemId);
		verify(itemCommandService, times(0)).save(any());
		verify(itemSizeCommandService, times(0)).saveCUDAll(any(), anyList());
	}

	@Test
	@DisplayName("존재하지 않는 상품 사이즈 ID로 상품 편집을 실행하면 실패한다.")
	void modifyItem_Fail_NotFoundItemSize() {

		//given
		long notFoundItemSizeId = 0l;
		List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList = List.of(
				ModifyItemSizeRequestDTO.builder().itemSizeName("270").itemSizeId(notFoundItemSizeId).itemSizeOrder(1).cudRequestCommand(CUDRequestCommand.DELETE).build()
		);

		ModifyItemRequestDTO modifyItemRequestDTO = getModifyItemRequestDTO(itemId, modifyItemSizeRequestDTOList);

		ItemEntity item = getItemEntity();

		when(categoryQueryService.getCategoryById(categoryId)).thenReturn(Optional.of(category));
		when(brandQueryService.getBrandById(brandId)).thenReturn(Optional.of(brand));
		when(itemQueryService.getItemById(itemId)).thenReturn(item);

		//when
		final ItemSizeNotFoundException result = assertThrows(ItemSizeNotFoundException.class, () -> modifyItemFacade.modifyItem(modifyItemRequestDTO));

		//then
		assertEquals(result.getMessage(), ItemSizeErrorCode.ITEM_SIZE_NOT_FOUND_EXCEPTION.getErrorMessage());

		//verify
		verify(categoryQueryService, times(1)).getCategoryById(categoryId);
		verify(brandQueryService, times(1)).getBrandById(brandId);
		verify(itemQueryService, times(1)).getItemById(itemId);
		verify(itemSizeQueryService, times(0)).getItemSizeListBySizeId(itemId);
		verify(itemCommandService, times(0)).save(any());
		verify(itemSizeCommandService, times(0)).saveCUDAll(any(), anyList());
	}

	@Test
	@DisplayName("상품 사이즈의 순번이 중복되는 상황에서 상품 편집을 실행하면 실패한다.")
	void modifyItem_Fail_DuplicateItemSizeOrder() {

		//given
		List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList = List.of(
				ModifyItemSizeRequestDTO.builder().itemSizeName("270").itemSizeId(1l).itemSizeOrder(1).cudRequestCommand(CUDRequestCommand.DELETE).build(),
				ModifyItemSizeRequestDTO.builder().itemSizeName("290").itemSizeOrder(2).cudRequestCommand(CUDRequestCommand.CREATE).build()
		);

		ModifyItemRequestDTO modifyItemRequestDTO = getModifyItemRequestDTO(itemId, modifyItemSizeRequestDTOList);

		ItemEntity item = getItemEntity();

		when(categoryQueryService.getCategoryById(categoryId)).thenReturn(Optional.of(category));
		when(brandQueryService.getBrandById(brandId)).thenReturn(Optional.of(brand));
		when(itemQueryService.getItemById(itemId)).thenReturn(item);

		//when & then
		final DuplicateItemSizeOrderException result = assertThrows(DuplicateItemSizeOrderException.class, () -> modifyItemFacade.modifyItem(modifyItemRequestDTO));

		//then
		assertEquals(result.getMessage(), ItemSizeErrorCode.DUPLICATE_ITEM_SIZE_EXCEPTION.getErrorMessage());

		//verify
		verify(itemQueryService, times(1)).getItemById(itemId);
		verify(itemCommandService, times(0)).save(any());
		verify(itemSizeCommandService, times(0)).saveCUDAll(any(), anyList());
		verify(itemSizeQueryService, times(0)).getItemSizeListBySizeId(itemId);
	}

	private ItemEntity getItemEntity() {
		List<ItemSizeEntity> itemSizeEntityList = List.of(
				ItemSizeEntity.builder().itemId(itemId).itemSizeId(1l).itemSizeName("270").itemSizeOrder(1).build(),
				ItemSizeEntity.builder().itemId(itemId).itemSizeId(2l).itemSizeName("275").itemSizeOrder(2).build()
		);

		return ItemEntity.builder()
				.itemId(itemId)
				.itemName(itemName)
				.modelNo("CW2288-111")
				.gender("MEN")
				.categoryId(categoryId)
				.brandId(brandId)
				.retailPrice(139000l)
				.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
				.releaseDate(LocalDate.now())
				.itemSizeList(itemSizeEntityList)
				.build();
	}

	private ModifyItemRequestDTO getModifyItemRequestDTO(Long itemId) {

		List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList = List.of(
				ModifyItemSizeRequestDTO.builder().itemSizeName("270").itemSizeId(1l).itemSizeOrder(1).cudRequestCommand(CUDRequestCommand.DELETE).build(),
				ModifyItemSizeRequestDTO.builder().itemSizeName("240").itemSizeOrder(1).cudRequestCommand(CUDRequestCommand.CREATE).build(),
				ModifyItemSizeRequestDTO.builder().itemSizeName("245").itemSizeId(2l).itemSizeOrder(4).cudRequestCommand(CUDRequestCommand.UPDATE).build(),
				ModifyItemSizeRequestDTO.builder().itemSizeName("250").itemSizeOrder(2).cudRequestCommand(CUDRequestCommand.CREATE).build()
		);

		return ModifyItemRequestDTO.builder()
				.itemId(itemId)
				.itemName(itemName.repeat(2))
				.modelNo("CW2288-111")
				.gender("MEN")
				.categoryId(categoryId)
				.brandId(brandId)
				.retailPrice(139000l)
				.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
				.releaseDate(LocalDate.now())
				.itemSizeList(modifyItemSizeRequestDTOList)
				.build();
	}

	private ModifyItemRequestDTO getModifyItemRequestDTO(Long itemId, List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList) {

		return ModifyItemRequestDTO.builder()
				.itemId(itemId)
				.itemName(itemName.repeat(2))
				.modelNo("CW2288-111")
				.gender("MEN")
				.categoryId(categoryId)
				.brandId(brandId)
				.retailPrice(139000l)
				.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
				.releaseDate(LocalDate.now())
				.itemSizeList(modifyItemSizeRequestDTOList)
				.build();
	}
}