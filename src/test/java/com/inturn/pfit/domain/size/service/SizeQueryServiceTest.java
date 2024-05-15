package com.inturn.pfit.domain.size.service;

import com.inturn.pfit.domain.category.dto.request.CreateCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CreateCategoryResponseDTO;
import com.inturn.pfit.domain.category.service.CategoryCommandService;
import com.inturn.pfit.domain.size.dto.request.CreateSizeRequestDTO;
import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.size.facade.CreateSizeFacade;
import com.inturn.pfit.domain.size.repository.SizeRepository;
import com.inturn.pfit.domain.sizetype.dto.request.CreateSizeTypeRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("junit")
class SizeQueryServiceTest {

	@Autowired
	private CategoryCommandService categoryCommandService;

	@Autowired
	private CreateSizeFacade createSizeFacade;
	CreateSizeRequestDTO createSizeRequestDTO;

	@Autowired
	private SizeQueryService sizeQueryService;

	@Autowired
	private SizeRepository sizeRepository;




	static Integer categoryId;

	static String sizeName = "한국 신발 사이즈";

	static List<CreateSizeTypeRequestDTO> sizeTypeList = List.of(
			CreateSizeTypeRequestDTO.builder().sizeTypeName("275").sizeTypeOrder(1).build(),
			CreateSizeTypeRequestDTO.builder().sizeTypeName("280").sizeTypeOrder(2).build(),
			CreateSizeTypeRequestDTO.builder().sizeTypeName("285").sizeTypeOrder(3).build()
	);


	@Autowired
	private TransactionTemplate transactionTemplate;

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
	@Transactional
	void test() {

		;
		var c = createSizeFacade.createSize(createSizeRequestDTO);
		Optional<SizeEntity> res = transactionTemplate.execute(status -> sizeRepository.findById(c.sizeId()));
		System.out.println("1");
	}

}