package com.inturn.pfit.global.common.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@NoArgsConstructor
@SuperBuilder
public class BaseTimeEntity {

	private LocalDateTime createdDt;

	private LocalDateTime updatedDt;

	@PrePersist
	private void prePersist() {
		this.createdDt = LocalDateTime.now();
	}

	@PreUpdate
	private void preUpdate() {
		this.updatedDt = LocalDateTime.now();
	}
}
