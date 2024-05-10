package com.inturn.pfit.domain.size.service;

import com.inturn.pfit.domain.size.dto.request.CreateSizeRequestDTO;
import com.inturn.pfit.domain.size.dto.request.DeleteSizeRequestDTO;
import com.inturn.pfit.domain.size.dto.request.ModifySizeRequestDTO;
import com.inturn.pfit.domain.size.dto.response.CreateSizeResponseDTO;
import com.inturn.pfit.domain.size.dto.response.SizeResponseDTO;
import com.inturn.pfit.domain.size.entity.Size;
import com.inturn.pfit.domain.size.repository.SizeRepository;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SizeCommandService {

	private final SizeRepository sizeRepository;

	private final SizeQueryService SizeQueryService;

	//브랜드 등록
	@Transactional
	public CreateSizeResponseDTO createSize(CreateSizeRequestDTO req) {

		Size Size = req.createSize();

		return CreateSizeResponseDTO.from(this.save(Size));
	}

	@Transactional
	public SizeResponseDTO modifySize(ModifySizeRequestDTO req) {

		//해당 브랜드가 존재하는지 조회
		Size Size = SizeQueryService.getSizeById(req.sizeId());

		Size modSize = req.modifySize(Size);

		return SizeResponseDTO.from(this.save(modSize));
	}

	@Transactional
	public CommonResponseDTO deleteSize(DeleteSizeRequestDTO req) {
		//브랜드 ID로 조회하고 삭제 처리
		sizeRepository.delete(SizeQueryService.getSizeById(req.sizeId()));
		return CommonResponseDTO.ok();
	}

	@Transactional
	public Size save(Size entity) {
		return sizeRepository.save(entity);
	}
}
