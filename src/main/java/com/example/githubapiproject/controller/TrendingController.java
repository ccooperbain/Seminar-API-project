package com.example.githubapiproject.controller;


import com.example.githubapiproject.model.TrendingRepositoryResponse;
import com.example.githubapiproject.service.TrendingService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/trending")
public class TrendingController {

    private final TrendingService trendingService;

    public TrendingController(
            TrendingService trendingService) {

        this.trendingService = trendingService;
    }

    @GetMapping
    public List<TrendingRepositoryResponse>
    getTrendingRepositories(
            @RequestParam(defaultValue = "10") int limit) {

        List<TrendingRepositoryResponse> trending =
                trendingService.getTrendingRepositories();

        if (limit < 1) {
            limit = 10;
        }

        return trending.stream()
                .limit(limit)
                .toList();
    }
}