package com.example.sentenceanalysis.controller;

import com.example.sentenceanalysis.dto.SentenceRequest;
import com.example.sentenceanalysis.dto.SentenceResponse;
import com.example.sentenceanalysis.service.SentenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analyze")
public class SentenceController {

    private final SentenceService sentenceService;

    public SentenceController(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

    @PostMapping
    public ResponseEntity<SentenceResponse> analyzeSentence(@Valid @RequestBody SentenceRequest request) {
        return ResponseEntity.ok(sentenceService.analyze(request.getSentence()));
    }
}
