package com.inturn.pfit.domain.item.service;

import com.inturn.pfit.domain.item.entity.ItemEntity;
import com.inturn.pfit.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemCommandService {

	private final ItemRepository itemRepository;

	@Transactional
	public ItemEntity save(ItemEntity entity) {
		return itemRepository.save(entity);
	}
}
