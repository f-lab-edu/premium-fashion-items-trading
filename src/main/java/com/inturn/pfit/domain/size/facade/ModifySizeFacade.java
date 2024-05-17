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
import com.inturn.pfit.domain.sizetype.exception.SizeTypeNotFoundException;
import com.inturn.pfit.domain.sizetype.service.SizeTypeCommandService;
import com.inturn.pfit.domain.sizetype.service.SizeTypeQueryService;
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
public class ModifySizeFacade {

	private final SizeCommandService sizeCommandService;

	private final SizeQueryService sizeQueryService;

	private final SizeTypeCommandService sizeTypeCommandService;

	private final SizeTypeQueryService sizeTypeQueryService;

	private final CategoryQueryService categoryQueryService;

	@Transactional
	public SizeResponseDTO modifySize(ModifySizeRequestDTO req) {

		//해당 size가 존재하는지 확인
		SizeEntity size = sizeQueryService.getSizeById(req.sizeId());

		//validate 처리
		validateModifySize(req, size);

		//size 편집
		SizeEntity modSize = sizeCommandService.save(req.convertSize(size));

		//sizeType에 대한 CUD 처리
		sizeTypeCommandService.saveCUDAll(size, req.sizeTypeList());

		//sizeType 목록 조회 - 1차 캐시 영역에 존재하는 데이터가 아니기에 조회하여 set
		List<SizeTypeEntity> sizeTypeList = sizeTypeQueryService.getSizeTypeListBySizeId(size.getSizeId());
		
		return SizeResponseDTO.from(modSize, sizeTypeList);
	}

	private void validateModifySize(ModifySizeRequestDTO req, SizeEntity size) {

		//TODO - 이미 해당 카테고리로 상품이 등록된 경우는 validate 추가 -> 상품 개발 후 해당 내용 개발 진행
		categoryQueryService.validateExistCategoryByOrder(req.categoryId());

		List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList = req.sizeTypeList();

		//수정 / 삭제할 sizeType이 기 등록된 데이터가 아닐 경우 validate
		validateExistSizeTypeForRequestUpdateAndDelete(size, modifySizeTypeRequestDTOList);

		validateDuplicateSizeType(size, modifySizeTypeRequestDTOList);
	}

	private void validateDuplicateSizeType(SizeEntity size, List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList) {
		HashSet<Integer> sizeTypeOrderMap = new HashSet<>();
		//기 등록된 sizeTypeOrder와 수정 요청온 sizeTypeOrder 가 중복되는지 확인.
		for(SizeTypeEntity sizeType : size.getSizeTypeList()){
			Integer sizeTypeOrder = findSizeTypeOrder(modifySizeTypeRequestDTOList, sizeType);
			if(ObjectUtils.isNotEmpty(sizeTypeOrder) && !sizeTypeOrderMap.add(sizeTypeOrder)) {
				throw new DuplicateSizeTypeOrderException();
			}
		}

		//CREATE는 별도 비교
		if(!modifySizeTypeRequestDTOList.stream().filter(o -> CUDRequestCommand.CREATE.equals(o.cudRequestCommand())).map(ModifySizeTypeRequestDTO::sizeTypeOrder)
				.allMatch(sizeTypeOrderMap::add)) {
			throw new DuplicateSizeTypeOrderException();
		}
	}

	private void validateExistSizeTypeForRequestUpdateAndDelete(SizeEntity size, List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList) {
		boolean isExistSizeType = modifySizeTypeRequestDTOList.stream().filter(o -> ObjectUtils.isNotEmpty(o.sizeTypeId())).
				allMatch(reqSizeType -> size.getSizeTypeList().stream().anyMatch(sizeType -> sizeType.getSizeTypeId().equals(reqSizeType.sizeTypeId())));
		//모두 일치한다면(true) 통과, 아니라면 Exception 발생
		if(!isExistSizeType) {
			throw new SizeTypeNotFoundException();
		}
	}

	private Integer findSizeTypeOrder(List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList, SizeTypeEntity sizeType) {
		Optional<ModifySizeTypeRequestDTO> modifySizeTypeOpt = modifySizeTypeRequestDTOList.stream().filter(o -> sizeType.getSizeTypeId().equals(o.sizeTypeId())).findFirst();
		return modifySizeTypeOpt.isPresent() ? CUDRequestCommand.UPDATE.equals(modifySizeTypeOpt.get().cudRequestCommand()) ? modifySizeTypeOpt.get().sizeTypeOrder() : null
				: sizeType.getSizeTypeOrder();
	}
}
