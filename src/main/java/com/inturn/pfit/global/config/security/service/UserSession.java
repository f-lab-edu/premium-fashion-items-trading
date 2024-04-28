package com.inturn.pfit.global.config.security.service;

import com.inturn.pfit.domain.user.entity.UserEntity;
import lombok.Builder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

public class UserSession extends User {

	//기본 생성자 필수
	//
	public UserSession(UserEntity user) {
		super(user.getEmail(), user.getPassword(), Set.of(new SimpleGrantedAuthority(user.getRoleCode())));
	}
}
