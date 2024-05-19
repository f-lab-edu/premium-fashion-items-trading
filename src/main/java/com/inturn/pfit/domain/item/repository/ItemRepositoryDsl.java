package com.inturn.pfit.domain.item.repository;


import com.inturn.pfit.domain.item.dto.request.ItemPagingRequestDTO;
import com.inturn.pfit.domain.item.dto.response.ItemResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryDsl {

	Page<ItemResponseDTO> getPagingList(ItemPagingRequestDTO req, Pageable pageable);
}
