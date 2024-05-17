package com.inturn.pfit.domain.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inturn.pfit.domain.category.dto.request.CreateCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.request.ModifyCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CreateCategoryResponseDTO;
import com.inturn.pfit.domain.category.exception.CategoryNotFoundException;
import com.inturn.pfit.domain.category.exception.ExistCategoryOrderException;
import com.inturn.pfit.domain.category.service.CategoryCommandService;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import com.inturn.pfit.support.annotation.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class CategoryIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private CategoryCommandService categoryCommandService;

	static Integer categoryId;
	static String categoryName;
	static Integer categoryOrder;
	CreateCategoryRequestDTO createCategoryRequestDTO;
	ModifyCategoryRequestDTO modifyCategoryRequestDTO;

	@BeforeEach
	void testBefore() {
		categoryId = 1;
		categoryName = "신발";
		categoryOrder = 1;
		createCategoryRequestDTO = CreateCategoryRequestDTO.builder()
				.categoryName(categoryName)
				.categoryOrder(categoryOrder)
				.build();


		modifyCategoryRequestDTO = modifyCategoryRequestDTO.builder()
				.categoryName(categoryName)
				.categoryOrder(categoryOrder)
				.build();
	}


	@Test
	@DisplayName("카테고리 등록 성공")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void createCategory_Success() throws Exception {
		//given

		//when
		ResultActions actions = mockMvc.perform(post("/v1/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createCategoryRequestDTO)));

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.categoryId").exists())
				.andDo(print());
	}

	@Test
	@DisplayName("이미 등록된 카테고리 순번으로 등록하여 실패")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void createCategory_Fail_ExistCategoryOrder() throws Exception {
		//given

		//이미 해당 객체를 먼저 등록 - 실패 이유
		categoryCommandService.createCategory(createCategoryRequestDTO);

		//when
		ResultActions actions = mockMvc.perform(post("/v1/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createCategoryRequestDTO)));

		//then
		actions
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ExistCategoryOrderException))
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("인증 실패로 카테고리 등록 실패")
	@Transactional
	void createCategory_Fail_Unauthorized() throws Exception {
		//given

		//when
		ResultActions actions = mockMvc.perform(post("/v1/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createCategoryRequestDTO)));

		//then
		actions
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("권한이 달라 카테고리 등록 실패")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_USER)
	void createCategory_Fail_Forbidden() throws Exception {
		//given

		//when
		ResultActions actions = mockMvc.perform(post("/v1/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createCategoryRequestDTO)));

		//then
		actions
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("카테고리 명이 없어 카테고리 등록 실패")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void createCategory_Fail_CategoryNameIsNull() throws Exception {
		//given
		CreateCategoryRequestDTO req = CreateCategoryRequestDTO.builder()
				.categoryOrder(categoryOrder)
				.build();

		//when
		ResultActions actions = mockMvc.perform(post("/v1/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)));

		//then
		actions
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("카테고리 조회 성공")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void getCategoryById_Success() throws Exception {
		//given
		CreateCategoryResponseDTO res = categoryCommandService.createCategory(createCategoryRequestDTO);

		//when
		ResultActions actions = mockMvc.perform(get("/v1/category/%s".formatted(res.categoryId())));

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.categoryId").value(res.categoryId()))
				.andExpect(jsonPath("$.data.categoryName").value(categoryName))
				.andExpect(jsonPath("$.data.categoryOrder").value(categoryOrder))
				.andDo(print());
	}

	@Test
	@DisplayName("존재하지 않는 카테고리 ID로 카테고리를 조회하여 NotFoundCategoryException 발생 실패")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void getCategoryById_Fail_NotFoundCategory() throws Exception {
		//given
		Integer notExistCategoryId = 9999;

		//when
		ResultActions actions = mockMvc.perform(get("/v1/category/%s".formatted(notExistCategoryId)));

		//then
		actions
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryNotFoundException))
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("카테고리 ID가 null 이기 때문에 카테고리 조회 실패")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void getCategoryById_Fail_NullCategoryId() throws Exception {
		//given

		//when
		ResultActions actions = mockMvc.perform(get("/v1/category/"));

		//TODO 해당 테스트는 고민을 해보자.
		//@NotNull에 대해서 처리할 수 있는 조건을 확인해보자.
		//then
		actions
				.andExpect(status().isInternalServerError())
				.andDo(print());
	}

	@Test
	@DisplayName("카테고리 수정 성공")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void modifyCategory_Success() throws Exception {
		//given
		CreateCategoryResponseDTO res = categoryCommandService.createCategory(createCategoryRequestDTO);

		ModifyCategoryRequestDTO req = ModifyCategoryRequestDTO.builder()
				.categoryId(res.categoryId())
				.categoryName(categoryName.repeat(2))
				.categoryOrder(categoryOrder + 1)
				.build();

		//when
		ResultActions actions = mockMvc.perform(patch("/v1/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)));

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.categoryId").value(res.categoryId()))
				.andExpect(jsonPath("$.data.categoryName").value(categoryName.repeat(2)))
				.andExpect(jsonPath("$.data.categoryOrder").value(categoryOrder + 1))
				.andDo(print());
	}


	@ParameterizedTest
	@DisplayName("ModifyCategoryRequestDTO 의 파라미터가 validate 되어 카테고리 수정 실패")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	@MethodSource("provideParameter")
	void modifyCategory_Fail_ValidFail(Integer categoryId, String categoryName, Integer categoryOrder) throws Exception {
		//given
		ModifyCategoryRequestDTO req = ModifyCategoryRequestDTO.builder()
				.categoryId(categoryId)
				.categoryName(categoryName)
				.categoryOrder(categoryOrder)
				.build();

		//when
		ResultActions actions = mockMvc.perform(patch("/v1/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)));

		//then
		actions
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	private static Stream<Arguments> provideParameter() {
		return Stream.of(
				Arguments.of(null, categoryName, categoryOrder),
				Arguments.of(1, null, categoryOrder),
				Arguments.of(1, "", categoryOrder),
				Arguments.of(1, categoryName, null)
		);
	}

	@Test
	@DisplayName("카테고리 데이터가 존재하지 않는 ID로 카테고리 수정을 호출하여 NotFoundCategoryException 발생 실패")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void modifyCategory_Fail_NotFoundCategory() throws Exception {
		//given
		ModifyCategoryRequestDTO req = ModifyCategoryRequestDTO.builder()
				.categoryId(categoryId)
				.categoryName(categoryName)
				.categoryOrder(categoryOrder)
				.build();

		//when
		ResultActions actions = mockMvc.perform(patch("/v1/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)));

		//then
		actions
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryNotFoundException))
				.andDo(print());
	}

	//TODO - paging 관련 테스트는 추후 보완해보자.

	@ParameterizedTest
	@DisplayName("카테고리 조회 성공")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	@MethodSource("providePagingParameter")
	void getCategoryPagingList_Success(Integer size, Integer page, Integer listSize) throws Exception {
		//given
		String categoryName = "신발";
		Integer categoryOrder = 1;
		int createRow = 20;

		for(int i = 0; i < createRow; i++) {
			categoryCommandService.createCategory(CreateCategoryRequestDTO.builder()
					.categoryOrder(categoryOrder + i)
					.categoryName(categoryName + i)
					.build());
		}

		//when
		ResultActions actions = mockMvc.perform(get("/v1/category/paging")
				.param("sort", "categoryOrder,desc")
				.param("size", size.toString())
				.param("page", page.toString())
		);

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.content.length()").value(listSize))
				.andDo(print());
	}

	private static Stream<Arguments> providePagingParameter() {
		return Stream.of(
				Arguments.of( 3, 0, 3),
				//20개를 등록하고 나머지 3 * 6 = 18, 나머지는 2개
				Arguments.of( 3, 6, 2)
		);
	}
	
	//TODO - 카테고리 삭제는 추후 상품 개발완료 후 facade 서비스를 붙인 후 테스트 코드 작성
	@Test
	@DisplayName("카테고리 삭제 - 성공")
	void deleteCategory_Success() {

	}
}