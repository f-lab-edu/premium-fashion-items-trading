package com.inturn.pfit.domain.address.facade;

import com.inturn.pfit.domain.address.dto.response.AddressResponseDTO;
import com.inturn.pfit.domain.address.service.AddressQueryService;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.UserNotFoundException;
import com.inturn.pfit.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserAddressFacade {

	private final AddressQueryService addressQueryService;

	private final UserQueryService userQueryService;

	@Transactional(readOnly = true)
	public List<AddressResponseDTO> getAddressListByUserId(final Long userId) {

		final UserEntity user = userQueryService.getUserById(userId).orElseThrow(() -> new UserNotFoundException());

		return addressQueryService.getAddressListByUserId(user.getUserId());
	}
}
