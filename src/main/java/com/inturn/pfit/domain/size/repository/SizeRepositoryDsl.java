package com.inturn.pfit.domain.size.repository;


import com.inturn.pfit.domain.size.dto.request.SizePagingRequestDTO;
import com.inturn.pfit.domain.size.dto.response.SizeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SizeRepositoryDsl {

	Page<SizeResponseDTO> getPagingList(SizePagingRequestDTO req, Pageable pageable);
}
