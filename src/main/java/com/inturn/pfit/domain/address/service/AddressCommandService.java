package com.inturn.pfit.domain.address.service;

import com.inturn.pfit.domain.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressCommandService {

	private final AddressRepository addressRepository;

}
