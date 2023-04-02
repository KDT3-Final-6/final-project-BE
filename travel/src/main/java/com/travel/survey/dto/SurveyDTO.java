package com.travel.survey.dto;

import com.travel.survey.entity.*;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class SurveyDTO {

    @Getter
    public static class PostSurvey {

        @NotBlank
        private String surveyGender;

        @NotBlank
        private String surveyGroup;

        @NotBlank
        private String surveyCompanion;

        @NotBlank
        private String surveyReligion;

        @NotBlank
        private String surveySeason;

        @NotBlank
        private String surveyHobby;

        public <T extends Enum<T>> T convertStringToEnum(String value, Class enumClass) {
            if (enumClass.equals(Companion.class)) {
                for (Companion enumValue : Companion.values()) {
                    if (enumValue.getKorean().equals(value)) {
                        return (T) enumValue;
                    }
                }
            } else if (enumClass.equals(Gender.class)) {
                for (Gender enumValue : Gender.values()) {
                    if (enumValue.getKorean().equals(value)) {
                        return (T) enumValue;
                    }
                }
            } else if (enumClass.equals(Group.class)) {
                for (Group enumValue : Group.values()) {
                    if (enumValue.getKorean().equals(value)) {
                        return (T) enumValue;
                    }
                }
            } else if (enumClass.equals(Hobby.class)) {
                for (Hobby enumValue : Hobby.values()) {
                    if (enumValue.getKorean().equals(value)) {
                        return (T) enumValue;
                    }
                }
            } else if (enumClass.equals(Religion.class)) {
                for (Religion enumValue : Religion.values()) {
                    if (enumValue.getKorean().equals(value)) {
                        return (T) enumValue;
                    }
                }
            } else if (enumClass.equals(Season.class)) {
                for (Season enumValue : Season.values()) {
                    if (enumValue.getKorean().equals(value)) {
                        return (T) enumValue;
                    }
                }
            }

            return null;
        }

        public Survey toEntity() {
            Survey survey = Survey.builder()
                    .surveyCompanion(convertStringToEnum(surveyCompanion, Companion.class))
                    .surveyGender(convertStringToEnum(surveyGender, Gender.class))
                    .surveyGroup(convertStringToEnum(surveyGroup, Group.class))
                    .surveyHobby(convertStringToEnum(surveyHobby, Hobby.class))
                    .surveyReligion(convertStringToEnum(surveyReligion, Religion.class))
                    .surveySeason(convertStringToEnum(surveySeason, Season.class))
                    .build();
            return survey;
        }
    }


    @Getter
    public static class GetSurvey {
        private String surveyGender;
        private String surveyGroup;
        private String surveyCompanion;
        private String surveyReligion;
        private String surveySeason;
        private String surveyHobby;

        public GetSurvey(Survey survey) {
            this.surveyGender = survey.getSurveyGender().getKorean();
            this.surveyGroup = survey.getSurveyGroup().getKorean();
            this.surveyCompanion = survey.getSurveyCompanion().getKorean();
            this.surveyReligion = survey.getSurveyReligion().getKorean();
            this.surveySeason = survey.getSurveySeason().getKorean();
            this.surveyHobby = survey.getSurveyHobby().getKorean();
        }
    }
}
