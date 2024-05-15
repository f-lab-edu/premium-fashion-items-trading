package com.inturn.pfit.domain.size.service;

import com.inturn.pfit.domain.category.repository.CategoryRepository;
import com.inturn.pfit.domain.size.dto.request.SizePagingRequestDTO;
import com.inturn.pfit.domain.size.dto.response.SizeResponseDTO;
import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.size.exception.NotFoundSizeException;
import com.inturn.pfit.domain.size.repository.SizeRepository;
import com.inturn.pfit.domain.sizetype.repository.SizeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SizeQueryService {
	private final SizeRepository sizeRepository;

	private final CategoryRepository categoryRepository;


	private final SizeTypeRepository sizeTypeRepository;

	@Transactional(readOnly = true)
	public Page<SizeResponseDTO> getSizePagingList(SizePagingRequestDTO req, Pageable page) {
		return sizeRepository.getPagingList(req, page);
	}

	@Transactional(readOnly = true)
	public SizeResponseDTO getSize(Integer sizeId) {
		return SizeResponseDTO.from(getSizeById(sizeId));
	}

	@Transactional(readOnly = true)
	public SizeEntity getSizeById(Integer sizeId) {
		return sizeRepository.findById(sizeId).orElseThrow(() -> new NotFoundSizeException());
	}
}
