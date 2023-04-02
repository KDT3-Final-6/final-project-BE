package com.travel.survey.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "survey")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long surveyId;

    @Column(name = "survey_gender")
    @Enumerated(EnumType.STRING)
    private Gender surveyGender;

    @Column(name = "survey_group")
    @Enumerated(EnumType.STRING)
    private Group surveyGroup;

    @Column(name = "survey_companion")
    @Enumerated(EnumType.STRING)
    private Companion surveyCompanion;

    @Column(name = "survey_religion")
    @Enumerated(EnumType.STRING)
    private Religion surveyReligion;

    @Column(name = "survey_season")
    @Enumerated(EnumType.STRING)
    private Season surveySeason;

    @Column(name = "survey_hobby")
    @Enumerated(EnumType.STRING)
    private Gender surveyHobby;

    @Builder
    public Survey(Gender surveyGender, Group surveyGroup, Companion surveyCompanion, Religion surveyReligion, Season surveySeason, Gender surveyHobby) {
        this.surveyGender = surveyGender;
        this.surveyGroup = surveyGroup;
        this.surveyCompanion = surveyCompanion;
        this.surveyReligion = surveyReligion;
        this.surveySeason = surveySeason;
        this.surveyHobby = surveyHobby;
    }
}
