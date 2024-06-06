package com.inturn.pfit.domain.address.facade;

import com.inturn.pfit.domain.address.dto.request.ModifyAddressRequestDTO;
import com.inturn.pfit.domain.address.dto.response.AddressResponseDTO;
import com.inturn.pfit.domain.address.entity.AddressEntity;
import com.inturn.pfit.domain.address.exception.AddressNotFoundException;
import com.inturn.pfit.domain.address.service.AddressCommandService;
import com.inturn.pfit.domain.address.service.AddressQueryService;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.UserNotFoundException;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.global.support.utils.PfitConsts;
import com.inturn.pfit.global.support.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModifyAddressFacade {

	private final AddressCommandService addressCommandService;

	private final AddressQueryService addressQueryService;
	private final UserQueryService userQueryService;

	@Transactional
	public AddressResponseDTO modifyAddress(ModifyAddressRequestDTO req) {

		final UserEntity user = userQueryService.getUserById(SessionUtils.getUserId()).orElseThrow(() -> new UserNotFoundException());

		AddressEntity address = addressQueryService.getAddressById(req.addressId()).orElseThrow(() -> new AddressNotFoundException());

		if(PfitConsts.CommonCodeConsts.YN_Y.equals(req.defaultYn())){
			user.getAddressList().forEach(AddressEntity::setDefaultN);
		}

		return AddressResponseDTO.from(addressCommandService.save(req.convertAddress(address)));
	}
}
