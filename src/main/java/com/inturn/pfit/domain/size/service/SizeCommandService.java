package com.inturn.pfit.domain.size.service;

import com.inturn.pfit.domain.size.dto.request.DeleteSizeRequestDTO;
import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.size.repository.SizeRepository;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SizeCommandService {

	@Getter
	private final SizeRepository sizeRepository;

	private final SizeQueryService SizeQueryService;

	@Transactional
	public CommonResponseDTO deleteSize(DeleteSizeRequestDTO req) {
		//브랜드 ID로 조회하고 삭제 처리
		sizeRepository.delete(SizeQueryService.getSizeById(req.sizeId()));
		return CommonResponseDTO.ok();
	}

	@Transactional
	public SizeEntity save(SizeEntity entity) {
		return sizeRepository.save(entity);
	}
}
