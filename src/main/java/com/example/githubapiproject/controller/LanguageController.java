package com.example.githubapiproject.controller;


import com.example.githubapiproject.model.LanguageStatisticsResponse;
import com.example.githubapiproject.service.LanguageService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(
            LanguageService languageService) {

        this.languageService = languageService;
    }

    @GetMapping
    public List<LanguageStatisticsResponse>
    getLanguageStatistics() {

        return languageService.getLanguageStatistics();
    }
}
