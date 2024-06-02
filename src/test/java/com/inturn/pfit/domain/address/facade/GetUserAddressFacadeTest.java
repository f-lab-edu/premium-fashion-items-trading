package com.inturn.pfit.domain.address.facade;

import com.inturn.pfit.domain.address.dto.response.AddressResponseDTO;
import com.inturn.pfit.domain.address.entity.AddressEntity;
import com.inturn.pfit.domain.address.service.AddressQueryService;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.UserNotFoundException;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.domain.user.vo.UserErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundSessionException;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;
import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.support.utils.PfitConsts;
import com.inturn.pfit.support.fixture.SessionFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class GetUserAddressFacadeTest {

	@InjectMocks
	GetUserAddressFacade getUserAddressFacade;

	@Mock
	UserQueryService userQueryService;

	@Mock
	AddressQueryService addressQueryService;

	@Test
	@DisplayName("정상적인 요청 상황에서 사용자 주소록 조회를 호출하면 성공한다")
	void getAddressListByUserId_Success() {

		//given
		long addressId = 1l;
		long userId = 1l;
		UserEntity user = SessionFixture.getUserEntity(userId);
		SessionFixture.setMockSession(new UserSession(user));

		List<AddressEntity> addressEntityList = List.of(AddressEntity.builder()
				.addressId(addressId)
				.userId(userId)
				.recipients("SDC")
				.phone("010-1234-5678")
				.postCode("11111")
				.address("서울시 구로구 XXX로 0 길")
				.addressDetail("SK 1260")
				.defaultYn(PfitConsts.CommonCodeConsts.YN_Y)
				.build());

		when(userQueryService.getUserById(userId)).thenReturn(Optional.of(user));
		when(addressQueryService.getAddressListByUserId(addressId)).thenReturn(addressEntityList.stream().map(AddressResponseDTO::from).collect(Collectors.toList()));

		//when
		List<AddressResponseDTO> addressList = getUserAddressFacade.getAddressListByUserId();

		//then
		assertEquals(addressList.size(), addressEntityList.size());

		//verify
		verify(userQueryService, times(1)).getUserById(userId);
		verify(addressQueryService, times(1)).getAddressListByUserId(userId);
	}

	@Test
	@DisplayName("Session 데이터가 존재하지 않는 상황에서 사용자 주소록 조회를 호출하면 실패한다.")
	void getAddressListByUserId_Fail_SessionNotFound() {

		//given
		long userId = 1l;
		SessionFixture.setMockEmptySession();

		//when
		final NotFoundSessionException result = assertThrows(NotFoundSessionException.class, () -> getUserAddressFacade.getAddressListByUserId());

		//then
		assertEquals(result.getMessage(), CommonErrorCode.NOT_FOUND_SESSION_EXCEPTION.getErrorMessage());

		//verify
		verify(userQueryService, times(0)).getUserById(userId);
		verify(addressQueryService, times(0)).getAddressListByUserId(userId);
	}

	@Test
	@DisplayName("사용자 데이터가 존재하지 않는 상황에서 사용자 주소록 조회를 호출하면 실패한다.")
	void getAddressListByUserId_Fail_UserNotFound() {

		//given
		long userId = 1l;
		UserEntity user = SessionFixture.getUserEntity(userId);
		SessionFixture.setMockSession(new UserSession(user));

		when(userQueryService.getUserById(userId)).thenReturn(Optional.empty());

		//when
		final UserNotFoundException result = assertThrows(UserNotFoundException.class, () -> getUserAddressFacade.getAddressListByUserId());

		//then
		assertEquals(result.getMessage(), UserErrorCode.USER_NOT_FOUND.getErrorMessage());

		//verify
		verify(userQueryService, times(1)).getUserById(userId);
		verify(addressQueryService, times(0)).getAddressListByUserId(userId);
	}
}