package com.example.project.service;

import java.util.Map;

public interface AiService {
    Map<String, Object> analyzeNote(String content);
    Map<String, Object> summarizeNote(String content);
    Map<String, Object> chatWithNote(String question, String content);
}