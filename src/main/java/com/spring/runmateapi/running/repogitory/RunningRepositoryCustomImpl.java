package com.spring.runmateapi.running.repogitory;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.running.entity.QRunning;
import com.spring.runmateapi.running.entity.Running;
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

        List<Running> content = queryFactory
                .selectFrom(running)
                .where(builder)
                .orderBy("oldest".equals(pageRequestDTO.getSort())
                    ? running.createdAt.asc()
                    : running.createdAt.desc())
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
