package com.inturn.pfit.domain.sizetype.service;


import com.inturn.pfit.domain.sizetype.repository.SizeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SizeTypeQueryService {

	private final SizeTypeRepository sizeTypeRepository;


}
