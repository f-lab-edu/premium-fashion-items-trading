package com.inturn.pfit.domain.item.repository;


import com.inturn.pfit.domain.item.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long>, ItemRepositoryDsl {

}
