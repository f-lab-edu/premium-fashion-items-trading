package com.inturn.pfit.global.support.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class PfitQuerydslRepositorySupport<C extends EntityPathBase> extends QuerydslRepositorySupport {
	/**
	 * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
	 *
	 * @param domainClass must not be {@literal null}.
	 */
	public PfitQuerydslRepositorySupport(Class<?> domainClass) {
		super(domainClass);
	}

	public <T extends SimpleExpression> BooleanExpression eq(T path, Object param){
		return ObjectUtils.isEmpty(param) ? null : path.eq(param);
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

			}
		});
		return orders.toArray(OrderSpecifier[]::new);
	}

}
