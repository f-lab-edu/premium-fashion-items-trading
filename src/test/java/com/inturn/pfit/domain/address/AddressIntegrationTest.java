package com.inturn.pfit.domain.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inturn.pfit.domain.address.dto.request.CreateAddressRequestDTO;
import com.inturn.pfit.domain.address.dto.request.ModifyAddressRequestDTO;
import com.inturn.pfit.domain.address.dto.response.CreateAddressResponseDTO;
import com.inturn.pfit.domain.address.exception.AddressNotFoundException;
import com.inturn.pfit.domain.address.exception.UserNotEqualsAddressException;
import com.inturn.pfit.domain.address.facade.CreateAddressFacade;
import com.inturn.pfit.domain.address.facade.GetUserAddressFacade;
import com.inturn.pfit.domain.address.facade.ModifyAddressFacade;
import com.inturn.pfit.domain.address.repository.AddressRepository;
import com.inturn.pfit.domain.address.service.AddressQueryService;
import com.inturn.pfit.domain.user.dto.request.SignUpRequestDTO;
import com.inturn.pfit.domain.user.dto.response.SignUpResponseDTO;
import com.inturn.pfit.domain.user.facade.SignUpFacade;
import com.inturn.pfit.domain.user.repository.UserRepository;
import com.inturn.pfit.global.common.exception.NotFoundSessionException;
import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import com.inturn.pfit.global.support.utils.PfitConsts;
import com.inturn.pfit.support.annotation.IntegrationTest;
import com.inturn.pfit.support.fixture.CommonResponseResultFixture;
import com.inturn.pfit.support.fixture.SessionFixture;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class AddressIntegrationTest {


	@Autowired
	MockMvc mockMvc;

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

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	UserRepository userRepository;


	private ObjectMapper objectMapper = new ObjectMapper();

	static SignUpRequestDTO signUpRequestDTO;

	static CreateAddressRequestDTO createAddressRequestDTO;

	static long userId;

	@BeforeAll
	static void initBeforeTest() {
	}

	@BeforeEach
	void beforeTest() {
		signUpRequestDTO = SignUpRequestDTO.builder()
				.email("%s@naver.com".formatted(RandomStringUtils.randomAlphabetic(10)))
				.password("ABcdef1234!@")
				.confirmPassword("ABcdef1234!@")
				.alarmYn(PfitConsts.CommonCodeConsts.YN_Y)
				.roleCode(RoleConsts.ROLE_USER)
				.build();

		SignUpResponseDTO signUpRes = signUpFacade.signUp(signUpRequestDTO);

		userId = signUpRes.userId();

		createAddressRequestDTO = CreateAddressRequestDTO.builder()
				.recipients("SDC")
				.phone("010-1234-5678")
				.postCode("11111")
				.address("서울시 구로구 XXX로 0 길")
				.addressDetail("SK 1260")
				.defaultYn(PfitConsts.CommonCodeConsts.YN_Y)
				.build();
	}


	@AfterEach
	void afterTest() {
		addressRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Nested
	@DisplayName("주소 조회")
	class getAddress {
		@Test
		@DisplayName("주소 데이터가 존재하는 주소록 ID로 주소 조회를 요청할 경우 성공한다.")
		void getAddress_Success() throws Exception {

			//given
			long addressId = createAddress();

			//when
			ResultActions actions = mockMvc.perform(get("/v1/address/%d".formatted(addressId))
					.session(SessionFixture.setMockSession(new UserSession(SessionFixture.getUserEntity(userId))))
			);

			//then
			CommonResponseResultFixture.successResultActions(actions)
					.andExpect(jsonPath("$.data.addressId").value(addressId));
		}

		@Test
		@DisplayName("주소 데이터가 존재하지 않는 주소 ID로 주소 조회를 시도할 경우 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_USER)
		void getAddress_Fail_AddressNotFound() throws Exception {

			//given
			long addressNotFoundId = 0l;

			//when
			ResultActions actions = mockMvc.perform(get("/v1/address/%d".formatted(addressNotFoundId)));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof AddressNotFoundException));
		}

		@Test
		@DisplayName("Session이 없는 상태에서 주소 조회를 시도할 경우 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_USER)
		void getAddress_Fail_NotExistSession() throws Exception {

			//given
			long existAddressId = createAddress();

			//when
			ResultActions actions = mockMvc.perform(get("/v1/address/%d".formatted(existAddressId)));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundSessionException));
		}

		@Test
		@DisplayName("Session의 사용자 ID와 주소의 사용자 ID가 다를 경우 주소 조회를 요청하면 실패한다.")
		void getAddress_Fail_NotEqualAddressUserIdAndSessionUserId() throws Exception {

			//given
			long addressId = createAddress();
			long notEqualsUserId = 0l;

			//when
			ResultActions actions = mockMvc.perform(get("/v1/address/%d".formatted(addressId))
					.session(SessionFixture.setMockSession(new UserSession(SessionFixture.getUserEntity(notEqualsUserId))))
			);

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotEqualsAddressException));
		}
	}

	@Nested
	@DisplayName("사용자 주소록 조회")
	class getAddressListByUserId {

		@Test
		@DisplayName("정상적인 상황에서 사용자 주소록 조회 시 성공한다.")
		void getAddressListByUserId_Success() throws Exception {
			//given
			createAddress();

			//when
			ResultActions actions = mockMvc.perform(get("/v1/address/user")
					.session(SessionFixture.setMockSession(new UserSession(SessionFixture.getUserEntity(userId))))
			);

			//then
			CommonResponseResultFixture.successResultActions(actions)
					.andExpect(jsonPath("$.data.length()").value(1));
		}

		@Test
		@DisplayName("Session 정보가 없는 상황에서 사용자 주소록 조회 시 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_USER)
		void getAddressListByUserId_Fail_SessionNotFounnd() throws Exception {
			//given

			//when
			ResultActions actions = mockMvc.perform(get("/v1/address/user"));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundSessionException));
		}
	}


	@Nested
	@DisplayName("주소 등록")
	class createAddress {
		@Test
		@DisplayName("정상적인 상황에서 주소 등록 시 성공한다.")
		void createAddress_Success() throws Exception {
			//given

			//when
			ResultActions actions = mockMvc.perform(post("/v1/address")
					.content(objectMapper.writeValueAsString(createAddressRequestDTO))
					.contentType(MediaType.APPLICATION_JSON)
					.session(SessionFixture.setMockSession(new UserSession(SessionFixture.getUserEntity(userId))))
			);

			//then
			CommonResponseResultFixture.successResultActions(actions)
					.andExpect(jsonPath("$.data.addressId").exists());
		}

		@Test
		@DisplayName("Session 데이터가 없는 상태에서 주소 등록을 요청하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_USER)
		void createAddress_Fail_SessionNotFound() throws Exception {
			//given

			//when
			ResultActions actions = mockMvc.perform(post("/v1/address")
					.content(objectMapper.writeValueAsString(createAddressRequestDTO))
					.contentType(MediaType.APPLICATION_JSON)
			);

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundSessionException));
		}

		@Test
		@DisplayName("인증 정보가 없는 상황에서 주소 등록을 시도하면 실패한다.")
		void createAddress_Fail_Unauthorized() throws Exception {
			//given

			//when
			ResultActions actions = mockMvc.perform(post("/v1/address")
					.content(objectMapper.writeValueAsString(createAddressRequestDTO))
					.contentType(MediaType.APPLICATION_JSON)
			);

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isUnauthorized());
		}

		@Test
		@DisplayName("인증 정보가 없는 상황에서 주소 등록을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void createAddress_Fail_Forbidden() throws Exception {
			//given

			//when
			ResultActions actions = mockMvc.perform(post("/v1/address")
					.content(objectMapper.writeValueAsString(createAddressRequestDTO))
					.contentType(MediaType.APPLICATION_JSON)
			);

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isForbidden());
		}

		@ParameterizedTest
		@DisplayName("인증 정보가 없는 상황에서 주소 등록을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_USER)
		@MethodSource("provideCreateParameter")
		void createAddress_Fail_ValidateFields(String recipients, String phone, String postCode, String address, String defaultYn) throws Exception {
			//given
			CreateAddressRequestDTO req = CreateAddressRequestDTO.builder()
					.recipients(recipients)
					.phone(phone)
					.postCode(postCode)
					.address(address)
					.defaultYn(defaultYn)
					.build();

			//when
			ResultActions actions = mockMvc.perform(post("/v1/address")
					.content(objectMapper.writeValueAsString(req))
					.contentType(MediaType.APPLICATION_JSON)
			);

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
		}


		private static Stream<Arguments> provideCreateParameter() {
			return Stream.of(
					Arguments.of(null, "010-1234-5678", "11111", "서울시 구로구 XXX로 0 길", PfitConsts.CommonCodeConsts.YN_Y),
					Arguments.of("홍길동", null, "11111", "서울시 구로구 XXX로 0 길", PfitConsts.CommonCodeConsts.YN_Y),
					Arguments.of("홍길동", "11111", "11111", "서울시 구로구 XXX로 0 길", PfitConsts.CommonCodeConsts.YN_Y),
					Arguments.of("홍길동", StringUtils.EMPTY, "11111", "서울시 구로구 XXX로 0 길", PfitConsts.CommonCodeConsts.YN_Y),
					Arguments.of("홍길동", "010-1234-5678", null, "서울시 구로구 XXX로 0 길", PfitConsts.CommonCodeConsts.YN_Y),
					Arguments.of("홍길동", "010-1234-5678", "Test111", "서울시 구로구 XXX로 0 길", PfitConsts.CommonCodeConsts.YN_Y),
					Arguments.of("홍길동", "010-1234-5678", "Test111", null, PfitConsts.CommonCodeConsts.YN_Y),
					Arguments.of("홍길동", "010-1234-5678", "Test111", "서울시 구로구 XXX로 0 길", null),
					Arguments.of("홍길동", "010-1234-5678", "Test111", "서울시 구로구 XXX로 0 길", "ERROR")

			);
		}
	}

	@Nested
	@DisplayName("주소 편집")
	class modifyAddress {

		@Test
		@DisplayName("정상적인 상황에서 주소 편집 시 성공한다.")
		void modifyAddress_Success() throws Exception {
			//given
			long addressId = createAddress();

			ModifyAddressRequestDTO req = ModifyAddressRequestDTO.builder()
					.addressId(addressId)
					.recipients(createAddressRequestDTO.recipients().repeat(2))
					.phone("010-9876-5432")
					.postCode("99999")
					.address(createAddressRequestDTO.address().repeat(2))
					.addressDetail(createAddressRequestDTO.addressDetail().repeat(2))
					.defaultYn(PfitConsts.CommonCodeConsts.YN_Y)
					.build();

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/address")
					.content(objectMapper.writeValueAsString(req))
					.contentType(MediaType.APPLICATION_JSON)
					.session(SessionFixture.setMockSession(new UserSession(SessionFixture.getUserEntity(userId))))
			);

			//then
			CommonResponseResultFixture.successResultActions(actions)
					.andExpect(jsonPath("$.data.addressId").value(addressId))
					.andExpect(jsonPath("$.data.recipients").value(req.recipients()))
					.andExpect(jsonPath("$.data.phone").value(req.phone()))
					.andExpect(jsonPath("$.data.postCode").value(req.postCode()))
					.andExpect(jsonPath("$.data.address").value(req.address()))
					.andExpect(jsonPath("$.data.addressDetail").value(req.addressDetail()))
					.andExpect(jsonPath("$.data.defaultYn").value(req.defaultYn()));
		}

		@Test
		@DisplayName("Session 정보가 없는 상황에서 주소 편집을 요청하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_USER)
		void modifyAddress_Fail_SessionNotFound() throws Exception {
			//given
			long addressId = createAddress();

			ModifyAddressRequestDTO req = ModifyAddressRequestDTO.builder()
					.addressId(addressId)
					.recipients(createAddressRequestDTO.recipients().repeat(2))
					.phone("010-9876-5432")
					.postCode("99999")
					.address(createAddressRequestDTO.address().repeat(2))
					.addressDetail(createAddressRequestDTO.addressDetail().repeat(2))
					.defaultYn(PfitConsts.CommonCodeConsts.YN_Y)
					.build();

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/address")
					.content(objectMapper.writeValueAsString(req))
					.contentType(MediaType.APPLICATION_JSON)
			);

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundSessionException));
		}

		@Test
		@DisplayName("Session 정보가 없는 상황에서 주소 편집을 요청하면 실패한다.")
		void modifyAddress_Fail_AddressNotFound() throws Exception {
			//given
			long addressNotFoundId = 0l;

			ModifyAddressRequestDTO req = ModifyAddressRequestDTO.builder()
					.addressId(addressNotFoundId)
					.recipients(createAddressRequestDTO.recipients().repeat(2))
					.phone("010-9876-5432")
					.postCode("99999")
					.address(createAddressRequestDTO.address().repeat(2))
					.addressDetail(createAddressRequestDTO.addressDetail().repeat(2))
					.defaultYn(PfitConsts.CommonCodeConsts.YN_Y)
					.build();

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/address")
					.content(objectMapper.writeValueAsString(req))
					.contentType(MediaType.APPLICATION_JSON)
					.session(SessionFixture.setMockSession(new UserSession(SessionFixture.getUserEntity(userId))))
			);

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof AddressNotFoundException));

		}
	}

	private long createAddress() {

		SessionFixture.setMockSession(new UserSession(SessionFixture.getUserEntity(userId)));

		CreateAddressResponseDTO res = createAddressFacade.createAddress(createAddressRequestDTO);

		return res.addressId();
	}
}
