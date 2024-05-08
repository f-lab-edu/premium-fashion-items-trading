package com.inturn.pfit.domain.brand;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inturn.pfit.domain.brand.dto.request.CreateBrandRequestDTO;
import com.inturn.pfit.domain.brand.dto.request.ModifyBrandRequestDTO;
import com.inturn.pfit.domain.brand.dto.response.CreateBrandResponseDTO;
import com.inturn.pfit.domain.brand.service.BrandCommandService;
import com.inturn.pfit.global.common.exception.NotFoundException;
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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("junit")
@AutoConfigureMockMvc
class BrandIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private BrandCommandService brandCommandService;

	static Integer brandId;
	static String brandName;
	CreateBrandRequestDTO createBrandRequestDTO;
	ModifyBrandRequestDTO modifyBrandRequestDTO;

	@BeforeEach
	void testBefore() {
		brandId = 1;
		brandName = "Nike";
		createBrandRequestDTO = CreateBrandRequestDTO.builder()
				.brandName(brandName)
				.build();


		modifyBrandRequestDTO = modifyBrandRequestDTO.builder()
				.brandName(brandName)
				.build();
	}


	@Test
	@DisplayName("브랜드 등록(createBrand) - 성공")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void createBrand_Success() throws Exception {
		//given

		//when
		ResultActions actions = mockMvc.perform(post("/v1/brand")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createBrandRequestDTO)));

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.brandId").exists())
				.andDo(print());
	}

	@Test
	@DisplayName("브랜드 등록(createBrand) - 실패 : Unauthorized")
	@Transactional
	void createBrand_Fail_Unauthorized() throws Exception {
		//given

		//when
		ResultActions actions = mockMvc.perform(post("/v1/brand")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createBrandRequestDTO)));

		//then
		actions
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("브랜드 등록(createBrand) - 실패 : Forbidden")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_USER)
	void createBrand_Fail_Forbidden() throws Exception {
		//given

		//when
		ResultActions actions = mockMvc.perform(post("/v1/brand")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createBrandRequestDTO)));

		//then
		actions
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("브랜드 등록(createBrand) - 실패 : 브랜드 명 필수값 null")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void createBrand_Fail_BrandNameIsNull() throws Exception {
		//given
		CreateBrandRequestDTO req = CreateBrandRequestDTO.builder()
				.build();

		//when
		ResultActions actions = mockMvc.perform(post("/v1/brand")
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
	@DisplayName("브랜드 조회(getBrandById) - 성공")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void getBrandById_Success() throws Exception {
		//given
		CreateBrandResponseDTO res = brandCommandService.createBrand(createBrandRequestDTO);

		//when
		ResultActions actions = mockMvc.perform(get("/v1/brand/%s".formatted(res.brandId())));

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.brandId").value(res.brandId()))
				.andExpect(jsonPath("$.data.brandName").value(brandName))
				.andDo(print());
	}

	@Test
	@DisplayName("브랜드 조회(getBrandById) - 실패 : 존재하지 않는 브랜드 조회")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void getBrandById_Fail_NotFoundBrand() throws Exception {
		//given
		Integer notExistBrandId = 9999;

		//when
		ResultActions actions = mockMvc.perform(get("/v1/brand/%s".formatted(notExistBrandId)));

		//then
		actions
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("브랜드 조회(getBrandById) - 실패 : 브랜드 ID Null")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void getBrandById_Fail_NullBrandId() throws Exception {
		//given

		//when
		ResultActions actions = mockMvc.perform(get("/v1/brand/"));

		//TODO 해당 테스트는 고민을 해보자.
		//@NotNull에 대해서 처리할 수 있는 조건을 확인해보자.
		//then
		actions
				.andExpect(status().isInternalServerError())
				.andDo(print());
	}

	@Test
	@DisplayName("브랜드 수정(modifyBrand) - 성공")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void modifyBrand_Success() throws Exception {
		//given
		CreateBrandResponseDTO res = brandCommandService.createBrand(createBrandRequestDTO);

		ModifyBrandRequestDTO req = ModifyBrandRequestDTO.builder()
				.brandId(res.brandId())
				.brandName(brandName.repeat(2))
				.build();

		//when
		ResultActions actions = mockMvc.perform(patch("/v1/brand")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)));

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.brandId").value(res.brandId()))
				.andExpect(jsonPath("$.data.brandName").value(brandName.repeat(2)))
				.andDo(print());
	}


	@ParameterizedTest
	@DisplayName("브랜드 수정(modifyBrand) - 실패 : Valid Fail")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	@MethodSource("provideParameter")
	void modifyBrand_Fail_ValidFail(Integer brandId, String brandName) throws Exception {
		//given
		ModifyBrandRequestDTO req = ModifyBrandRequestDTO.builder()
				.brandId(brandId)
				.brandName(brandName)
				.build();

		//when
		ResultActions actions = mockMvc.perform(patch("/v1/brand")
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
				Arguments.of(null, brandName),
				Arguments.of(1, null),
				Arguments.of(1, "")
		);
	}

	@Test
	@DisplayName("브랜드 수정(modifyBrand) - 실패 : 존재하지 않는 브랜드")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void modifyBrand_Fail_NotFoundBrand() throws Exception {
		//given
		ModifyBrandRequestDTO req = ModifyBrandRequestDTO.builder()
				.brandId(brandId)
				.brandName(brandName)
				.build();

		//when
		ResultActions actions = mockMvc.perform(patch("/v1/brand")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)));

		//then
		actions
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
				.andDo(print());
	}

	//TODO - paging 관련 테스트는 추후 보완해보자.

	@ParameterizedTest
	@DisplayName("브랜드 조회 Paging(getBrandPagingList) - 성공")
	@Transactional
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	@MethodSource("providePagingParameter")
	void getBrandPagingList_Success(Integer size, Integer page, Integer listSize) throws Exception {
		//given
		String brandName = "신발";
		int createRow = 20;

		for(int i = 0; i < createRow; i++) {
			brandCommandService.createBrand(CreateBrandRequestDTO.builder()
					.brandName(brandName + i)
					.build());
		}

		//when
		ResultActions actions = mockMvc.perform(get("/v1/brand/paging")
				.param("sort", "brandSort,desc")
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
	
	//TODO - 브랜드 삭제는 추후 상품 개발완료 후 facade 서비스를 붙인 후 테스트 코드 작성
	@Test
	@DisplayName("브랜드 삭제 - 성공")
	void deleteBrand_Success() {

	}
}