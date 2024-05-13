package com.inturn.pfit.domain.brand.repository;

import com.inturn.pfit.domain.brand.entity.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("junit")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BrandRepositoryTest {

	@Autowired
	private BrandRepository brandRepository;

	Brand brand;

	@BeforeEach
	void testStart() {
		brand = Brand.builder()
				.brandName("Nike")
				.build();
	}

	@Test
	@DisplayName("브랜드 데이터가 정상적인 상황에서 브랜드 저장을 시도하면 성공한다.")
	@Transactional
	void save_Success() {

		//given

		//when
		Brand result = brandRepository.save(brand);

		//then
		assertNotNull(result.getBrandId());
		assertEquals(result.getBrandName(), brand.getBrandName());
	}
}