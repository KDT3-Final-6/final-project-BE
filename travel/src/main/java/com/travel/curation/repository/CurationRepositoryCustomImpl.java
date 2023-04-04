package com.travel.curation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class CurationRepositoryCustomImpl implements CurationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public CurationRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


}
