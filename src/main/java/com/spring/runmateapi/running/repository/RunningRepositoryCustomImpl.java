package com.spring.runmateapi.running.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.running.entity.QRunning;
import com.spring.runmateapi.running.entity.Running;
import com.spring.runmateapi.runningLike.entity.QRunningLike;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class RunningRepositoryCustomImpl implements RunningRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Running> searchList(PageRequestDTO pageRequestDTO, Pageable pageable) {
        QRunning running = QRunning.running;
        QRunningLike runningLike = QRunningLike.runningLike;

        BooleanBuilder builder = new BooleanBuilder();

        if(pageRequestDTO.getLocation() != null && !pageRequestDTO.getLocation().isBlank()) {
            builder.and(running.location.contains(pageRequestDTO.getLocation()));
        }
        if(pageRequestDTO.getRunDate() != null) {
            builder.and(running.runDate.eq(pageRequestDTO.getRunDate()));
        }
        if(pageRequestDTO.getMaxDistance() != null) {
            builder.and(running.distance.loe(pageRequestDTO.getMaxDistance()));
        }
        //좋아요 수 서브쿼리
        NumberExpression<Long> likeCountExpr = Expressions.numberTemplate(Long.class, "({0})",
                JPAExpressions
                .select(runningLike.count())
                .from(runningLike)
                .where(runningLike.running.eq(running))
        );
        OrderSpecifier<?> orderSpecifier;
        if("popular".equals(pageRequestDTO.getSort())) {
            orderSpecifier = likeCountExpr.desc(); // 좋아요 많은순
        } else if ("oldest".equals(pageRequestDTO.getSort())) {
            orderSpecifier = running.createdAt.asc();
        } else {
            orderSpecifier = running.createdAt.desc(); // 기본 최신순
        }

        List<Running> content = queryFactory
                .selectFrom(running)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(running.count())
                .from(running)
                .where(builder)
                .fetchOne();
        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
