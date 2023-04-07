package com.travel.curation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.product.entity.Product;
import com.travel.product.entity.Status;
import com.travel.survey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.travel.product.entity.QCategory.category;
import static com.travel.product.entity.QPeriodOption.periodOption;
import static com.travel.product.entity.QProduct.product;
import static com.travel.product.entity.QProductCategory.productCategory;

public class CurationRepositoryCustomImpl implements CurationRepositoryCustom {

    private static final LocalDate SPRING = LocalDate.of(2024, 3, 01);
    private static final LocalDate SUMMER = LocalDate.of(2023, 6, 01);
    private static final LocalDate AUTUMN = LocalDate.of(2023, 9, 01);
    private static final LocalDate WINTER = LocalDate.of(2024, 12, 01);
    private final JPAQueryFactory queryFactory;

    public CurationRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<Product> findAllWithThreeCategories(Pageable pageable, String season, String district, String theme) {

        LocalDate now = LocalDate.now();

        district = districtConnector(district);
        theme = themeConnector(theme);

        List<Product> content =
                queryFactory.selectFrom(product)
                        .join(product.periodOptions, periodOption)
                        .where(curationBySeason(season, now), product.productStatus.eq(Status.FORSALE), product.in(
                                JPAExpressions.selectFrom(product)
                                        .join(product.productCategories, productCategory)
                                        .join(productCategory.category, category)
                                        .where(category.categoryName.in(district, theme))
                                        .groupBy(product.productId)
                                        .having(productCategory.category.categoryId.countDistinct().eq((long) 2))
                        ))
                        .groupBy(product.productId)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(product.wishlistCount.desc())
                        .fetch();

        long total =
                queryFactory.select(Wildcard.count)
                        .from(product)
                        .join(product.periodOptions, periodOption)
                        .where(curationBySeason(season, now), product.productStatus.eq(Status.FORSALE), product.in(
                                JPAExpressions.selectFrom(product)
                                        .join(product.productCategories, productCategory)
                                        .join(productCategory.category, category)
                                        .where(category.categoryName.in(district, theme))
                                        .groupBy(product.productId)
                                        .having(productCategory.category.categoryId.countDistinct().eq((long) 2))
                        ))
                        .groupBy(product.productId)
                        .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Product> findAllWithTarget(Pageable pageable, Survey survey, String target) {
        List<Product> content =
                queryFactory.selectFrom(product)
                        .join(product.productCategories, productCategory)
                        .join(productCategory.category, category)
                        .where(curationByTarget(target, survey), product.productStatus.eq(Status.FORSALE))
                        .groupBy(product.productId)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(product.wishlistCount.desc())
                        .fetch();

        long total =
                queryFactory.select(Wildcard.count)
                        .from(product)
                        .join(product.productCategories, productCategory)
                        .join(productCategory.category, category)
                        .where(curationByTarget(target, survey), product.productStatus.eq(Status.FORSALE))
                        .groupBy(product.productId)
                        .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Product> findAllWithSeason(Pageable pageable, Survey survey) {
        List<Product> content =
                queryFactory.selectFrom(product)
                        .join(product.periodOptions, periodOption)
                        .where(curationBySeason(survey), product.productStatus.eq(Status.FORSALE))
                        .groupBy(product.productId)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(product.wishlistCount.desc())
                        .fetch();

        long total =
                queryFactory.select(Wildcard.count)
                        .from(product)
                        .join(product.periodOptions, periodOption)
                        .where(curationBySeason(survey), product.productStatus.eq(Status.FORSALE))
                        .groupBy(product.productId)
                        .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Product> findAllWithGroupAndThemes(Pageable pageable, String group, List<String> conceptList) {
        List<Product> content =
                queryFactory.selectFrom(product)
                        .join(product.productCategories, productCategory)
                        .join(productCategory.category, category)
                        .where(curationByThemes(conceptList), product.productStatus.eq(Status.FORSALE), product.in(
                                JPAExpressions.selectFrom(product)
                                        .join(product.productCategories, productCategory)
                                        .join(productCategory.category, category)
                                        .where(curationByGroup(group)))
                        )
                        .groupBy(product.productId)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(product.wishlistCount.desc())
                        .fetch();

        long total =
                queryFactory.select(Wildcard.count)
                        .from(product)
                        .join(product.productCategories, productCategory)
                        .join(productCategory.category, category)
                        .where(curationByThemes(conceptList), product.productStatus.eq(Status.FORSALE), product.in(
                                JPAExpressions.selectFrom(product)
                                        .join(product.productCategories, productCategory)
                                        .join(productCategory.category, category)
                                        .where(curationByGroup(group)))
                        )
                        .groupBy(product.productId)
                        .fetch().size();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression curationBySeason(String season, LocalDate now) {
        switch (season) {
            case "지금":
                if (isBetween(SUMMER, now, AUTUMN)) {
                    return periodOption.startDate.between(SUMMER, AUTUMN.minusDays(1));
                } else if (isBetween(AUTUMN, now, WINTER)) {
                    return periodOption.startDate.between(AUTUMN, WINTER.minusDays(1));
                } else if (isBetween(WINTER, now, SPRING)) {
                    return periodOption.startDate.between(WINTER, SPRING.minusDays(1));
                } else {
                    return periodOption.startDate.between(SPRING.minusDays(1), SPRING.plusMonths(3));
                }

            case "봄에":
                return periodOption.startDate.between(SPRING.minusDays(1), SPRING.plusMonths(3));

            case "여름에":
                return periodOption.startDate.between(SUMMER, AUTUMN.minusDays(1));

            case "가을에":
                return periodOption.startDate.between(AUTUMN, WINTER.minusDays(1));

            case "겨울에":
                return periodOption.startDate.between(WINTER, SPRING.minusDays(1));
        }
        return null;
    }

    private Boolean isBetween(LocalDate start, LocalDate now, LocalDate end) {
        if (now.isAfter(start.minusDays(1)) & now.isBefore(end)) {
            return true;
        }
        return false;
    }

    private String districtConnector(String district) {
        switch (district) {
            case "동남아":
                return "동남아시아";

            case "동북아":
                return "대만/중국/일본";

            case "태평양":
                return "괌&사이판&하와이";

            case "호주":
                return "호주&뉴질랜드";

            case "중앙아시아":
                return "인도/중앙아시아";

            case "아프리카":
                return "아프리카";

            case "유럽":
                return "유럽";

            case "코카서스":
                return "코카서스";

            case "중남미":
                return "중남미";

            case "북미":
                return "북미";
        }
        return district;
    }

    private String themeConnector(String theme) {
        switch (theme) {
            case "봉사활동":
                return "볼론투어";

            case "골프":
                return "골프여행";

            case "힐링":
                return "휴양지";
        }
        return theme;
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
                return productCategory.category.categoryName.eq("트레킹");
        }
        return null;
    }

    private BooleanExpression curationBySeason(Survey survey) {
        switch (survey.getSurveySeason().name()) {
            case "SPRING":
                return periodOption.startDate.between(SPRING.minusDays(1), SPRING.plusMonths(3));

            case "SUMMER":
                return periodOption.startDate.between(SUMMER, AUTUMN.minusDays(1));

            case "AUTUMN":
                return periodOption.startDate.between(AUTUMN, WINTER.minusDays(1));

            case "WINTER":
                return periodOption.startDate.between(WINTER, SPRING.minusDays(1));
        }
        return null;
    }

    private BooleanBuilder curationByThemes(List<String> conceptList) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        conceptList.stream()
                .map(this::curationByTheme)
                .forEach(booleanBuilder::or);

        return booleanBuilder;
    }

    private BooleanExpression curationByGroup(String group) {
        switch (group) {
            case "남자끼리":
                return productCategory.category.categoryName.eq("남자끼리");

            case "여자끼리":
                return productCategory.category.categoryName.eq("여자끼리");

            case "가족끼리":
                return productCategory.category.categoryName.eq("가족끼리");

            case "5070끼리":
                return productCategory.category.categoryName.eq("5070끼리");

            case "누구든지":
                return productCategory.category.categoryName.eq("누구든지");
        }
        return null;
    }

    private BooleanExpression curationByTheme(String theme) {
        switch (theme) {
            case "쇼핑":
                return productCategory.category.categoryName.eq("쇼핑");

            case "골프":
                return productCategory.category.categoryName.eq("골프여행");

            case "힐링":
                return productCategory.category.categoryName.eq("휴양지");

            case "와인":
                return productCategory.category.categoryName.eq("와인");

            case "문화탐방":
                return productCategory.category.categoryName.eq("문화탐방");

            case "성지순례":
                return productCategory.category.categoryName.eq("성지순례");

            case "봉사활동":
                return productCategory.category.categoryName.eq("볼론투어");

            case "트레킹":
                return productCategory.category.categoryName.eq("트레킹");
        }
        return null;
    }
}