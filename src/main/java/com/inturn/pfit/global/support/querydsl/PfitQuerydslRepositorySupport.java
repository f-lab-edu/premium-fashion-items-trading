package com.inturn.pfit.global.support.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class PfitQuerydslRepositorySupport<C extends EntityPathBase> extends QuerydslRepositorySupport {

	public PfitQuerydslRepositorySupport(Class<?> domainClass) {
		super(domainClass);
	}

	public <T extends SimpleExpression> BooleanExpression eq(T path, Object param){
		return ObjectUtils.isEmpty(param) ? null : path.eq(param);
	}

	public BooleanExpression like(StringPath path, String param){
		return ObjectUtils.isEmpty(param) ? null : path.contains(param);
	}

	public <T> Page<T> pagingList(JPQLQuery<T> query, Pageable pageable) {
		return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
	}

	public OrderSpecifier[] getOrderSpecifiers(C clazz, Sort sort) {
		if(ObjectUtils.isEmpty(sort))   return new OrderSpecifier[]{};
		List<OrderSpecifier> orders = new ArrayList<>();
		PathBuilder orderByExpression = new PathBuilder(clazz.getClass(), clazz.toString(), PathBuilderValidator.FIELDS);
		sort.stream().forEach(order -> {
			Order direction = order.isAscending() ? Order.ASC : Order.DESC;
			try {
				orders.add(new OrderSpecifier(direction, orderByExpression.get(order.getProperty())));
			}
			catch (Exception e) {
				log.error("PfitQuerydslRepositorySupport getOrderSpecifiers exception = %s".formatted(e.toString()));
			}
		});
		return orders.toArray(OrderSpecifier[]::new);
	}
}
