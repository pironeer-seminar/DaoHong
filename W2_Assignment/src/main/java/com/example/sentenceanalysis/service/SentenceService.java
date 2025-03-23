package com.example.sentenceanalysis.service;

import com.example.sentenceanalysis.dto.SentenceResponse;
import org.springframework.stereotype.Service;

@Service
public class SentenceService {

    public SentenceResponse analyze(String sentence) {
        int length = sentence.length();
        int wordCount = sentence.trim().split("\\s+").length;
        boolean containsSpring = sentence.toLowerCase().contains("spring");

        return new SentenceResponse(length, wordCount, containsSpring);
    }
}
