package com.inturn.pfit.domain.size.repository;


import com.inturn.pfit.domain.size.dto.request.SizePagingRequestDTO;
import com.inturn.pfit.domain.size.dto.response.SizePagingResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SizeRepositoryDsl {

	Page<SizePagingResponseDTO> getPagingList(SizePagingRequestDTO req, Pageable pageable);
}
