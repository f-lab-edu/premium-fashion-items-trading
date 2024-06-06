package com.inturn.pfit.domain.address.service;

import com.inturn.pfit.domain.address.entity.AddressEntity;
import com.inturn.pfit.domain.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressCommandService {

	private final AddressRepository addressRepository;

	@Transactional
	public AddressEntity save(AddressEntity entity) {
		return addressRepository.save(entity);
	}
}
