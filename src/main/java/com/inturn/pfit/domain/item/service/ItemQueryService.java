package com.inturn.pfit.domain.item.service;

import com.inturn.pfit.domain.item.dto.request.ItemPagingRequestDTO;
import com.inturn.pfit.domain.item.dto.response.ItemResponseDTO;
import com.inturn.pfit.domain.item.entity.ItemEntity;
import com.inturn.pfit.domain.item.exception.ItemNotFoundException;
import com.inturn.pfit.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemQueryService {

	private final ItemRepository itemRepository;

	@Transactional(readOnly = true)
	public Page<ItemResponseDTO> getItemPagingList(ItemPagingRequestDTO req, Pageable page) {
		return itemRepository.getPagingList(req, page);
	}

	@Transactional(readOnly = true)
	public ItemResponseDTO getItem(Long itemId) {
		return ItemResponseDTO.from(this.getItemById(itemId));
	}

	@Transactional(readOnly = true)
	public ItemEntity getItemById(Long itemId) {
		return itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException());
	}
}
