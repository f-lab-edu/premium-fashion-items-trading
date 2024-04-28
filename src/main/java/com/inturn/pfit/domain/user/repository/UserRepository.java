package com.inturn.pfit.domain.user.repository;

import com.inturn.pfit.domain.user.entity.User;
import com.inturn.pfit.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	UserEntity findByEmail(String email);
}
