package com.example.plusproject.domain.reivew.controller;

import com.example.plusproject.domain.reivew.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
}
