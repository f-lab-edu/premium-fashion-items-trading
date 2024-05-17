package com.inturn.pfit.domain.sizetype.repository;

import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeTypeRepository extends JpaRepository<SizeTypeEntity, Integer> {

	List<SizeTypeEntity> findAllBySizeId(Integer sizeId);
}
