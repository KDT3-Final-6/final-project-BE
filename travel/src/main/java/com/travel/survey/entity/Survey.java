package com.travel.survey.entity;

import lombok.AccessLevel;
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
    private Gender surveyGroup;

    @Column(name = "survey_partner")
    @Enumerated(EnumType.STRING)
    private Gender surveyPartner;

    @Column(name = "survey_religion")
    @Enumerated(EnumType.STRING)
    private Gender surveyReligion;

    @Column(name = "survey_season")
    @Enumerated(EnumType.STRING)
    private Gender surveySeason;

    @Column(name = "survey_theme")
    @Enumerated(EnumType.STRING)
    private Gender surveyTheme;
}
