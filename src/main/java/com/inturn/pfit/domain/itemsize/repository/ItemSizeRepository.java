package com.inturn.pfit.domain.itemsize.repository;


import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemSizeRepository extends JpaRepository<ItemSizeEntity, Long> {

	List<ItemSizeEntity> findAllByItemId(Long itemId);
}
