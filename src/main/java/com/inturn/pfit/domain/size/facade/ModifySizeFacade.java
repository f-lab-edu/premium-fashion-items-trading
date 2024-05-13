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
		SizeEntity modSize = sizeCommandService.save(req.modifySize(size));

		//sizeType에 대한 CUD 처리
		sizeTypeCommandService.saveCUDAll(size, req.sizeTypeList());

		//sizeType 목록 조회 - 1차 캐시 영역에 존재하는 데이터가 아니기에 조회하여 set
		List<SizeTypeEntity> sizeTypeList = sizeTypeQueryService.getSizeTypeListBySizeId(size.getSizeId());
		
		return SizeResponseDTO.from(modSize, sizeTypeList);
	}

	private void validateModifySize(ModifySizeRequestDTO req, SizeEntity size) {

		//TODO - 이미 해당 카테고리로 상품이 등록된 경우는 validate 추가 -> 상품 개발 후 해당 내용 개발 진행
		
		//카테고리가 존재하는지 여부 확인
		categoryQueryService.getCategoryById(req.categoryId());

		List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList = req.sizeTypeList();

		//수정 / 삭제할 sizeType이 기 등록된 데이터가 아닐 경우 validate
		boolean isExistSizeType = modifySizeTypeRequestDTOList.stream().filter(o -> ObjectUtils.isNotEmpty(o.sizeTypeId())).
				allMatch(reqSizeType -> size.getSizeTypeList().stream().anyMatch(sizeType -> sizeType.getSizeTypeId().equals(reqSizeType.sizeTypeId())));
		//모두 일치한다면(true) 통과, 아니라면 Exception 발생
		if(!isExistSizeType) {
			throw new NotFoundSizeTypeException();
		}

		HashSet<Integer> sizeTypeOrderMap = new HashSet<>();
		//기 등록된 sizeTypeOrder와 수정 요청온 sizeTypeOrder 가 중복되는지 확인.
		for(SizeTypeEntity sizeType : size.getSizeTypeList()){
			//기존에 등록되었던 데이터 인지(UPDATE / DELETE) 확인
			Optional<ModifySizeTypeRequestDTO> modifySizeTypeOpt = modifySizeTypeRequestDTOList.stream().filter(o -> sizeType.getSizeTypeId().equals(o.sizeTypeId())).findFirst();

			//존재하는데 UPDATE이면 요청으로 온 order를 반환 / DELETE 이면 null로 반환
			//존재하지 않을 경우 기존 데이터의 order를 반환.
			Integer sizeTypeOrder = modifySizeTypeOpt.isPresent() ? CUDMode.UPDATE.equals(modifySizeTypeOpt.get().cudMode()) ? modifySizeTypeOpt.get().sizeTypeOrder() : null
					: sizeType.getSizeTypeOrder();
			//중복일 경우 false 반환
			if(!sizeTypeOrderMap.add(sizeTypeOrder)) {
				throw new DuplicateSizeTypeOrderException();
			}
		}
		//CREATE는 새로운 요청이기에 별도로 비교
		if(!modifySizeTypeRequestDTOList.stream().filter(o -> CUDMode.CREATE.equals(o.cudMode())).map(ModifySizeTypeRequestDTO::sizeTypeOrder)
				.allMatch(o -> sizeTypeOrderMap.add(o))) {
			throw new DuplicateSizeTypeOrderException();
		}

	}
}
