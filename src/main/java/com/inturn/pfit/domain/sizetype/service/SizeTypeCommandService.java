package com.inturn.pfit.domain.sizetype.service;

import com.inturn.pfit.domain.sizetype.dto.request.ModifySizeTypeRequestDTO;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.domain.sizetype.repository.SizeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeTypeCommandService {

	private final SizeTypeRepository sizeTypeRepository;

	@Transactional
	public List<SizeTypeEntity> saveAll(List<SizeTypeEntity> list) {
		return sizeTypeRepository.saveAll(list);
	}

	@Transactional
	public void saveCUDAll(Integer sizeId, List<ModifySizeTypeRequestDTO> list) {
		for(ModifySizeTypeRequestDTO req : list) {
			SizeTypeEntity entity = req.modifySizeType(sizeId);
			switch (req.cudMode()) {
				case CREATE, UPDATE -> sizeTypeRepository.save(entity);
				case DELETE -> sizeTypeRepository.delete(entity);
			}
		}
	}
}
