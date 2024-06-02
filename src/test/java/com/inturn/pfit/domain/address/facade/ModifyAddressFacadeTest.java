package com.inturn.pfit.domain.address.facade;

import com.inturn.pfit.domain.address.dto.request.ModifyAddressRequestDTO;
import com.inturn.pfit.domain.address.dto.response.AddressResponseDTO;
import com.inturn.pfit.domain.address.entity.AddressEntity;
import com.inturn.pfit.domain.address.exception.AddressNotFoundException;
import com.inturn.pfit.domain.address.service.AddressCommandService;
import com.inturn.pfit.domain.address.service.AddressQueryService;
import com.inturn.pfit.domain.address.vo.AddressErrorCode;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.UserNotFoundException;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.domain.user.vo.UserErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundSessionException;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;
import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.support.utils.PfitConsts;
import com.inturn.pfit.support.fixture.SessionFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ModifyAddressFacadeTest {

	@InjectMocks
	ModifyAddressFacade modifyAddressFacade;

	@Mock
	AddressCommandService addressCommandService;

	@Mock
	AddressQueryService addressQueryService;

	@Mock
	UserQueryService userQueryService;

	static long addressId = 1l;

	static ModifyAddressRequestDTO req;

	@BeforeAll
	static void testBefore() {
		req = ModifyAddressRequestDTO.builder()
				.addressId(addressId)
				.recipients("SDC")
				.phone("010-1234-5678")
				.postCode("11111")
				.address("서울시 구로구 XXX로 0 길")
				.addressDetail("SK 1260")
				.defaultYn(PfitConsts.CommonCodeConsts.YN_Y)
				.build();
	}

	@Test
	@DisplayName("정상적인 요청 상황에서 주소 편집을 호출하면 성공한다.")
	void modifyAddress_Success() {
		long addressId = 1l;
		long userId = 1l;
		UserEntity user = SessionFixture.getUserEntity(userId).toBuilder().addressList(List.of()).build();
		SessionFixture.setMockSession(new UserSession(user));

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

		AddressEntity modAddress = AddressEntity.builder()
				.addressId(addressId)
				.userId(userId)
				.recipients(address.getRecipients().repeat(2))
				.phone("010-9876-5432")
				.postCode("99999")
				.address(address.getAddress().repeat(2))
				.defaultYn(PfitConsts.CommonCodeConsts.YN_Y)
				.build();

		when(userQueryService.getUserById(userId)).thenReturn(Optional.of(user));
		when(addressQueryService.getAddressById(addressId)).thenReturn(Optional.of(address));
		when(addressCommandService.save(any(AddressEntity.class))).thenReturn(modAddress);

		//when
		AddressResponseDTO result = modifyAddressFacade.modifyAddress(req);

		//then
		assertEquals(result.getAddressId(), addressId);
		assertEquals(result.getRecipients(), modAddress.getRecipients());
		assertEquals(result.getPhone(), modAddress.getPhone());
		assertEquals(result.getPostCode(), modAddress.getPostCode());
		assertEquals(result.getAddress(), modAddress.getAddress());
		assertEquals(result.getDefaultYn(), modAddress.getDefaultYn());

		//verify
		verify(userQueryService, times(1)).getUserById(userId);
		verify(addressQueryService, times(1)).getAddressById(userId);
		verify(addressCommandService, times(1)).save(any(AddressEntity.class));
	}

	@Test
	@DisplayName("Session 데이터가 존재하지 않는 상황에서 주소 편집을 호출하면 실패한다.")
	void createAddress_Fail_SessionNotFound() {

		//given
		long userId = 1l;
		SessionFixture.setMockEmptySession();

		//when
		final NotFoundSessionException result = assertThrows(NotFoundSessionException.class, () -> modifyAddressFacade.modifyAddress(req));

		//then
		assertEquals(result.getMessage(), CommonErrorCode.NOT_FOUND_SESSION_EXCEPTION.getErrorMessage());

		//verify
		verify(userQueryService, times(0)).getUserById(userId);
		verify(addressQueryService, times(0)).getAddressById(userId);
		verify(addressCommandService, times(0)).save(any(AddressEntity.class));
	}
	
	@Test
	@DisplayName("사용자 데이터가 존재하지 않는 사용자 ID로 주소 편집을 요청할 경우 싫패한다.")
	void createAddress_Fail_UserNotFound() {

		//given
		long userId = 1l;
		UserEntity user = SessionFixture.getUserEntity(userId);
		SessionFixture.setMockSession(new UserSession(user));

		//when
		final UserNotFoundException result = assertThrows(UserNotFoundException.class, () -> modifyAddressFacade.modifyAddress(req));

		//then
		assertEquals(result.getMessage(), UserErrorCode.USER_NOT_FOUND.getErrorMessage());

		//verify
		verify(userQueryService, times(1)).getUserById(userId);
		verify(addressQueryService, times(0)).getAddressById(userId);
		verify(addressCommandService, times(0)).save(any(AddressEntity.class));
	}

	@Test
	@DisplayName("주소 데이터가 없는 주소 ID로 주소 편집을 요청하면 실패한다.")
	void modifyAddress_Fail_AddressNotFound() {
		long addressId = 1l;
		long userId = 1l;
		UserEntity user = SessionFixture.getUserEntity(userId).toBuilder().addressList(List.of()).build();
		SessionFixture.setMockSession(new UserSession(user));

		when(userQueryService.getUserById(userId)).thenReturn(Optional.of(user));
		when(addressQueryService.getAddressById(addressId)).thenReturn(Optional.empty());

		//when
		final AddressNotFoundException result = assertThrows(AddressNotFoundException.class, () -> modifyAddressFacade.modifyAddress(req));

		//then
		assertEquals(result.getMessage(), AddressErrorCode.ADDRESS_NOT_FOUND_EXCEPTION.getErrorMessage());

		//verify
		verify(userQueryService, times(1)).getUserById(userId);
		verify(addressQueryService, times(1)).getAddressById(userId);
		verify(addressCommandService, times(0)).save(any(AddressEntity.class));
	}
}