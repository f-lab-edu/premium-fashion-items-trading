package com.inturn.pfit.domain.sizetype.service;


import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.domain.sizetype.repository.SizeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeTypeQueryService {

	private final SizeTypeRepository sizeTypeRepository;

	@Transactional(readOnly = true)
	public List<SizeTypeEntity> getSizeTypeListBySizeId(Integer sizeId) {
		return sizeTypeRepository.findAllBySizeId(sizeId);
	}

}
