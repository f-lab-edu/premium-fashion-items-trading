package com.inturn.pfit.domain.address.service;

import com.inturn.pfit.domain.address.dto.response.AddressResponseDTO;
import com.inturn.pfit.domain.address.entity.AddressEntity;
import com.inturn.pfit.domain.address.exception.AddressNotFoundException;
import com.inturn.pfit.domain.address.exception.UserNotEqualsAddressException;
import com.inturn.pfit.domain.address.repository.AddressRepository;
import com.inturn.pfit.domain.address.vo.AddressErrorCode;
import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.support.utils.PfitConsts;
import com.inturn.pfit.support.fixture.SessionFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AddressQueryServiceTest {

	@InjectMocks
	AddressQueryService addressQueryService;

	@Mock
	AddressRepository addressRepository;

	@Test
	@DisplayName("주소 데이터가 존재하는 주소 ID로 주소 조회를 요청할 경우 성공한다.")
	void getAddress_Success() {

		//given
		long addressId = 1l;
		long userId = 1l;

		AddressEntity address = AddressEntity.builder()
				.addressId(addressId)
				.userId(userId)
				.recipients("SDC")
				.phone("010-1234-5678")
				.postCode("11111")
				.address("서울시 구로구 XXX로 0 길")
				.addressDetail("SK 1260")
				.defaultYn(PfitConsts.CommonCodeConsts.YN_Y)
				.build();

		when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
		SessionFixture.setMockSession(new UserSession(SessionFixture.getUserEntity(userId)));

		//when
		AddressResponseDTO res = addressQueryService.getAddress(addressId);

		//then
		assertEquals(res.getAddressId(), addressId);
		assertEquals(res.getUserId(), userId);

		//verify
		verify(addressRepository, times(1)).findById(addressId);
	}

	@Test
	@DisplayName("주소 데이터의 사용자 ID가 Session 사용자 ID와 다른 경우로 주소 조회를 요청할 경우 실패한다.")
	void getAddress_Fail_UserNotEqualsAddress() {

		//given
		long addressId = 1l;
		long userId = 1l;
		long userNotEqualsId = 2l;

		AddressEntity address = AddressEntity.builder()
				.addressId(addressId)
				.userId(userNotEqualsId)
				.recipients("SDC")
				.phone("010-1234-5678")
				.postCode("11111")
				.address("서울시 구로구 XXX로 0 길")
				.addressDetail("SK 1260")
				.defaultYn(PfitConsts.CommonCodeConsts.YN_Y)
				.build();

		when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
		SessionFixture.setMockSession(new UserSession(SessionFixture.getUserEntity(userId)));

		//when
		final UserNotEqualsAddressException result =  assertThrows(UserNotEqualsAddressException.class, () -> addressQueryService.getAddress(addressId));

		//then
		assertEquals(result.getMessage(), AddressErrorCode.USER_NOT_EQUALS_ADDRESS.getErrorMessage());

		//verify
		verify(addressRepository, times(1)).findById(addressId);
	}

	@Test
	@DisplayName("주소 데이터가 존재하지 않는 주소 ID로 주소 조회를 요청할 경우 실패한다.")
	void getAddress_Fail_AddressNotFound() {

		//given
		long addressNotExistId = 0l;

		when(addressRepository.findById(addressNotExistId)).thenReturn(Optional.empty());

		//when
		final AddressNotFoundException result =  assertThrows(AddressNotFoundException.class, () -> addressQueryService.getAddress(addressNotExistId));

		//then
		assertEquals(result.getMessage(), AddressErrorCode.ADDRESS_NOT_FOUND_EXCEPTION.getErrorMessage());

		//verify
		verify(addressRepository, times(1)).findById(addressNotExistId);
	}

}