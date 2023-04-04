package com.travel.curation.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.product.entity.Product;
import com.travel.survey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.travel.product.entity.QPeriodOption.periodOption;
import static com.travel.product.entity.QProduct.product;
import static com.travel.product.entity.QProductCategory.productCategory;

public class CurationRepositoryCustomImpl implements CurationRepositoryCustom {

    private static final LocalDate spring = LocalDate.of(2024, 3, 01);
    private static final LocalDate summer = LocalDate.of(2023, 6, 01);
    private static final LocalDate autumn = LocalDate.of(2023, 9, 01);
    private static final LocalDate winter = LocalDate.of(2024, 12, 01);
    private final JPAQueryFactory queryFactory;

    public CurationRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<Product> findAllWithTarget(Pageable pageable, Survey survey, String target) {
        List<Product> content =
                queryFactory.selectFrom(product)
                        .join(product.productCategories, productCategory)
                        .where(curationByTarget(target, survey))
                        .groupBy(product.productId)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(product.wishlistCount.desc())
                        .fetch();

        long total =
                queryFactory.select(Wildcard.count)
                        .from(product)
                        .join(product.productCategories, productCategory)
                        .where(curationByTarget(target, survey))
                        .groupBy(product.productId)
                        .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Product> findAllWithSeason(Pageable pageable, Survey survey) {
        List<Product> content =
                queryFactory.selectFrom(product)
                        .join(product.periodOptions, periodOption)
                        .where(curationBySeason(survey))
                        .groupBy(product.productId)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(product.wishlistCount.desc())
                        .fetch();

        long total =
                queryFactory.select(Wildcard.count)
                        .from(product)
                        .join(product.periodOptions, periodOption)
                        .where(curationBySeason(survey))
                        .groupBy(product.productId)
                        .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression curationByTarget(String target, Survey survey) {
        switch (target) {
            case "companion":
                return byCompanion(survey);

            case "gender":
                return byGender(survey);

            case "ageGroup":
                return byAgeGroup(survey);

            case "religion":
                return byReligion(survey);

            case "hobby":
                return byHobby(survey);
        }
        return null;
    }

    private BooleanExpression byCompanion(Survey survey) {
        switch (survey.getSurveyCompanion().name()) {
            case "COUPLE":
            case "FAMILY":
                return productCategory.category.categoryName.eq("가족끼리");

            case "FRIEND":
            case "CLUB":
            case "COWORKER":
                return productCategory.category.categoryName.eq("5070끼리")
                        .or(productCategory.category.categoryName.eq("남자끼리"))
                        .or(productCategory.category.categoryName.eq("여자끼리"));

            case "ETC":
                return productCategory.category.categoryName.eq("누구든지");
        }
        return null;
    }

    private BooleanExpression byGender(Survey survey) {
        switch (survey.getSurveyGender().name()) {
            case "MALE":
                return productCategory.category.categoryName.eq("남자끼리");

            case "FEMALE":
                return productCategory.category.categoryName.eq("여자끼리");
        }
        return null;
    }

    private BooleanExpression byAgeGroup(Survey survey) {
        switch (survey.getSurveyGroup().name()) {
            case "TWENTHIRTY":
                return productCategory.category.categoryName.eq("남자끼리")
                        .or(productCategory.category.categoryName.eq("여자끼리"));

            case "FOURFIFTY":
                return productCategory.category.categoryName.eq("가족끼리");

            case "SIXSEVNTY":
                return productCategory.category.categoryName.eq("5070끼리");

            case "ETC":
                return productCategory.category.categoryName.eq("누구든지");
        }
        return null;
    }

    private BooleanExpression byReligion(Survey survey) {
        switch (survey.getSurveyReligion().name()) {
            case "CHRISTIAN":
                return productCategory.category.categoryName.eq("기독교");

            case "BUDDHISM":
                return productCategory.category.categoryName.eq("불교");

            case "ETC":
            case "ATHEISM":
                return null;
        }
        return null;
    }

    private BooleanExpression byHobby(Survey survey) {
        switch (survey.getSurveyHobby().name()) {
            case "SHOPPING":
                return productCategory.category.categoryName.eq("쇼핑");

            case "GOLF":
                return productCategory.category.categoryName.eq("골프여행");

            case "VACATION":
                return productCategory.category.categoryName.eq("휴양지");

            case "WINE":
                return productCategory.category.categoryName.eq("와인");

            case "CULTURAL":
                return productCategory.category.categoryName.eq("문화탐방");

            case "PILGRIMAGE":
                return productCategory.category.categoryName.eq("성지순례");

            case "VOLUNTEER":
                return productCategory.category.categoryName.eq("볼론투어");

            case "TREKKING":
                return productCategory.category.categoryName.eq("여자끼리");
        }
        return null;
    }

    private BooleanExpression curationBySeason(Survey survey) {
        switch (survey.getSurveySeason().name()) {
            case "SPRING":
                return periodOption.startDate.between(spring.minusDays(1), spring.plusMonths(3));

            case "SUMMER":
                return periodOption.startDate.between(summer, autumn.minusDays(1));

            case "AUTUMN":
                return periodOption.startDate.between(autumn, winter.minusDays(1));

            case "WINTER":
                return periodOption.startDate.between(winter, spring.minusDays(1));
        }
        return null;
    }
}
