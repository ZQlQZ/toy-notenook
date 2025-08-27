package com.example.project.controller;

import com.example.project.common.Result;
import com.example.project.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    
    @Autowired
    private AiService aiService;
    
    @PostMapping("/analyze")
    public Result<Map<String, Object>> analyzeNote(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        return Result.ok(aiService.analyzeNote(content));
    }
    
    @PostMapping("/summarize")
    public Result<Map<String, Object>> summarizeNote(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        return Result.ok(aiService.summarizeNote(content));
    }
    
    @PostMapping("/chat")
    public Result<Map<String, Object>> chatWithNote(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String content = request.get("content");
        return Result.ok(aiService.chatWithNote(question, content));
    }
}