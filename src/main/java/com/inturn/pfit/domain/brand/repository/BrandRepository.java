package com.inturn.pfit.domain.brand.repository;

import com.inturn.pfit.domain.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer>, BrandRepositoryDsl {
}
