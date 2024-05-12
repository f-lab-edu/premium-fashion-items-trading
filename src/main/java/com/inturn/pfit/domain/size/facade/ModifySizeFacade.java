package com.inturn.pfit.domain.size.facade;

import com.inturn.pfit.domain.category.service.CategoryQueryService;
import com.inturn.pfit.domain.size.dto.request.ModifySizeRequestDTO;
import com.inturn.pfit.domain.size.dto.response.SizeResponseDTO;
import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.size.service.SizeCommandService;
import com.inturn.pfit.domain.size.service.SizeQueryService;
import com.inturn.pfit.domain.sizetype.exception.DuplicateSizeTypeOrderException;
import com.inturn.pfit.domain.sizetype.exception.NotFoundSizeTypeException;
import com.inturn.pfit.domain.sizetype.service.SizeTypeCommandService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModifySizeFacade {

	private final SizeCommandService sizeCommandService;

	private final SizeQueryService sizeQueryService;

	private final SizeTypeCommandService sizeTypeCommandService;

	private final CategoryQueryService categoryQueryService;

	@Transactional
	public SizeResponseDTO modifySize(ModifySizeRequestDTO req) {

		//해당 size가 존재하는지 확인
		SizeEntity size = sizeQueryService.getSizeById(req.sizeId());

		//validate 처리
		validateModifySize(req, size);

		//sizeType에 대한 CUD 처리
		sizeTypeCommandService.saveCUDAll(size.getSizeId(), req.sizeTypeList());

		//size 편집
		SizeEntity modSize = sizeCommandService.save(req.modifySize(size));


		return SizeResponseDTO.from(modSize);
	}

	private void validateModifySize(ModifySizeRequestDTO req, SizeEntity size) {
		//카테고리가 존재하는지 여부 확인
		//TODO - NotFoundException 세분화 size, category 어디서 NotFound가 발생했는지 확인 불가.
		//TODO - 이미 해당 카테고리로 제품이 등록된 경우는 validate 추가
		categoryQueryService.getCategoryById(req.categoryId());

		//수정 / 삭제할 sizeType이 기존에 존재하는 데이터 인지 확인하여 validate
		boolean isExistSizeType = req.sizeTypeList().stream().filter(o -> ObjectUtils.isNotEmpty(o.sizeTypeId())).
				allMatch(reqSizeType -> size.getSizeTypeList().stream().anyMatch(sizeType -> sizeType.getSizeTypeId().equals(reqSizeType.sizeTypeId())));
		//모두 일치한다면(true) 통과, 아니라면 Exception 발생
		if(!isExistSizeType) {
			throw new NotFoundSizeTypeException();
		}

		//for문으로 처리
//		for(SizeTypeEntity sizeType : size.getSizeTypeList()){
//			req.sizeTypeList().stream().filter(o -> sizeType.getSizeTypeId().equals(o.sizeTypeId())).findFirst().
//		}

		//삭제를 제외한 sizeTypeOrder가 중복일 경우
		if(req.isDuplicateSizeTypeOrder()) {
			throw new DuplicateSizeTypeOrderException();
		}
	}
}
