package com.travel.survey.Controller;

import com.travel.survey.dto.SurveyDTO;
import com.travel.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<String> postSurvey(@Valid @RequestBody SurveyDTO.PostSurvey postSurvey,
                                             Authentication authentication) {
        surveyService.saveSurvey(postSurvey, authentication.getName());
        return ResponseEntity.ok(null);
    }
}
