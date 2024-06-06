package com.inturn.pfit.domain.address.service;

import com.inturn.pfit.domain.address.dto.response.AddressResponseDTO;
import com.inturn.pfit.domain.address.entity.AddressEntity;
import com.inturn.pfit.domain.address.exception.AddressNotFoundException;
import com.inturn.pfit.domain.address.exception.UserNotEqualsAddressException;
import com.inturn.pfit.domain.address.repository.AddressRepository;
import com.inturn.pfit.global.support.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressQueryService {

	private final AddressRepository addressRepository;

	@Transactional(readOnly = true)
	public AddressResponseDTO getAddress(Long addressId) {
		AddressEntity address = getAddressById(addressId).orElseThrow(() -> new AddressNotFoundException());
		if(!address.getUserId().equals(SessionUtils.getUserId())) {
			throw new UserNotEqualsAddressException();
		}

		return AddressResponseDTO.from(address);
	}

	@Transactional(readOnly = true)
	public Optional<AddressEntity> getAddressById(Long addressId) {
		return addressRepository.findById(addressId);
	}

	@Transactional(readOnly = true)
	public List<AddressResponseDTO> getAddressListByUserId(Long userId) {
		return addressRepository.findAllByUserId(userId).stream().map(AddressResponseDTO::from).collect(Collectors.toList());
	}
}
