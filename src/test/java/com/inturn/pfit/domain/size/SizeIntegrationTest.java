package com.inturn.pfit.domain.size;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inturn.pfit.domain.category.dto.request.CreateCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CreateCategoryResponseDTO;
import com.inturn.pfit.domain.category.exception.NotFoundCategoryException;
import com.inturn.pfit.domain.category.service.CategoryCommandService;
import com.inturn.pfit.domain.size.dto.request.CreateSizeRequestDTO;
import com.inturn.pfit.domain.size.dto.request.ModifySizeRequestDTO;
import com.inturn.pfit.domain.size.dto.response.CreateSizeResponseDTO;
import com.inturn.pfit.domain.size.facade.CreateSizeFacade;
import com.inturn.pfit.domain.sizetype.dto.request.CreateSizeTypeRequestDTO;
import com.inturn.pfit.domain.sizetype.exception.DuplicateSizeTypeOrderException;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("junit")
@AutoConfigureMockMvc
class SizeIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CategoryCommandService categoryCommandService;

	@Autowired
	private CreateSizeFacade createSizeFacade;

	private ObjectMapper objectMapper = new ObjectMapper();

	static Integer categoryId;

	static String sizeName = "한국 신발 사이즈";

	static List<CreateSizeTypeRequestDTO> sizeTypeList = List.of(
			CreateSizeTypeRequestDTO.builder().sizeTypeName("275").sizeTypeOrder(1).build(),
			CreateSizeTypeRequestDTO.builder().sizeTypeName("280").sizeTypeOrder(2).build(),
			CreateSizeTypeRequestDTO.builder().sizeTypeName("285").sizeTypeOrder(3).build()
	);

	CreateSizeRequestDTO createSizeRequestDTO;

	//TODO - BeforeAll로 한번만 쓸 수 없나 ??
	//아니면 BeforeEach 이기 때문에 매일 등록될 텐데..
	//상위 데이터를 넣어놓는 방법 찾아보자.
	@BeforeEach
	void testStart() {

		//상위 테이블인 카테고리를 먼저 등록
		CreateCategoryResponseDTO res = categoryCommandService.createCategory(CreateCategoryRequestDTO.builder()
				.categoryName("신발")
				.categoryOrder(1)
				.build()
		);
		categoryId = res.categoryId();

		//사이즈 등록 요청 데이터 set
		createSizeRequestDTO = CreateSizeRequestDTO.builder()
				.sizeName(sizeName)
				.categoryId(categoryId)
				.sizeTypeList(sizeTypeList)
				.build();
	}

	@Test
	@DisplayName("정상적인 요청에서 사이즈 등록을 시도할 경우 성공한다.")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void createSize_Success() throws Exception {

		//given

		//when
		ResultActions actions = mockMvc.perform(post("/v1/size")
						.content(objectMapper.writeValueAsString(createSizeRequestDTO))
						.contentType(MediaType.APPLICATION_JSON));

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.sizeId").exists())
				.andDo(print());

	}

	@Test
	@DisplayName("사이즈 종류 순번이 중복인 요청에서 사이즈 등록을 시도할 경우 실패한다.")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void createSize_Fail_DuplicateSizeTypeOrder() throws Exception {

		//given
		List<CreateSizeTypeRequestDTO> sizeTypeList = List.of(
				CreateSizeTypeRequestDTO.builder().sizeTypeName("275").sizeTypeOrder(1).build(),
				CreateSizeTypeRequestDTO.builder().sizeTypeName("280").sizeTypeOrder(1).build()
		);

		CreateSizeRequestDTO createSizeRequestDTO = CreateSizeRequestDTO.builder()
				.sizeName(sizeName)
				.categoryId(categoryId)
				.sizeTypeList(sizeTypeList)
				.build();

		//when
		ResultActions actions = mockMvc.perform(post("/v1/size")
				.content(objectMapper.writeValueAsString(createSizeRequestDTO))
				.contentType(MediaType.APPLICATION_JSON));

		//then
		actions
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateSizeTypeOrderException))
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("카테고리 데이터가 존재하지 않는 상황에서 사이즈 등록을 시도할 경우 실패한다.")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void createSize_Fail_NotFoundCategory() throws Exception {

		//given
		Integer notExistCategoryId = 0;

		CreateSizeRequestDTO req = CreateSizeRequestDTO.builder()
				.sizeName(sizeName)
				.categoryId(notExistCategoryId)
				.sizeTypeList(sizeTypeList)
				.build();

		//when
		ResultActions actions = mockMvc.perform(post("/v1/size")
				.content(objectMapper.writeValueAsString(req))
				.contentType(MediaType.APPLICATION_JSON));

		//then
		actions
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundCategoryException))
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@ParameterizedTest
	@DisplayName("필수값이 존재하지 않는 상황에서 사이즈 등록을 호출하면 실패한다.")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	@MethodSource("provideCreateSizeParameter")
	void createSize_Fail_ValidateNotNullFields(String sizeName, Integer categoryId, List<CreateSizeTypeRequestDTO> sizeTypeList) throws Exception {

		//given
		CreateSizeRequestDTO req = CreateSizeRequestDTO.builder()
				.sizeName(sizeName)
				.categoryId(categoryId)
				.sizeTypeList(sizeTypeList)
				.build();

		//when
		ResultActions actions = mockMvc.perform(post("/v1/size")
				.content(objectMapper.writeValueAsString(req))
				.contentType(MediaType.APPLICATION_JSON));

		//then
		actions
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	private static Stream<Arguments> provideCreateSizeParameter() {
		return Stream.of(
				Arguments.of(null, 1, sizeTypeList),
				Arguments.of("", 1, sizeTypeList),
				Arguments.of(sizeName, null, sizeTypeList),
				Arguments.of(sizeName, 1, null),
				Arguments.of(sizeName, 1, List.of()),
				Arguments.of(sizeName, 1, List.of(CreateSizeTypeRequestDTO.builder().build()))
		);
	}

	@Test
	@DisplayName("인증 정보가 없는 상황에서 브랜드 등록을 시도하면 실패한다.")
	@Transactional
	void createSize_Fail_Unauthorized() throws Exception {
		//given

		//when
		ResultActions actions = mockMvc.perform(post("/v1/size")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createSizeRequestDTO)));

		//then
		actions
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("권한이 없는 상황에서 사이즈 등록을 시도하면 실패한다.")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_USER)
	void createSize_Fail_Forbidden() throws Exception {
		//given

		//when
		ResultActions actions = mockMvc.perform(post("/v1/size")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createSizeRequestDTO)));

		//then
		actions
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("정상적인 요청으로 사이즈 편집을 시도하면 성공한다.")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void modifySize_Success() {

		//given
		//Size 등록
		CreateSizeResponseDTO responseDTO = createSizeFacade.createSize(createSizeRequestDTO);

//		List<ModifySizeTypeRequestDTO>

		ModifySizeRequestDTO modifySizeRequestDTO = ModifySizeRequestDTO.builder()
				.sizeId(responseDTO.sizeId())
				.sizeName(sizeName.repeat(2))
				.categoryId(categoryId)
				.build();

		//when

		//then
	}
}