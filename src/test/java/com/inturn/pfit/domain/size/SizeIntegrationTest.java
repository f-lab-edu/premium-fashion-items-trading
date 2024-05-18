package com.inturn.pfit.domain.size;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inturn.pfit.domain.category.dto.request.CreateCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CreateCategoryResponseDTO;
import com.inturn.pfit.domain.category.exception.CategoryNotFoundException;
import com.inturn.pfit.domain.category.repository.CategoryRepository;
import com.inturn.pfit.domain.category.service.CategoryCommandService;
import com.inturn.pfit.domain.size.dto.request.CreateSizeRequestDTO;
import com.inturn.pfit.domain.size.dto.request.ModifySizeRequestDTO;
import com.inturn.pfit.domain.size.dto.response.CreateSizeResponseDTO;
import com.inturn.pfit.domain.size.exception.SizeNotFoundException;
import com.inturn.pfit.domain.size.facade.CreateSizeFacade;
import com.inturn.pfit.domain.size.repository.SizeRepository;
import com.inturn.pfit.domain.size.service.SizeQueryService;
import com.inturn.pfit.domain.sizetype.dto.request.CreateSizeTypeRequestDTO;
import com.inturn.pfit.domain.sizetype.dto.request.ModifySizeTypeRequestDTO;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.domain.sizetype.exception.DuplicateSizeTypeOrderException;
import com.inturn.pfit.domain.sizetype.exception.SizeTypeNotFoundException;
import com.inturn.pfit.domain.sizetype.repository.SizeTypeRepository;
import com.inturn.pfit.global.common.vo.CUDRequestCommand;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import com.inturn.pfit.support.annotation.IntegrationTest;
import com.inturn.pfit.support.fixture.CommonResponseResultFixture;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class SizeIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CategoryCommandService categoryCommandService;

	@Autowired
	private SizeQueryService sizeQueryService;

	@Autowired
	private CreateSizeFacade createSizeFacade;

	@Autowired
	private SizeTypeRepository sizeTypeRepository;
	@Autowired
	private SizeRepository sizeRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TransactionTemplate transactionTemplate;


	private ObjectMapper objectMapper = new ObjectMapper();

	static Integer categoryId;

	static String sizeName = "한국 신발 사이즈";

	static List<CreateSizeTypeRequestDTO> createSizeTypeList = List.of(
			CreateSizeTypeRequestDTO.builder().sizeTypeName("275").sizeTypeOrder(1).build(),
			CreateSizeTypeRequestDTO.builder().sizeTypeName("280").sizeTypeOrder(2).build(),
			CreateSizeTypeRequestDTO.builder().sizeTypeName("285").sizeTypeOrder(3).build()
	);

	static List<ModifySizeTypeRequestDTO> modifySizeTypeList = List.of(
			ModifySizeTypeRequestDTO.builder().sizeTypeName("275").sizeTypeOrder(1).cudRequestCommand(CUDRequestCommand.CREATE).build(),
			ModifySizeTypeRequestDTO.builder().sizeTypeName("280").sizeTypeOrder(2).cudRequestCommand(CUDRequestCommand.CREATE).build(),
			ModifySizeTypeRequestDTO.builder().sizeTypeName("285").sizeTypeOrder(3).cudRequestCommand(CUDRequestCommand.CREATE).build()
	);

	CreateSizeRequestDTO createSizeRequestDTO;

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
				.sizeTypeList(createSizeTypeList)
				.build();
	}

	@AfterEach
	void testEnd() {
		sizeTypeRepository.deleteAll();
		sizeRepository.deleteAll();
		categoryRepository.deleteAll();
	}

	@Nested
	@DisplayName("사이즈 등록")
	class createSize {
		@Test
		@DisplayName("정상적인 요청에서 사이즈 등록을 시도할 경우 성공한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void createSize_Success() throws Exception {

			//given

			//when
			ResultActions actions = mockMvc.perform(post("/v1/size")
					.content(objectMapper.writeValueAsString(createSizeRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.successResultActions(actions)
					.andExpect(jsonPath("$.data.sizeId").exists());
		}

		@Test
		@DisplayName("사이즈 종류 순번이 중복인 요청에서 사이즈 등록을 시도할 경우 실패한다.")
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
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void createSize_Fail_NotFoundCategory() throws Exception {

			//given
			Integer notExistCategoryId = 0;

			CreateSizeRequestDTO req = CreateSizeRequestDTO.builder()
					.sizeName(sizeName)
					.categoryId(notExistCategoryId)
					.sizeTypeList(createSizeTypeList)
					.build();

			//when
			ResultActions actions = mockMvc.perform(post("/v1/size")
					.content(objectMapper.writeValueAsString(req))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			actions
					.andExpect(status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryNotFoundException))
					.andExpect(jsonPath("$.data").doesNotExist())
					.andExpect(jsonPath("$.success").value(false))
					.andDo(print());
		}

		@ParameterizedTest
		@DisplayName("필수값이 존재하지 않는 상황에서 사이즈 등록을 호출하면 실패한다.")
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
					Arguments.of(null, 1, createSizeTypeList),
					Arguments.of("", 1, createSizeTypeList),
					Arguments.of(sizeName, null, createSizeTypeList),
					Arguments.of(sizeName, 1, null),
					Arguments.of(sizeName, 1, List.of()),
					Arguments.of(sizeName, 1, List.of(CreateSizeTypeRequestDTO.builder().build()))
			);
		}

		@Test
		@DisplayName("인증 정보가 없는 상황에서 브랜드 등록을 시도하면 실패한다.")
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
	}

	@Nested
	@DisplayName("사이즈 단일 조회")
	class getSizeById {
		//TODO - 이 부분은 확인.. 왜? 지연 로딩이 안될까 ? 트랜잭션을 뺴야되네..
		@Test
		@DisplayName("사이즈 데이터가 존재하는 정상적인 요청인 상황에서 사이즈 단일 조회를 시도하면 성공한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void getSizeById_Success() throws Exception {

			//given
			//size 등록
			CreateSizeResponseDTO res = createSizeFacade.createSize(createSizeRequestDTO);
			//when
			ResultActions actions = mockMvc.perform(get("/v1/size/%d".formatted(res.sizeId())));

			//then
			CommonResponseResultFixture.successResultActions(actions)
					.andExpect(jsonPath("$.data.sizeId").value(res.sizeId()))
					.andExpect(jsonPath("$.data.sizeTypeList.length()").value(createSizeTypeList.size()));
		}

		@Test
		@DisplayName("사이즈 데이터가 존재하지 않는 상황에서 사이즈 단일 조회를 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void getSizeById_Fail_NotFoundSize() throws Exception {

			//given

			//when
			Integer notFoundSizeId = 0;
			ResultActions actions = mockMvc.perform(get("/v1/size/%d".formatted(notFoundSizeId)));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof SizeNotFoundException));
		}

		@Test
		@DisplayName("사이즈 데이터가 존재하지 않는 상황에서 사이즈 단일 조회를 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void getSizeById_Fail_ValidNullSizeId() throws Exception {

			//given

			//when
			Integer notFoundSizeId = 0;
			ResultActions actions = mockMvc.perform(get("/v1/size"));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isInternalServerError());
		}
	}
	
	@Nested
	@DisplayName("사이즈 편집")
	class modifySize{

		@Test
		@DisplayName("정상적인 요청으로 사이즈 편집을 시도하면 성공한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void modifySize_Success() throws Exception {

			//given
			//Size 등록
			CreateSizeResponseDTO responseDTO = createSizeFacade.createSize(createSizeRequestDTO);

			final int updateIdx = 0;
			final int deleteIdx = 1;
			//등록된 데이터를 조회하여 UPDATE / DELETE 항목 결정
			final List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList = transactionTemplate.execute(status -> {
				List<SizeTypeEntity> sizeTypeEntityList = sizeQueryService.getSizeById(responseDTO.sizeId()).getSizeTypeList();
				List<ModifySizeTypeRequestDTO> list = new ArrayList<>();
				//UPDATE할 항목 선정
				list.add(
						ModifySizeTypeRequestDTO.builder().cudRequestCommand(CUDRequestCommand.UPDATE).sizeTypeOrder(sizeTypeEntityList.get(updateIdx).getSizeTypeOrder())
								.sizeTypeId(sizeTypeEntityList.get(updateIdx).getSizeTypeId())
								.sizeTypeName(sizeTypeEntityList.get(updateIdx).getSizeTypeName().repeat(2)).build()
				);
				list.add(
						ModifySizeTypeRequestDTO.builder().cudRequestCommand(CUDRequestCommand.DELETE).sizeTypeId(sizeTypeEntityList.get(deleteIdx).getSizeTypeId())
								.sizeTypeOrder(sizeTypeEntityList.get(deleteIdx).getSizeTypeOrder())
								.sizeTypeName(sizeTypeEntityList.get(deleteIdx).getSizeTypeName()).build()
				);
				return list;
			});

			//삭제할
			final int deleteSizeTypeOrder = modifySizeTypeRequestDTOList.stream().filter(o -> CUDRequestCommand.DELETE.equals(o.cudRequestCommand()))
					.map(ModifySizeTypeRequestDTO::sizeTypeOrder).findFirst().orElse(10);
			//size 조회
			//기존 데이터 중 UPDATE, DELETE 처리
			modifySizeTypeRequestDTOList.addAll(
					//삭제할 sizeTypeOrder로 CREATE
					List.of(
							ModifySizeTypeRequestDTO.builder().cudRequestCommand(CUDRequestCommand.CREATE).sizeTypeOrder(deleteSizeTypeOrder).sizeTypeName("240").build(),
							ModifySizeTypeRequestDTO.builder().cudRequestCommand(CUDRequestCommand.CREATE).sizeTypeOrder(4).sizeTypeName("245").build()
					)
			);

			ModifySizeRequestDTO modifySizeRequestDTO = ModifySizeRequestDTO.builder()
					.sizeId(responseDTO.sizeId())
					.sizeName(sizeName.repeat(2))
					.categoryId(categoryId)
					.sizeTypeList(modifySizeTypeRequestDTOList)
					.build();

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/size")
					.content(objectMapper.writeValueAsString(modifySizeRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.successResultActions(actions)
					.andExpect(jsonPath("$.data.sizeId").value(responseDTO.sizeId()))
					.andExpect(jsonPath("$.data.sizeName").value(sizeName.repeat(2)))
					.andExpect(jsonPath("$.data.sizeTypeList.length()").value(4));
		}

		@Test
		@DisplayName("사이즈 데이터가 존재하지 않는 요청으로 사이즈 편집을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void modifySize_Fail_NotFoundSize() throws Exception {

			//given
			//Size 등록
			int notFoundSizeId = 0;
			ModifySizeRequestDTO modifySizeRequestDTO = getModifySizeRequestDTO(notFoundSizeId);

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/size")
					.content(objectMapper.writeValueAsString(modifySizeRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof SizeNotFoundException));
		}

		@Test
		@DisplayName("편집할 사이즈 종류가 존재하지 않는 요청으로 사이즈 편집을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void modifySize_Fail_NotFoundModifySizeType() throws Exception {

			//given
			//Size 등록
			CreateSizeResponseDTO responseDTO = createSizeFacade.createSize(createSizeRequestDTO);

			int notFoundSizeTypeId = 0;
			List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList = List.of(
					ModifySizeTypeRequestDTO.builder().sizeTypeName("240").sizeTypeOrder(1).sizeTypeId(notFoundSizeTypeId).cudRequestCommand(CUDRequestCommand.UPDATE).build()
			);

			ModifySizeRequestDTO modifySizeRequestDTO = getModifySizeRequestDTO(responseDTO.sizeId(), modifySizeTypeRequestDTOList);

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/size")
					.content(objectMapper.writeValueAsString(modifySizeRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof SizeTypeNotFoundException));
		}

		@Test
		@DisplayName("중복된 사이즈 종류 순번으로 사이즈 편집을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void modifySize_Fail_DuplicateSizeTypeOrder() throws Exception {

			//given
			//Size 등록
			CreateSizeResponseDTO responseDTO = createSizeFacade.createSize(createSizeRequestDTO);

			List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList = List.of(
					ModifySizeTypeRequestDTO.builder().sizeTypeName("240").sizeTypeOrder(1).cudRequestCommand(CUDRequestCommand.CREATE).build(),
					ModifySizeTypeRequestDTO.builder().sizeTypeName("245").sizeTypeOrder(1).cudRequestCommand(CUDRequestCommand.CREATE).build()
			);

			ModifySizeRequestDTO modifySizeRequestDTO = getModifySizeRequestDTO(responseDTO.sizeId(), modifySizeTypeRequestDTOList);

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/size")
					.content(objectMapper.writeValueAsString(modifySizeRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateSizeTypeOrderException));
		}

		@Test
		@DisplayName("이미 등록된 사이즈 종류 순번으로 사이즈 편집을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void modifySize_Fail_ExistDuplicateSizeTypeOrder() throws Exception {

			//given
			//Size 등록
			CreateSizeResponseDTO responseDTO = createSizeFacade.createSize(createSizeRequestDTO);

			int sizeTypeOrder = sizeQueryService.getSize(responseDTO.sizeId()).getSizeTypeList().get(0).getSizeTypeOrder();

			List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList = List.of(
					ModifySizeTypeRequestDTO.builder().sizeTypeName("240").sizeTypeOrder(sizeTypeOrder).cudRequestCommand(CUDRequestCommand.CREATE).build()
			);

			ModifySizeRequestDTO modifySizeRequestDTO = getModifySizeRequestDTO(responseDTO.sizeId(), modifySizeTypeRequestDTOList);

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/size")
					.content(objectMapper.writeValueAsString(modifySizeRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateSizeTypeOrderException));
		}

		@ParameterizedTest
		@DisplayName("파라미터 유효성 조건에 일치하지 않는 요청으로 사이즈 편집을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		@MethodSource("provideModifySizeParameter")
		void modifySize_Fail_ValidParameter(Integer sizeId, String sizeName, Integer categoryId, List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList) throws Exception {

			//given
			ModifySizeRequestDTO modifySizeRequestDTO = ModifySizeRequestDTO.builder()
					.sizeId(sizeId)
					.sizeName(sizeName)
					.categoryId(categoryId)
					.sizeTypeList(modifySizeTypeRequestDTOList)
					.build();

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/size")
					.content(objectMapper.writeValueAsString(modifySizeRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
		}

		private static Stream<Arguments> provideModifySizeParameter() {
			return Stream.of(
					Arguments.of(null, sizeName, 1, modifySizeTypeList),
					Arguments.of(1, "", 1, modifySizeTypeList),
					Arguments.of(1, null, 1, modifySizeTypeList),
					Arguments.of(1, sizeName, null, modifySizeTypeList),
					Arguments.of(1, sizeName, 1, null),
					Arguments.of(1, sizeName, 1, List.of()),
					Arguments.of(1, sizeName, 1, List.of(ModifySizeTypeRequestDTO.builder().build()))
			);
		}

		private ModifySizeRequestDTO getModifySizeRequestDTO(Integer sizeId) {

			List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList = List.of(
					ModifySizeTypeRequestDTO.builder().sizeTypeName("240").sizeTypeOrder(1).cudRequestCommand(CUDRequestCommand.CREATE).build(),
					ModifySizeTypeRequestDTO.builder().sizeTypeName("245").sizeTypeOrder(2).cudRequestCommand(CUDRequestCommand.CREATE).build(),
					ModifySizeTypeRequestDTO.builder().sizeTypeName("250").sizeTypeOrder(3).cudRequestCommand(CUDRequestCommand.CREATE).build()
			);

			return ModifySizeRequestDTO.builder()
					.sizeId(sizeId)
					.sizeName(sizeName.repeat(2))
					.categoryId(categoryId)
					.sizeTypeList(modifySizeTypeRequestDTOList)
					.build();
		}

		private ModifySizeRequestDTO getModifySizeRequestDTO(Integer sizeId, List<ModifySizeTypeRequestDTO> modifySizeTypeRequestDTOList) {

			return ModifySizeRequestDTO.builder()
					.sizeId(sizeId)
					.sizeName(sizeName.repeat(2))
					.categoryId(categoryId)
					.sizeTypeList(modifySizeTypeRequestDTOList)
					.build();
		}
	}

	@Nested
	@DisplayName("사이즈 조회 Paging")
	class getSizePagingList{

		@ParameterizedTest
		@DisplayName("정상적인 요청 상황에서 사이즈 조회 Paging을 호출하면 성공한다.")
		@Transactional
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		@MethodSource("providePagingParameter")
		void getSizePagingList_Success(Integer size, Integer page, String searchSizeName, Integer listSize) throws Exception {

			//given
			int createRow = 20;

			for(int i = 0; i < createRow; i++) {
				CreateSizeRequestDTO createSizeRequestDTO = CreateSizeRequestDTO.builder()
						.sizeName("%s-%d".formatted(sizeName, i))
						.categoryId(categoryId)
						.sizeTypeList(createSizeTypeList)
						.build();
				createSizeFacade.createSize(createSizeRequestDTO);
			}

			//when
			ResultActions actions = mockMvc.perform(get("/v1/size/paging")
					.param("size", size.toString())
					.param("page", page.toString())
					.param("sizeName", searchSizeName)
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
					Arguments.of(20, 0, StringUtils.EMPTY, 20),
					Arguments.of(5, 0, StringUtils.EMPTY, 5),
					Arguments.of(20, 0, sizeName + "-9", 1),
					Arguments.of(3, 6, StringUtils.EMPTY, 2)
			);
		}
	}
}