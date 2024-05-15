package com.inturn.pfit.domain.size.repository;


import com.inturn.pfit.domain.size.entity.SizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<SizeEntity, Integer>, SizeRepositoryDsl {

}
