package com.inturn.pfit.domain.category.repository;

import com.inturn.pfit.domain.category.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;

	Category category;

	@BeforeEach
	void testStart() {
		category = Category.builder()
				.categoryName("운동화")
				.categoryOrder(1)
				.build();
	}

	@Test
	@DisplayName("카테고리 저장 성공(save)")
	@Transactional
	void save_Success() {

		//given

		//when
		Category result = categoryRepository.save(category);

		//then
		assertNotNull(result.getCategoryId());
		assertEquals(result.getCategoryName(), category.getCategoryName());
		assertEquals(result.getCategoryOrder(), category.getCategoryOrder());

	}
	
	@Test
	@DisplayName("카테고리 순번으로 조회 성공(findByCategoryOrder)")
	@Transactional
	void findByCategoryOrder_Success() {

		//given

		//when
		Category result = categoryRepository.save(category);
		final Optional<Category> categoryOpt = categoryRepository.findByCategoryOrder(1);

		//then
		assertEquals(categoryOpt.isPresent(), true);
		assertEquals(categoryOpt.get().getCategoryName(), "운동화");
		assertEquals(categoryOpt.get().getCategoryOrder(), 1);
	}

	@Test
	@DisplayName("카테고리 번호로 조회 시 데이터가 이미 존재하여 실패")
	@Transactional
	void findByCategoryOrder_Fail_NotFound() {

		//given

		//when
		final Optional<Category> categoryOpt = categoryRepository.findByCategoryOrder(1);

		//then
		assertEquals(categoryOpt.isPresent(), false);
	}
}