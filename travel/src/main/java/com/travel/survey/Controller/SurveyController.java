package com.travel.survey.Controller;

import com.travel.survey.dto.SurveyDTO;
import com.travel.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<SurveyDTO.GetSurvey> getSurvey(Authentication authentication) {
        return ResponseEntity.ok(surveyService.displaySurvey(authentication.getName()));
    }
}
