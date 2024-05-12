package com.inturn.pfit.domain.size.facade;

import com.inturn.pfit.domain.category.service.CategoryQueryService;
import com.inturn.pfit.domain.size.dto.request.CreateSizeRequestDTO;
import com.inturn.pfit.domain.size.dto.response.CreateSizeResponseDTO;
import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.size.service.SizeCommandService;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.domain.sizetype.service.SizeTypeCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateSizeFacade {

	private final SizeCommandService sizeCommandService;

	private final SizeTypeCommandService sizeTypeCommandService;

	private final CategoryQueryService categoryQueryService;

	@Transactional
	public CreateSizeResponseDTO createSize(CreateSizeRequestDTO req) {

		//카테고리가 존재하는지 여부 확인
		categoryQueryService.getCategoryById(req.createSize().getCategoryId());

		//size 저장
		SizeEntity size = sizeCommandService.save(req.createSize());

		//저장된 id를 반환받아 저장할 sizeType 목록을 반환
		List<SizeTypeEntity> sizeTypeList = req.getCsreateSizeTypeList(size.getSizeId());

		//sizeType을 저장
		sizeTypeCommandService.saveAll(sizeTypeList);

		return CreateSizeResponseDTO.from(size);
	}
}
