package com.inturn.pfit.domain.itemsize.service;

import com.inturn.pfit.domain.item.entity.ItemEntity;
import com.inturn.pfit.domain.itemsize.dto.request.ModifyItemSizeRequestDTO;
import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import com.inturn.pfit.domain.itemsize.repository.ItemSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemSizeCommandService {

	private final ItemSizeRepository itemSizeRepository;

	@Transactional
	public List<ItemSizeEntity> saveAll(List<ItemSizeEntity> list) {
		return itemSizeRepository.saveAll(list);
	}

	@Transactional
	public void saveCUDAll(ItemEntity item, List<ModifyItemSizeRequestDTO> list) {
		for(ModifyItemSizeRequestDTO req : list) {
			//위와 같이 req로 for문 처리한 이유는 entity에는 CUD 모드 컬럼이 없기 때문에.
			ItemSizeEntity entity = req.convertItemSize(item.getItemId(), item.getItemSizeList());
			switch (req.cudRequestCommand()) {
				case CREATE, UPDATE -> itemSizeRepository.save(entity);
				case DELETE -> itemSizeRepository.delete(entity);
			}
		}
	}
}
