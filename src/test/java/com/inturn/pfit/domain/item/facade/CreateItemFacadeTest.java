package com.inturn.pfit.domain.item.facade;

import com.inturn.pfit.domain.brand.entity.Brand;
import com.inturn.pfit.domain.brand.exception.BrandNotFoundException;
import com.inturn.pfit.domain.brand.service.BrandQueryService;
import com.inturn.pfit.domain.brand.vo.BrandErrorCode;
import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.domain.category.exception.CategoryNotFoundException;
import com.inturn.pfit.domain.category.service.CategoryQueryService;
import com.inturn.pfit.domain.category.vo.CategoryErrorCode;
import com.inturn.pfit.domain.item.dto.request.CreateItemRequestDTO;
import com.inturn.pfit.domain.item.dto.response.CreateItemResponseDTO;
import com.inturn.pfit.domain.item.entity.ItemEntity;
import com.inturn.pfit.domain.item.service.ItemCommandService;
import com.inturn.pfit.domain.itemsize.dto.request.CreateItemSizeRequestDTO;
import com.inturn.pfit.domain.itemsize.exception.DuplicateItemSizeOrderException;
import com.inturn.pfit.domain.itemsize.service.ItemSizeCommandService;
import com.inturn.pfit.domain.itemsize.vo.ItemSizeErrorCode;
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
class CreateItemFacadeTest {

	@InjectMocks
	CreateItemFacade createItemFacade;
	@Mock
	ItemCommandService itemCommandService;
	@Mock
	CategoryQueryService categoryQueryService;
	@Mock
	BrandQueryService brandQueryService;
	@Mock
	ItemSizeCommandService itemSizeCommandService;

	static Integer categoryId;
	static Integer brandId;
	static String itemName;
	static Long itemId;

	static Category category;
	static Brand brand;

	@BeforeAll
	static void initTest() {
		Random random = new Random();
		categoryId = random.nextInt();
		brandId = random.nextInt();
		itemId = random.nextLong();
		itemName = RandomStringUtils.randomAlphabetic(10);

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
	@DisplayName("정상적인 요청 상황에서 상품 등록을 호출하면 성공한다.")
	void createItem_Success(){
		//given
		CreateItemRequestDTO createItemRequestDTO = getCreateItemRequestDTO();

		ItemEntity item = ItemEntity.builder()
				.itemId(itemId)
				.itemName(itemName)
				.modelNo("CW2288-111")
				.gender("MEN")
				.categoryId(categoryId)
				.brandId(brandId)
				.retailPrice(139000l)
				.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
				.releaseDate(LocalDate.now())
				.itemSizeList(createItemRequestDTO.itemSizeList().stream().map(o -> o.convertItemSize(itemId)).collect(Collectors.toList()))
				.build();

		when(categoryQueryService.getCategoryById(categoryId)).thenReturn(Optional.of(category));
		when(brandQueryService.getBrandById(brandId)).thenReturn(Optional.of(brand));

		when(itemCommandService.save(any())).thenReturn(item);

		//when
		CreateItemResponseDTO res = createItemFacade.createItem(createItemRequestDTO);

		//then
		assertEquals(res.itemId(), itemId);

		//verify
		verify(itemSizeCommandService, times(1)).saveAll(anyList());
		verify(itemCommandService, times(1)).save(any());
		verify(brandQueryService, times(1)).getBrandById(brandId);
		verify(categoryQueryService, times(1)).getCategoryById(categoryId);
	}

	@Test
	@DisplayName("상품 사이즈가 중복된 요청 상황에서 상품 등록을 호출하면 실패한다.")
	void createSize_Fail_DuplicateItemSizeOrderList() {

		//given
		List<CreateItemSizeRequestDTO> duplicateSizeTypeOrderList = List.of(
				CreateItemSizeRequestDTO.builder().itemSizeName("275").itemSizeOrder(1).build(),
				CreateItemSizeRequestDTO.builder().itemSizeName("280").itemSizeOrder(1).build()
		);

		CreateItemRequestDTO createItemRequestDTO = getCreateItemRequestDTO(duplicateSizeTypeOrderList);

		//when
		final DuplicateItemSizeOrderException result =  assertThrows(DuplicateItemSizeOrderException.class, () -> createItemFacade.createItem(createItemRequestDTO));

		//then
		assertEquals(result.getMessage(), ItemSizeErrorCode.DUPLICATE_ITEM_SIZE_EXCEPTION.getErrorMessage());

		//verify
		verify(itemSizeCommandService, times(0)).saveAll(anyList());
		verify(itemCommandService, times(0)).save(any());
		verify(categoryQueryService, times(0)).getCategoryById(categoryId);
		verify(brandQueryService, times(0)).getBrandById(brandId);
	}

	@Test
	@DisplayName("존재하지 않는 카테고리 ID로 상품 등록을 호출하면 실패한다.")
	void createSize_Fail_CategoryNotFound() {

		//given
		CreateItemRequestDTO createItemRequestDTO = getCreateItemRequestDTO();

		when(categoryQueryService.getCategoryById(categoryId)).thenReturn(Optional.empty());

		//when
		final CategoryNotFoundException result =  assertThrows(CategoryNotFoundException.class, () -> createItemFacade.createItem(createItemRequestDTO));

		//then
		assertEquals(result.getMessage(), CategoryErrorCode.CATEGORY_NOT_FOUND_EXCEPTION.getErrorMessage());

		//verify
		verify(categoryQueryService, times(1)).getCategoryById(categoryId);
		verify(brandQueryService, times(0)).getBrandById(brandId);
		verify(itemSizeCommandService, times(0)).saveAll(anyList());
		verify(itemCommandService, times(0)).save(any());
	}

	@Test
	@DisplayName("존재하지 않는 브랜드 ID로 상품 등록을 호출하면 실패한다.")
	void createSize_Fail_BrandNotFound() {

		//given
		CreateItemRequestDTO createItemRequestDTO = getCreateItemRequestDTO();

		when(categoryQueryService.getCategoryById(categoryId)).thenReturn(Optional.of(category));
		when(brandQueryService.getBrandById(brandId)).thenReturn(Optional.empty());

		//when
		final BrandNotFoundException result =  assertThrows(BrandNotFoundException.class, () -> createItemFacade.createItem(createItemRequestDTO));

		//then
		assertEquals(result.getMessage(), BrandErrorCode.BRAND_NOT_FOUND_EXCEPTION.getErrorMessage());

		//verify
		verify(categoryQueryService, times(1)).getCategoryById(categoryId);
		verify(brandQueryService, times(1)).getBrandById(brandId);
		verify(itemSizeCommandService, times(0)).saveAll(anyList());
		verify(itemCommandService, times(0)).save(any());
	}

	private CreateItemRequestDTO getCreateItemRequestDTO(List<CreateItemSizeRequestDTO> itemSizeList) {

		return CreateItemRequestDTO.builder()
				.itemName(itemName)
				.modelNo("CW2288-111")
				.gender("MEN")
				.categoryId(categoryId)
				.brandId(brandId)
				.retailPrice(139000l)
				.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
				.releaseDate(LocalDate.now())
				.itemSizeList(itemSizeList)
				.build();
	}

	private CreateItemRequestDTO getCreateItemRequestDTO() {

		List<CreateItemSizeRequestDTO> createItemSizeList = List.of(
				CreateItemSizeRequestDTO.builder().itemSizeName("275").itemSizeOrder(1).build(),
				CreateItemSizeRequestDTO.builder().itemSizeName("280").itemSizeOrder(2).build(),
				CreateItemSizeRequestDTO.builder().itemSizeName("285").itemSizeOrder(3).build()
		);

		return CreateItemRequestDTO.builder()
				.itemName(itemName)
				.modelNo("CW2288-111")
				.gender("MEN")
				.categoryId(categoryId)
				.brandId(brandId)
				.retailPrice(139000l)
				.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
				.releaseDate(LocalDate.now())
				.itemSizeList(createItemSizeList)
				.build();
	}

}