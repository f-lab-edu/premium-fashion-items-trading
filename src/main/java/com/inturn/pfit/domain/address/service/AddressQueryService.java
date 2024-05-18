package com.inturn.pfit.domain.address.service;

import com.inturn.pfit.domain.address.dto.response.AddressResponseDTO;
import com.inturn.pfit.domain.address.exception.AddressNotFoundException;
import com.inturn.pfit.domain.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressQueryService {

	private final AddressRepository addressRepository;

	@Transactional(readOnly = true)
	public AddressResponseDTO getAddressById(Long addressId) {
		return AddressResponseDTO.from(addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException()));
	}

	@Transactional(readOnly = true)
	public List<AddressResponseDTO> getAddressListByUserId(Long userId) {
		return addressRepository.findAllByUserId(userId).stream().map(AddressResponseDTO::from).collect(Collectors.toList());
	}
}
