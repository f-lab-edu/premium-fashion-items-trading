package com.inturn.pfit.global.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inturn.pfit.global.support.utils.PfitConsts;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseTimeEntity {


	//@DateTimeFormat은 스프링 프레임워크에서 날짜와 시간을 나타내는 문자열을 자바의 날짜 및 시간 객체로 변환할 떄 사용, @RequestBoady, @ResponseBody에서는 동작하지 않음.
	@DateTimeFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
	//@JsonFormat를 사용하여 @RequestBody, @ResponseBody에서 문자열을 해당 날짜 및 시간 객체로 전환
	@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
	private LocalDateTime createdAt;

	@DateTimeFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
	@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
	private LocalDateTime updatedAt;

	@PrePersist
	private void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	private void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
