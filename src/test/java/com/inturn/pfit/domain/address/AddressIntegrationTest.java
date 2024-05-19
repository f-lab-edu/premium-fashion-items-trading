package com.inturn.pfit.domain.address;

import com.inturn.pfit.domain.address.exception.AddressNotFoundException;
import com.inturn.pfit.domain.address.facade.CreateAddressFacade;
import com.inturn.pfit.domain.address.facade.GetUserAddressFacade;
import com.inturn.pfit.domain.address.facade.ModifyAddressFacade;
import com.inturn.pfit.domain.address.service.AddressQueryService;
import com.inturn.pfit.domain.user.dto.request.SignUpRequestDTO;
import com.inturn.pfit.domain.user.dto.response.SignUpResponseDTO;
import com.inturn.pfit.domain.user.facade.SignUpFacade;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import com.inturn.pfit.global.support.utils.PfitConsts;
import com.inturn.pfit.support.annotation.IntegrationTest;
import com.inturn.pfit.support.fixture.CommonResponseResultFixture;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class AddressIntegrationTest {


	@Autowired
	private MockMvc mockMvc;

	@Autowired
	AddressQueryService addressQueryService;

	@Autowired
	ModifyAddressFacade modifyAddressFacade;

	@Autowired
	CreateAddressFacade createAddressFacade;

	@Autowired
	GetUserAddressFacade getUserAddressFacade;

	@Autowired
	SignUpFacade signUpFacade;

	static SignUpRequestDTO signUpRequestDTO;

	@BeforeAll
	static void initBeforeTest() {
		signUpRequestDTO = SignUpRequestDTO.builder()
				.email("%s@naver.com".formatted(RandomStringUtils.randomAlphabetic(10)))
				.password("ABcdef1234!@")
				.confirmPassword("ABcdef1234!@")
				.alarmYn(PfitConsts.CommonCodeConsts.YN_Y)
				.roleCode(RoleConsts.ROLE_USER)
				.build();
	}

	@BeforeEach
	void beforeTest() {
	}

	@Test
	@DisplayName("주소록 데이터가 존재하는 주소록 ID로 주소록 조회를 요청할 경우 성공한다.")
	@WithMockUser(authorities = RoleConsts.ROLE_USER)
	void getAddress_Success() throws Exception {

		//given
		SignUpResponseDTO res = signUpFacade.signUp(signUpRequestDTO);

		long userId = res.userId();

		//when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/v1/address/%d".formatted(userId)));

		//then
		CommonResponseResultFixture.successResultActions(actions)
				.andExpect(jsonPath("$.data.userId").value(userId));
	}

	@Test
	@DisplayName("존재하지 않는 주소록 ID로 주소록 조회를 요청할 경우 실패한다.")
	@WithMockUser(authorities = RoleConsts.ROLE_USER)
	void getAddress_Fail_UserNotFound() throws Exception {

		//given
		long notFoundUserId = 0l;

		//when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/v1/address/%d".formatted(notFoundUserId)));

		//then
		CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof AddressNotFoundException));
	}
}
