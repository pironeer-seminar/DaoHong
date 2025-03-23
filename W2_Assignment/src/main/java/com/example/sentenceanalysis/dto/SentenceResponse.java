package com.example.sentenceanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SentenceResponse {
    private int length;
    private int wordCnt;
    private boolean containSpring;
}
