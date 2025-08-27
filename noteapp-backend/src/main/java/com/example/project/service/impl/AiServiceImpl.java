package com.example.project.service.impl;

import com.example.project.service.AiService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class AiServiceImpl implements AiService {
    
    private final List<String> mockSummaries = Arrays.asList(
        "这是一个关于技术学习的笔记，主要介绍了Spring Boot框架的使用方法。",
        "这份笔记记录了项目开发过程中的关键要点和注意事项。",
        "这是一个关于人工智能发展的思考和总结。"
    );
    
    private final List<String> mockKeywords = Arrays.asList(
        "Spring Boot, MyBatis, Java",
        "项目开发, 注意事项, 技术要点",
        "人工智能, 机器学习, 深度学习"
    );
    
    private final List<String> mockAnswers = Arrays.asList(
        "根据笔记内容，这是一个技术相关的笔记，主要涉及Spring Boot框架的使用。",
        "从内容来看，这是一份项目开发笔记，记录了重要的技术要点。",
        "这份笔记主要讨论了人工智能的发展趋势和应用前景。"
    );
    
    @Override
    public Map<String, Object> analyzeNote(String content) {
        Map<String, Object> response = new HashMap<>();
        Random random = new Random();
        
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("summary", mockSummaries.get(random.nextInt(mockSummaries.size())));
        analysis.put("keywords", mockKeywords.get(random.nextInt(mockKeywords.size())));
        
        response.put("success", true);
        response.put("message", "分析成功");
        response.put("data", analysis);
        
        return response;
    }
    
    @Override
    public Map<String, Object> summarizeNote(String content) {
        Map<String, Object> response = new HashMap<>();
        Random random = new Random();
        
        response.put("success", true);
        response.put("message", "总结成功");
        response.put("data", mockSummaries.get(random.nextInt(mockSummaries.size())));
        
        return response;
    }
    
    @Override
    public Map<String, Object> chatWithNote(String question, String content) {
        Map<String, Object> response = new HashMap<>();
        Random random = new Random();
        
        response.put("success", true);
        response.put("message", "问答成功");
        response.put("data", mockAnswers.get(random.nextInt(mockAnswers.size())));
        
        return response;
    }
}