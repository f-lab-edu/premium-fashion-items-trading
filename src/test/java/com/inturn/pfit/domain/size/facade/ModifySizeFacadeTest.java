package com.inturn.pfit.domain.size.facade;

import com.inturn.pfit.domain.category.service.CategoryQueryService;
import com.inturn.pfit.domain.size.dto.request.ModifySizeRequestDTO;
import com.inturn.pfit.domain.size.dto.response.SizeResponseDTO;
import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.size.service.SizeCommandService;
import com.inturn.pfit.domain.size.service.SizeQueryService;
import com.inturn.pfit.domain.sizetype.dto.request.ModifySizeTypeRequestDTO;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.domain.sizetype.exception.DuplicateSizeTypeOrderException;
import com.inturn.pfit.domain.sizetype.exception.NotFoundSizeTypeException;
import com.inturn.pfit.domain.sizetype.service.SizeTypeCommandService;
import com.inturn.pfit.domain.sizetype.service.SizeTypeQueryService;
import com.inturn.pfit.global.common.vo.CUDMode;
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
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ModifySizeFacadeTest {

	@InjectMocks
	ModifySizeFacade modifySizeFacade;

	@Mock
	SizeQueryService sizeQueryService;

	@Mock
	SizeTypeQueryService sizeTypeQueryService;

	@Mock
	SizeCommandService sizeCommandService;

	@Mock
	SizeTypeCommandService sizeTypeCommandService;

	@Mock
	CategoryQueryService categoryQueryService;

	static Integer categoryId = 1;

	static Integer sizeId = 1;

	static String sizeName = "한국 신발 사이즈";

	@Test
	@DisplayName("정상적인 요청 상황에서 사이즈 편집을 실행하면 성공한다.")
	void modifySize_Success() {

		//given
		ModifySizeRequestDTO modifySizeRequestDTO = getModifySizeRequestDTO(sizeId);

		List<SizeTypeEntity> sizeTypeEntityList = List.of(
				SizeTypeEntity.builder().sizeId(sizeId).sizeTypeId(1).sizeTypeName("270").sizeTypeOrder(1).build(),
				SizeTypeEntity.builder().sizeId(sizeId).sizeTypeId(2).sizeTypeName("275").sizeTypeOrder(2).build()
		);

		SizeEntity size = SizeEntity.builder()
				.sizeId(sizeId)
				.sizeName(sizeName)
				.categoryId(categoryId)
				.sizeTypeList(sizeTypeEntityList)
				.build();

		//기존 저장된 사이즈 종류에 편집된 데이터를 merge
		List<SizeTypeEntity> modifySizeTypeList = sizeTypeEntityList.stream().collect(Collectors.toList());

		for(ModifySizeTypeRequestDTO sizeTypeReq : modifySizeRequestDTO.sizeTypeList()) {
			SizeTypeEntity modSizeTypeEntity = sizeTypeReq.modifySizeType(size.getSizeId(), size.getSizeTypeList());
			switch (sizeTypeReq.cudMode()) {
				case CREATE -> modifySizeTypeList.add(modSizeTypeEntity);
				case UPDATE -> {
					modifySizeTypeList.removeIf(o -> modSizeTypeEntity.getSizeTypeId().equals(o.getSizeTypeId()));
					modifySizeTypeList.add(modSizeTypeEntity);
				}
				case DELETE -> modifySizeTypeList.removeIf(o -> modSizeTypeEntity.getSizeTypeId().equals(o.getSizeTypeId()));
			}
		}

		SizeEntity modSize = modifySizeRequestDTO.modifySize(size);

		when(sizeQueryService.getSizeById(sizeId)).thenReturn(size);
		when(sizeCommandService.save(any())).thenReturn(modSize);
		when(sizeTypeQueryService.getSizeTypeListBySizeId(sizeId)).thenReturn(modifySizeTypeList);

		//when
		SizeResponseDTO res = modifySizeFacade.modifySize(modifySizeRequestDTO);

		//then
		assertEquals(res.getSizeId(), sizeId);
		assertEquals(res.getSizeName(), sizeName.repeat(2));
		assertEquals(res.getSizeTypeList().size(), modifySizeTypeList.size());

		//verify
		verify(sizeQueryService, times(1)).getSizeById(sizeId);
		verify(sizeCommandService, times(1)).save(any());
		verify(sizeTypeCommandService, times(1)).saveCUDAll(any(), anyList());
		verify(sizeTypeQueryService, times(1)).getSizeTypeListBySizeId(sizeId);
	}

	@Test
	@DisplayName("사이즈 종류 데이터가 없는 상황에서 사이즈 편집을 실행하면 실패한다.")
	void modifySize_Fail_NotFoundSizeType() {

		//given
		int notFoundSizeTypeId = 0;
		List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList = List.of(
				ModifySizeTypeRequestDTO.builder().sizeTypeName("270").sizeTypeId(notFoundSizeTypeId).sizeTypeOrder(1).cudMode(CUDMode.DELETE).build()
		);

		ModifySizeRequestDTO modifySizeRequestDTO = getModifySizeRequestDTO(sizeId, modifySizeTypeRequestDTOList);

		List<SizeTypeEntity> sizeTypeEntityList = List.of(
				SizeTypeEntity.builder().sizeId(sizeId).sizeTypeId(1).sizeTypeName("270").sizeTypeOrder(1).build(),
				SizeTypeEntity.builder().sizeId(sizeId).sizeTypeId(2).sizeTypeName("275").sizeTypeOrder(2).build()
		);

		SizeEntity size = SizeEntity.builder()
				.sizeId(sizeId)
				.sizeName(sizeName)
				.categoryId(categoryId)
				.sizeTypeList(sizeTypeEntityList)
				.build();

		when(sizeQueryService.getSizeById(sizeId)).thenReturn(size);

		//when & then
		final NotFoundSizeTypeException results = assertThrows(NotFoundSizeTypeException.class, () -> modifySizeFacade.modifySize(modifySizeRequestDTO));

		//verify
		verify(sizeQueryService, times(1)).getSizeById(sizeId);
		verify(sizeCommandService, times(0)).save(any());
		verify(sizeTypeCommandService, times(0)).saveCUDAll(any(), anyList());
		verify(sizeTypeQueryService, times(0)).getSizeTypeListBySizeId(sizeId);
	}

	@Test
	@DisplayName("사이즈 종류의 순번이 중복되는 상황에서 사이즈 편집을 실행하면 실패한다.")
	void modifySize_Fail_DuplicateSizeTypeOrder() {

		//given
		List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList = List.of(
				ModifySizeTypeRequestDTO.builder().sizeTypeName("270").sizeTypeId(1).sizeTypeOrder(1).cudMode(CUDMode.DELETE).build(),
				ModifySizeTypeRequestDTO.builder().sizeTypeName("290").sizeTypeOrder(2).cudMode(CUDMode.CREATE).build()
		);

		ModifySizeRequestDTO modifySizeRequestDTO = getModifySizeRequestDTO(sizeId, modifySizeTypeRequestDTOList);

		List<SizeTypeEntity> sizeTypeEntityList = List.of(
				SizeTypeEntity.builder().sizeId(sizeId).sizeTypeId(1).sizeTypeName("270").sizeTypeOrder(1).build(),
				SizeTypeEntity.builder().sizeId(sizeId).sizeTypeId(2).sizeTypeName("275").sizeTypeOrder(2).build()
		);

		SizeEntity size = SizeEntity.builder()
				.sizeId(sizeId)
				.sizeName(sizeName)
				.categoryId(categoryId)
				.sizeTypeList(sizeTypeEntityList)
				.build();

		when(sizeQueryService.getSizeById(sizeId)).thenReturn(size);

		//when & then
		final DuplicateSizeTypeOrderException results = assertThrows(DuplicateSizeTypeOrderException.class, () -> modifySizeFacade.modifySize(modifySizeRequestDTO));

		//verify
		verify(sizeQueryService, times(1)).getSizeById(sizeId);
		verify(sizeCommandService, times(0)).save(any());
		verify(sizeTypeCommandService, times(0)).saveCUDAll(any(), anyList());
		verify(sizeTypeQueryService, times(0)).getSizeTypeListBySizeId(sizeId);
	}

	private ModifySizeRequestDTO getModifySizeRequestDTO(Integer sizeId) {

		List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList = List.of(
				ModifySizeTypeRequestDTO.builder().sizeTypeName("270").sizeTypeId(1).sizeTypeOrder(1).cudMode(CUDMode.DELETE).build(),
				ModifySizeTypeRequestDTO.builder().sizeTypeName("240").sizeTypeOrder(1).cudMode(CUDMode.CREATE).build(),
				ModifySizeTypeRequestDTO.builder().sizeTypeName("245").sizeTypeId(2).sizeTypeOrder(4).cudMode(CUDMode.UPDATE).build(),
				ModifySizeTypeRequestDTO.builder().sizeTypeName("250").sizeTypeOrder(2).cudMode(CUDMode.CREATE).build()
		);

		return ModifySizeRequestDTO.builder()
				.sizeId(sizeId)
				.sizeName(sizeName.repeat(2))
				.categoryId(categoryId)
				.sizeTypeList(modifySizeTypeRequestDTOList)
				.build();
	}

	private ModifySizeRequestDTO getModifySizeRequestDTO(Integer sizeId, List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList) {

		return ModifySizeRequestDTO.builder()
				.sizeId(sizeId)
				.sizeName(sizeName.repeat(2))
				.categoryId(categoryId)
				.sizeTypeList(modifySizeTypeRequestDTOList)
				.build();
	}

}