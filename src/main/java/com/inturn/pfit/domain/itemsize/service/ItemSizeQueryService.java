package com.inturn.pfit.domain.itemsize.service;

import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import com.inturn.pfit.domain.itemsize.repository.ItemSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemSizeQueryService {

	private final ItemSizeRepository itemSizeRepository;

	@Transactional(readOnly = true)
	public List<ItemSizeEntity> getItemSizeListBySizeId(Long itemId) {
		return itemSizeRepository.findAllByItemId(itemId);
	}
}
