package com.inturn.pfit.domain.size.facade;

import com.inturn.pfit.domain.category.service.CategoryQueryService;
import com.inturn.pfit.domain.size.dto.request.CreateSizeRequestDTO;
import com.inturn.pfit.domain.size.dto.response.CreateSizeResponseDTO;
import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.size.service.SizeCommandService;
import com.inturn.pfit.domain.sizetype.dto.request.CreateSizeTypeRequestDTO;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.domain.sizetype.exception.DuplicateSizeTypeOrderException;
import com.inturn.pfit.domain.sizetype.service.SizeTypeCommandService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CreateSizeFacadeTest {

	@InjectMocks
	CreateSizeFacade createSizeFacade;
	@Mock
	SizeCommandService sizeCommandService;
	@Mock
	CategoryQueryService categoryQueryService;
	@Mock
	SizeTypeCommandService sizeTypeCommandService;

	static Integer categoryId;
	static String sizeName;
	static Integer sizeId;
	static List<CreateSizeTypeRequestDTO> createSizeTypeList;

	static CreateSizeRequestDTO createSizeRequestDTO;

	@BeforeAll
	static void initTest() {
		categoryId = 1;
		sizeName = "신발 사이즈";
		sizeId = 1;
		createSizeTypeList = List.of(
				CreateSizeTypeRequestDTO.builder().sizeTypeName("275").sizeTypeOrder(1).build(),
				CreateSizeTypeRequestDTO.builder().sizeTypeName("280").sizeTypeOrder(2).build(),
				CreateSizeTypeRequestDTO.builder().sizeTypeName("285").sizeTypeOrder(3).build()
		);
		createSizeRequestDTO = CreateSizeRequestDTO.builder()
				.sizeName(sizeName)
				.categoryId(categoryId)
				.sizeTypeList(createSizeTypeList)
				.build();
	}

	@Test
	@DisplayName("정상적인 요청 상황에서 사이즈 등록을 호출하면 성공한다.")
	void createSize_Success() {

		//given
		List<SizeTypeEntity> sizeTypeEntityList = createSizeTypeList.stream().map(o -> o.createSizeType(sizeId)).collect(Collectors.toList());

		SizeEntity size = SizeEntity.builder()
				.sizeId(sizeId)
				.sizeName(sizeName)
				.categoryId(categoryId)
				.sizeTypeList(sizeTypeEntityList)
				.build();

		when(sizeCommandService.save(any())).thenReturn(size);

		//when
		CreateSizeResponseDTO res = createSizeFacade.createSize(createSizeRequestDTO);

		//then
		assertEquals(res.sizeId(), sizeId);

		//verify
		verify(sizeTypeCommandService, times(1)).saveAll(anyList());
		verify(sizeCommandService, times(1)).save(any());
		verify(categoryQueryService, times(1)).validateExistCategoryById(categoryId);
	}

	@Test
	@DisplayName("사이즈 종류가 중복된 요청 상황에서 사이즈 등록을 호출하면 실패.")
	void createSize_Fail_DuplicateSizeTypeOrderList() {

		//given
		List<CreateSizeTypeRequestDTO> duplicateSizeTypeOrderList = List.of(
				CreateSizeTypeRequestDTO.builder().sizeTypeName("275").sizeTypeOrder(1).build(),
				CreateSizeTypeRequestDTO.builder().sizeTypeName("280").sizeTypeOrder(1).build()
		);

		//when & then
		final DuplicateSizeTypeOrderException result =  assertThrows(DuplicateSizeTypeOrderException.class, () -> createSizeFacade.createSize( CreateSizeRequestDTO.builder()
				.sizeName(sizeName)
				.categoryId(categoryId)
				.sizeTypeList(duplicateSizeTypeOrderList)
				.build()));

		//verify
		verify(sizeTypeCommandService, times(0)).saveAll(anyList());
		verify(sizeCommandService, times(0)).save(any());
		verify(categoryQueryService, times(0)).validateExistCategoryById(categoryId);
	}

}