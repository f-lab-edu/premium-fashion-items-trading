package com.inturn.pfit.domain.size.service;

import com.inturn.pfit.domain.size.dto.response.SizeResponseDTO;
import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.size.exception.NotFoundSizeException;
import com.inturn.pfit.domain.size.repository.SizeRepository;
import com.inturn.pfit.domain.size.vo.SizeErrorCode;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class SizeQueryServiceTest {

	@InjectMocks
	private SizeQueryService sizeQueryService;

	@Mock
	private SizeRepository sizeRepository;

	@Test
	@DisplayName("사이즈 데이터가 존재하는 요청 상황에서 사이즈 조회를 호출하면 성공한다.")
	void getSize_Success() {

		//given
		int sizeId = 1;

		List<SizeTypeEntity> sizeTypeEntityList = List.of(
				SizeTypeEntity.builder().sizeId(sizeId).sizeTypeId(1).sizeTypeName("270").sizeTypeOrder(1).build(),
				SizeTypeEntity.builder().sizeId(sizeId).sizeTypeId(2).sizeTypeName("275").sizeTypeOrder(2).build()
		);

		SizeEntity size = SizeEntity.builder()
				.sizeId(sizeId)
				.sizeName("한국 신발 사이즈")
				.categoryId(1)
				.sizeTypeList(sizeTypeEntityList)
				.build();

		when(sizeRepository.findById(sizeId)).thenReturn(Optional.of(size));

		//when
		SizeResponseDTO res = sizeQueryService.getSize(sizeId);

		//then
		assertEquals(res.getSizeId(), sizeId);
		assertEquals(res.getSizeTypeList().size(), sizeTypeEntityList.size());

		//verify
		verify(sizeRepository, times(1)).findById(sizeId);
	}

	@Test
	@DisplayName("존재하지 않는 사이즈 요청 상황에서 사이즈 조회를 호출하면 실패한다.")
	void getSize_Fail_NotFoundSize() {

		//given
		int sizeId = 1;
		when(sizeRepository.findById(sizeId)).thenReturn(Optional.empty());

		//when
		final NotFoundSizeException result =  assertThrows(NotFoundSizeException.class, () -> sizeQueryService.getSize(sizeId));

		//then
		assertEquals(result.getMessage(), SizeErrorCode.NOT_FOUND_SIZE_EXCEPTION.getErrorMessage());

		//verify
		verify(sizeRepository, times(1)).findById(sizeId);
	}


}