package com.inturn.pfit.domain.address.facade;

import com.inturn.pfit.domain.address.dto.request.CreateAddressRequestDTO;
import com.inturn.pfit.domain.address.dto.response.CreateAddressResponseDTO;
import com.inturn.pfit.domain.address.entity.AddressEntity;
import com.inturn.pfit.domain.address.service.AddressCommandService;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.UserNotFoundException;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.global.support.utils.PfitConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateAddressFacade {

	private final AddressCommandService addressCommandService;

	private final UserQueryService userQueryService;

	@Transactional
	public CreateAddressResponseDTO createAddress(CreateAddressRequestDTO req) {

		final UserEntity user = userQueryService.getUserById(req.userId()).orElseThrow(() -> new UserNotFoundException());

		if(PfitConsts.CommonCodeConsts.YN_Y.equals(req.defaultYn())){
			user.getAddressList().forEach(AddressEntity::setDefaultN);
		}

		return CreateAddressResponseDTO.from(addressCommandService.save(req.convertAddress()));
	}
}
