package com.inturn.pfit.domain.sizetype.service;

import com.inturn.pfit.domain.size.entity.SizeEntity;
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
	public void saveCUDAll(SizeEntity size, List<ModifySizeTypeRequestDTO> list) {
		for(ModifySizeTypeRequestDTO req : list) {
			//위와 같이 req로 for문 처리한 이유는 entity에는 CUD 모드 컬럼이 없기 때문에.
			SizeTypeEntity entity = req.modifySizeType(size.getSizeId(), size.getSizeTypeList());
			switch (req.cudMode()) {
				case CREATE, UPDATE -> sizeTypeRepository.save(entity);
				case DELETE -> sizeTypeRepository.delete(entity);
			}
		}
	}
}
