package com.inturn.pfit.domain.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inturn.pfit.domain.brand.dto.request.CreateBrandRequestDTO;
import com.inturn.pfit.domain.brand.dto.response.CreateBrandResponseDTO;
import com.inturn.pfit.domain.brand.exception.BrandNotFoundException;
import com.inturn.pfit.domain.brand.repository.BrandRepository;
import com.inturn.pfit.domain.brand.service.BrandCommandService;
import com.inturn.pfit.domain.category.dto.request.CreateCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CreateCategoryResponseDTO;
import com.inturn.pfit.domain.category.exception.CategoryNotFoundException;
import com.inturn.pfit.domain.category.repository.CategoryRepository;
import com.inturn.pfit.domain.category.service.CategoryCommandService;
import com.inturn.pfit.domain.item.dto.request.CreateItemRequestDTO;
import com.inturn.pfit.domain.item.dto.request.ModifyItemRequestDTO;
import com.inturn.pfit.domain.item.dto.response.CreateItemResponseDTO;
import com.inturn.pfit.domain.item.exception.ItemNotFoundException;
import com.inturn.pfit.domain.item.facade.CreateItemFacade;
import com.inturn.pfit.domain.item.repository.ItemRepository;
import com.inturn.pfit.domain.item.service.ItemQueryService;
import com.inturn.pfit.domain.itemsize.dto.request.CreateItemSizeRequestDTO;
import com.inturn.pfit.domain.itemsize.dto.request.ModifyItemSizeRequestDTO;
import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import com.inturn.pfit.domain.itemsize.exception.DuplicateItemSizeOrderException;
import com.inturn.pfit.domain.itemsize.exception.ItemSizeNotFoundException;
import com.inturn.pfit.domain.itemsize.repository.ItemSizeRepository;
import com.inturn.pfit.global.common.vo.CUDRequestCommand;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import com.inturn.pfit.global.support.utils.PfitConsts;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ItemIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CategoryCommandService categoryCommandService;

	@Autowired
	private BrandCommandService brandCommandService;

	@Autowired
	private ItemQueryService itemQueryService;

	@Autowired
	private CreateItemFacade createItemFacade;

	@Autowired
	private ItemSizeRepository itemSizeRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private TransactionTemplate transactionTemplate;


	private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	static Integer categoryId;

	static Integer brandId;

	static String itemName = "나이키 에어포스 White";

	static List<CreateItemSizeRequestDTO> createItemSizeList = List.of(
			CreateItemSizeRequestDTO.builder().itemSizeName("275").itemSizeOrder(1).build(),
			CreateItemSizeRequestDTO.builder().itemSizeName("280").itemSizeOrder(2).build(),
			CreateItemSizeRequestDTO.builder().itemSizeName("285").itemSizeOrder(3).build()
	);

	static List<ModifyItemSizeRequestDTO> modifyItemSizeList = List.of(
			ModifyItemSizeRequestDTO.builder().itemSizeName("275").itemSizeOrder(1).cudRequestCommand(CUDRequestCommand.CREATE).build(),
			ModifyItemSizeRequestDTO.builder().itemSizeName("280").itemSizeOrder(2).cudRequestCommand(CUDRequestCommand.CREATE).build(),
			ModifyItemSizeRequestDTO.builder().itemSizeName("285").itemSizeOrder(3).cudRequestCommand(CUDRequestCommand.CREATE).build()
	);

	CreateItemRequestDTO createItemRequestDTO;


	//상위 데이터를 넣어놓는 방법 찾아보자.
	@BeforeEach
	void testStart() {

		//상위 테이블인 카테고리를 먼저 등록
		CreateCategoryResponseDTO categoryRes = categoryCommandService.createCategory(CreateCategoryRequestDTO.builder()
				.categoryName("신발")
				.categoryOrder(1)
				.build()
		);
		categoryId = categoryRes.categoryId();

		CreateBrandResponseDTO brandRes =  brandCommandService.createBrand(CreateBrandRequestDTO.builder()
				 .brandName("NIKE")
				 .build());
		brandId = brandRes.brandId();

		//상품 등록 요청 데이터 set
		createItemRequestDTO = CreateItemRequestDTO.builder()
				.itemName(itemName)
				.modelNo("CW2288-111")
				.gender("MEN")
				.categoryId(categoryId)
				.brandId(brandId)
				.retailPrice(139000l)
				.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
				.releaseDate(LocalDate.now())
				.itemSizeList(createItemSizeList)
				.build();
	}

	@AfterEach
	void testEnd() {
		itemSizeRepository.deleteAll();
		itemRepository.deleteAll();
		brandRepository.deleteAll();
		categoryRepository.deleteAll();
	}

	@Nested
	@DisplayName("상품 등록")
	class createItem {
		@Test
		@DisplayName("정상적인 요청에서 상품 등록을 시도할 경우 성공한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void createItem_Success() throws Exception {

			//given

			//when
			ResultActions actions = mockMvc.perform(post("/v1/item")
					.content(objectMapper.writeValueAsString(createItemRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.successResultActions(actions)
					.andExpect(jsonPath("$.data.itemId").exists());
		}

		@Test
		@DisplayName("상품 사이즈 순번이 중복인 요청에서 상품 등록을 시도할 경우 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void createItem_Fail_DuplicateItemSizeOrder() throws Exception {

			//given
			List<CreateItemSizeRequestDTO> itemSizeList = List.of(
					CreateItemSizeRequestDTO.builder().itemSizeName("275").itemSizeOrder(1).build(),
					CreateItemSizeRequestDTO.builder().itemSizeName("280").itemSizeOrder(1).build()
			);

			CreateItemRequestDTO createItemRequestDTO = getCreateItemRequestDTO(itemSizeList);

			//when
			ResultActions actions = mockMvc.perform(post("/v1/item")
					.content(objectMapper.writeValueAsString(createItemRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateItemSizeOrderException));
		}

		@Test
		@DisplayName("카테고리 데이터가 존재하지 않는 카테고리 ID로 상품 등록을 시도할 경우 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void createItem_Fail_CategoryNotFound() throws Exception {

			//given
			Integer notExistCategoryId = 0;

			CreateItemRequestDTO req = CreateItemRequestDTO.builder()
					.itemName(itemName)
					.modelNo("CW2288-111")
					.gender("MEN")
					.brandId(brandId)
					.retailPrice(139000l)
					.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
					.categoryId(notExistCategoryId)
					.itemSizeList(createItemSizeList)
					.build();

			//when
			ResultActions actions = mockMvc.perform(post("/v1/item")
					.content(objectMapper.writeValueAsString(req))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryNotFoundException));
		}

		@Test
		@DisplayName("브랜드 데이터가 존재하지 않는 브랜드 ID로 상품 등록을 시도할 경우 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void createItem_Fail_BrandNotFound() throws Exception {

			//given
			Integer notExistBrandId = 0;

			CreateItemRequestDTO req = CreateItemRequestDTO.builder()
					.itemName(itemName)
					.modelNo("CW2288-111")
					.gender("MEN")
					.brandId(notExistBrandId)
					.retailPrice(139000l)
					.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
					.categoryId(categoryId)
					.itemSizeList(createItemSizeList)
					.build();

			//when
			ResultActions actions = mockMvc.perform(post("/v1/item")
					.content(objectMapper.writeValueAsString(req))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof BrandNotFoundException));
		}

		@ParameterizedTest
		@DisplayName("필수값이 존재하지 않는 상황에서 상품 등록을 호출하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		@MethodSource("provideCreateItemParameter")
		void createItem_Fail_ValidateNotNullFields(String itemName, Integer categoryId, Integer brandId, List<CreateItemSizeRequestDTO> itemSizeList) throws Exception {

			//given
			CreateItemRequestDTO req = CreateItemRequestDTO.builder()
					.itemName(itemName)
					.categoryId(categoryId)
					.brandId(brandId)
					.itemSizeList(itemSizeList)
					.build();

			//when
			ResultActions actions = mockMvc.perform(post("/v1/item")
					.content(objectMapper.writeValueAsString(req))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
		}

		private static Stream<Arguments> provideCreateItemParameter() {
			return Stream.of(
					Arguments.of(null, 1, 1, createItemSizeList),
					Arguments.of("", 1, 1, createItemSizeList),
					Arguments.of(itemName, null, 1, createItemSizeList),
					Arguments.of(itemName, 1, null, createItemSizeList),
					Arguments.of(itemName, 1, 1, null),
					Arguments.of(itemName, 1, 1, List.of()),
					Arguments.of(itemName, 1, 1, List.of(CreateItemSizeRequestDTO.builder().build()))
			);
		}

		@Test
		@DisplayName("인증 정보가 없는 상황에서 브랜드 등록을 시도하면 실패한다.")
		void createItem_Fail_Unauthorized() throws Exception {
			//given

			//when
			ResultActions actions = mockMvc.perform(post("/v1/item")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createItemRequestDTO)));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isUnauthorized());
		}

		@Test
		@DisplayName("권한이 없는 상황에서 상품 등록을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_USER)
		void createItem_Fail_Forbidden() throws Exception {
			//given

			//when
			ResultActions actions = mockMvc.perform(post("/v1/item")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(createItemRequestDTO)));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isForbidden());
		}
	}

	@Nested
	@DisplayName("상품 단일 조회")
	class getItemById {
		//TODO - 이 부분은 확인.. 왜? 지연 로딩이 안될까 ? 트랜잭션을 뺴야되네..
		@Test
		@DisplayName("상품 데이터가 존재하는 정상적인 요청인 상황에서 상품 단일 조회를 시도하면 성공한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void getItemById_Success() throws Exception {

			//given
			//상품 등록
			CreateItemResponseDTO res = createItemFacade.createItem(createItemRequestDTO);
			//when
			ResultActions actions = mockMvc.perform(get("/v1/item/%d".formatted(res.itemId())));

			//then
			CommonResponseResultFixture.successResultActions(actions)
					.andExpect(jsonPath("$.data.itemId").value(res.itemId()))
					.andExpect(jsonPath("$.data.itemSizeList.length()").value(createItemSizeList.size()));
		}

		@Test
		@DisplayName("상품 데이터가 존재하지 않는 상황에서 상품 단일 조회를 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void getItemById_Fail_ItemNotFound() throws Exception {

			//given

			//when
			Integer notFoundItemId = 0;
			ResultActions actions = mockMvc.perform(get("/v1/item/%d".formatted(notFoundItemId)));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof ItemNotFoundException));
		}

		@Test
		@DisplayName("상품 데이터가 존재하지 않는 상황에서 상품 단일 조회를 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void getItemById_Fail_ValidNullSizeId() throws Exception {

			//given

			//when
			ResultActions actions = mockMvc.perform(get("/v1/item"));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isInternalServerError());
		}
	}

	@Nested
	@DisplayName("상품 편집")
	class modifyItem{

		@Test
		@DisplayName("정상적인 요청으로 상품 편집을 시도하면 성공한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void modifyItem_Success() throws Exception {

			//given
			//품목 등록
			CreateItemResponseDTO responseDTO = createItemFacade.createItem(createItemRequestDTO);

			final int updateIdx = 0;
			final int deleteIdx = 1;
			//등록된 데이터를 조회하여 UPDATE / DELETE 항목 결정
			final List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList = transactionTemplate.execute(status -> {
				List<ItemSizeEntity> itemSizeEntityList = itemQueryService.getItemById(responseDTO.itemId()).getItemSizeList();
				List<ModifyItemSizeRequestDTO> list = new ArrayList<>();
				//UPDATE할 항목 선정
				list.add(
						ModifyItemSizeRequestDTO.builder().cudRequestCommand(CUDRequestCommand.UPDATE).itemSizeOrder(itemSizeEntityList.get(updateIdx).getItemSizeOrder())
								.itemSizeId(itemSizeEntityList.get(updateIdx).getItemSizeId())
								.itemSizeName(itemSizeEntityList.get(updateIdx).getItemSizeName().repeat(2)).build()
				);
				list.add(
						ModifyItemSizeRequestDTO.builder().cudRequestCommand(CUDRequestCommand.DELETE).itemSizeId(itemSizeEntityList.get(deleteIdx).getItemSizeId())
								.itemSizeOrder(itemSizeEntityList.get(deleteIdx).getItemSizeOrder())
								.itemSizeName(itemSizeEntityList.get(deleteIdx).getItemSizeName()).build()
				);
				return list;
			});

			final int deleteItemSizeOrder = modifyItemSizeRequestDTOList.stream().filter(o -> CUDRequestCommand.DELETE.equals(o.cudRequestCommand()))
					.map(ModifyItemSizeRequestDTO::itemSizeOrder).findFirst().orElse(10);

			//기존 데이터 중 UPDATE, DELETE 처리
			modifyItemSizeRequestDTOList.addAll(
					//삭제할 itemSizeOrder로 CREATE
					List.of(
							ModifyItemSizeRequestDTO.builder().cudRequestCommand(CUDRequestCommand.CREATE).itemSizeOrder(deleteItemSizeOrder).itemSizeName("240").build(),
							ModifyItemSizeRequestDTO.builder().cudRequestCommand(CUDRequestCommand.CREATE).itemSizeOrder(4).itemSizeName("245").build()
					)
			);

			ModifyItemRequestDTO modifyItemRequestDTO = getModifyItemRequestDTO(responseDTO.itemId(), modifyItemSizeRequestDTOList);

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/item")
					.content(objectMapper.writeValueAsString(modifyItemRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.successResultActions(actions)
					.andExpect(jsonPath("$.data.itemId").value(responseDTO.itemId()))
					.andExpect(jsonPath("$.data.itemName").value(itemName.repeat(2)))
					.andExpect(jsonPath("$.data.itemSizeList.length()").value(4));
		}

		@Test
		@DisplayName("상품 데이터가 존재하지 않는 요청으로 상품 편집을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void modifyItem_Fail_NotFoundSize() throws Exception {

			//given
			long notFoundItemId = 0;
			ModifyItemRequestDTO modifyItemRequestDTO = getModifyItemRequestDTO(notFoundItemId);

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/item")
					.content(objectMapper.writeValueAsString(modifyItemRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof ItemNotFoundException));
		}

		@Test
		@DisplayName("편집할 상품 사이즈가 존재하지 않는 요청으로 상품 편집을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void modifyItem_Fail_NotFoundModifyItemSize() throws Exception {

			//given
			//상품 등록
			CreateItemResponseDTO responseDTO = createItemFacade.createItem(createItemRequestDTO);

			long notFoundItemSizeId = 0;
			List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList = List.of(
					ModifyItemSizeRequestDTO.builder().itemSizeName("240").itemSizeOrder(1).itemSizeId(notFoundItemSizeId).cudRequestCommand(CUDRequestCommand.UPDATE).build()
			);

			ModifyItemRequestDTO modifyItemRequestDTO = getModifyItemRequestDTO(responseDTO.itemId(), modifyItemSizeRequestDTOList);

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/item")
					.content(objectMapper.writeValueAsString(modifyItemRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof ItemSizeNotFoundException));
		}

		@Test
		@DisplayName("중복된 상품 사이즈 순번으로 상품 편집을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void modifyItem_Fail_DuplicateItemSizeOrder() throws Exception {

			//given
			//상품 등록
			CreateItemResponseDTO responseDTO = createItemFacade.createItem(createItemRequestDTO);

			List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList = List.of(
					ModifyItemSizeRequestDTO.builder().itemSizeName("240").itemSizeOrder(1).cudRequestCommand(CUDRequestCommand.CREATE).build(),
					ModifyItemSizeRequestDTO.builder().itemSizeName("245").itemSizeOrder(1).cudRequestCommand(CUDRequestCommand.CREATE).build()
			);

			ModifyItemRequestDTO modifyItemRequestDTO = getModifyItemRequestDTO(responseDTO.itemId(), modifyItemSizeRequestDTOList);

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/item")
					.content(objectMapper.writeValueAsString(modifyItemRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateItemSizeOrderException));
		}

		@Test
		@DisplayName("이미 등록된 상품 사이즈 순번으로 상품 편집을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		void modifyItem_Fail_ExistDuplicateItemSizeOrder() throws Exception {

			//given
			//상품 등록
			CreateItemResponseDTO responseDTO = createItemFacade.createItem(createItemRequestDTO);

			int itemSizeOrder = itemQueryService.getItem(responseDTO.itemId()).getItemSizeList().get(0).getItemSizeOrder();

			List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList = List.of(
					ModifyItemSizeRequestDTO.builder().itemSizeName("240").itemSizeOrder(itemSizeOrder).cudRequestCommand(CUDRequestCommand.CREATE).build()
			);

			ModifyItemRequestDTO modifyItemRequestDTO = getModifyItemRequestDTO(responseDTO.itemId(), modifyItemSizeRequestDTOList);

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/item")
					.content(objectMapper.writeValueAsString(modifyItemRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateItemSizeOrderException));
		}

		@ParameterizedTest
		@DisplayName("파라미터 유효성 조건에 일치하지 않는 요청으로 상품 편집을 시도하면 실패한다.")
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		@MethodSource("provideModifyItemParameter")
		void modifyItem_Fail_ValidParameter(Long itemId, String itemName, Integer categoryId, Integer brandId, List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList) throws Exception {

			//given
			ModifyItemRequestDTO modifyItemRequestDTO = ModifyItemRequestDTO.builder()
					.itemId(itemId)
					.itemName(itemName)
					.categoryId(categoryId)
					.brandId(brandId)
					.itemSizeList(modifyItemSizeRequestDTOList)
					.build();

			//when
			ResultActions actions = mockMvc.perform(patch("/v1/item")
					.content(objectMapper.writeValueAsString(modifyItemRequestDTO))
					.contentType(MediaType.APPLICATION_JSON));

			//then
			CommonResponseResultFixture.failResultActions(actions, status().isBadRequest())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
		}

		private static Stream<Arguments> provideModifyItemParameter() {
			return Stream.of(
					Arguments.of(null, itemName, 1, 1, modifyItemSizeList),
					Arguments.of(1l, "", 1, 1, modifyItemSizeList),
					Arguments.of(1l, null, 1, 1, modifyItemSizeList),
					Arguments.of(1l, itemName, null, 1, modifyItemSizeList),
					Arguments.of(1l, itemName, 1, null, modifyItemSizeList),
					Arguments.of(1l, itemName, 1, 1, null),
					Arguments.of(1l, itemName, 1, 1, List.of()),
					Arguments.of(1l, itemName, 1, 1, List.of(ModifyItemSizeRequestDTO.builder().build()))
			);
		}

		private ModifyItemRequestDTO getModifyItemRequestDTO(Long itemId) {

			List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList = List.of(
					ModifyItemSizeRequestDTO.builder().itemSizeName("240").itemSizeOrder(1).cudRequestCommand(CUDRequestCommand.CREATE).build(),
					ModifyItemSizeRequestDTO.builder().itemSizeName("245").itemSizeOrder(2).cudRequestCommand(CUDRequestCommand.CREATE).build(),
					ModifyItemSizeRequestDTO.builder().itemSizeName("250").itemSizeOrder(3).cudRequestCommand(CUDRequestCommand.CREATE).build()
			);

			return ModifyItemRequestDTO.builder()
					.itemId(itemId)
					.modelNo("CW2288-111")
					.gender("MEN")
					.brandId(brandId)
					.retailPrice(139000l)
					.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
					.releaseDate(LocalDate.now())
					.itemName(itemName.repeat(2))
					.categoryId(categoryId)
					.itemSizeList(modifyItemSizeRequestDTOList)
					.build();
		}

		private ModifyItemRequestDTO getModifyItemRequestDTO(Long itemId, List<ModifyItemSizeRequestDTO> modifyItemSizeRequestDTOList) {

			return ModifyItemRequestDTO.builder()
					.itemId(itemId)
					.modelNo("CW2288-111")
					.gender("MEN")
					.brandId(brandId)
					.retailPrice(139000l)
					.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
					.releaseDate(LocalDate.now())
					.itemName(itemName.repeat(2))
					.categoryId(categoryId)
					.itemSizeList(modifyItemSizeRequestDTOList)
					.build();
		}
	}

	@Nested
	@DisplayName("상품 조회 Paging")
	class getItemPagingList{

		@ParameterizedTest
		@DisplayName("정상적인 요청 상황에서 상품 조회 Paging을 호출하면 성공한다.")
		@Transactional
		@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
		@MethodSource("providePagingParameter")
		void getItemPagingList_Success(Integer size, Integer page, String searchItemName, Integer listSize) throws Exception {

			//given
			int createRow = 20;

			for(int i = 0; i < createRow; i++) {
				CreateItemRequestDTO createItemRequestDTO = CreateItemRequestDTO.builder()
						.itemName("%s-%d".formatted(itemName, i))
						.modelNo("CW2288-111")
						.gender("MEN")
						.retailPrice(139000l)
						.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
						.releaseDate(LocalDate.now())
						.categoryId(categoryId)
						.brandId(brandId)
						.itemSizeList(createItemSizeList)
						.build();
				createItemFacade.createItem(createItemRequestDTO);
			}

			//when
			ResultActions actions = mockMvc.perform(get("/v1/item/paging")
					.param("size", size.toString())
					.param("page", page.toString())
					.param("itemName", searchItemName)
			);

			//then
			actions
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.success").value(true))
					.andExpect(jsonPath("$.data.content.length()").value(listSize))
					.andDo(print());
		}

		//TODO - 파라미터 조건을 로직에서 어떻게 명확히 구분하게 할 수 있을까?
		private static Stream<Arguments> providePagingParameter() {
			return Stream.of(
					Arguments.of(20, 0, StringUtils.EMPTY, 20),
					Arguments.of(5, 0, StringUtils.EMPTY, 5),
					Arguments.of(20, 0, itemName + "-9", 1),
					Arguments.of(3, 6, StringUtils.EMPTY, 2)
			);
		}
	}

	private CreateItemRequestDTO getCreateItemRequestDTO(List<CreateItemSizeRequestDTO> createItemSizeList) {
		return CreateItemRequestDTO.builder()
				.itemName(itemName)
				.modelNo("CW2288-111")
				.gender("MEN")
				.categoryId(categoryId)
				.brandId(brandId)
				.retailPrice(139000l)
				.displayYn(PfitConsts.CommonCodeConsts.YN_Y)
				.releaseDate(LocalDate.now())
				.itemSizeList(createItemSizeList)
				.build();
	}

}