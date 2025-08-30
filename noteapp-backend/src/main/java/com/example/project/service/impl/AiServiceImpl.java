package com.example.project.service.impl;

import com.example.project.service.AiService;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AiServiceImpl implements AiService {
    
    @Value("${ark.api.key:}")
    private String apiKey;
    
    @Value("${ark.base.url:https://ark.cn-beijing.volces.com/api/v3}")
    private String baseUrl;
    
    @Value("${ark.model.endpoint}")
    private String modelEndpoint;
    
    private ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
    private Dispatcher dispatcher = new Dispatcher();
    
    @Override
    public Map<String, Object> analyzeNote(String content) {
        try {
            ArkService service = ArkService.builder()
                    .dispatcher(dispatcher)
                    .connectionPool(connectionPool)
                    .baseUrl(baseUrl)
                    .apiKey(apiKey)
                    .build();
            
            final List<ChatMessage> messages = new ArrayList<>();
            final ChatMessage userMessage = ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .content("请分析以下笔记内容，提供一个简短的摘要和关键词：\n\n" + content)
                    .build();
            messages.add(userMessage);
            
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(modelEndpoint)
                    .messages(messages)
                    .build();
            
            Object responseContentObj = service.createChatCompletion(chatCompletionRequest)
                    .getChoices().get(0).getMessage().getContent();
            
            // 正确解析返回的内容
            String responseContent = "";
            if (responseContentObj != null) {
                // 如果是字符串类型，直接使用
                if (responseContentObj instanceof String) {
                    responseContent = (String) responseContentObj;
                } else {
                    // 如果是其他对象类型，尝试获取其文本内容
                    // 根据豆包AI SDK的文档，getContent()可能返回字符串或复杂对象
                    responseContent = responseContentObj.toString();
                    
                    // 如果toString()返回"[object Object]"，则需要进一步解析
                    if ("[object Object]".equals(responseContent)) {
                        // 尝试通过反射获取text属性
                        try {
                            java.lang.reflect.Method textMethod = responseContentObj.getClass().getMethod("getText");
                            Object textObj = textMethod.invoke(responseContentObj);
                            if (textObj != null) {
                                responseContent = textObj.toString();
                            }
                        } catch (Exception e) {
                            // 如果无法通过反射获取，记录日志并使用原始toString()结果
                            System.err.println("无法解析AI返回内容: " + e.getMessage());
                        }
                    }
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("analysis", responseContent);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("message", "分析成功");
            responseData.put("data", result);
            
            return responseData;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "分析失败: " + e.getMessage());
            return errorResponse;
        }
    }
    
    @Override
    public Map<String, Object> summarizeNote(String content) {
        try {
            // 添加调试日志
            System.out.println("API Key: " + apiKey);
            System.out.println("Base URL: " + baseUrl);
            System.out.println("Model Endpoint: " + modelEndpoint);
            
            ArkService service = ArkService.builder()
                    .dispatcher(dispatcher)
                    .connectionPool(connectionPool)
                    .baseUrl(baseUrl)
                    .apiKey(apiKey)
                    .build();
            
            final List<ChatMessage> messages = new ArrayList<>();
            final ChatMessage userMessage = ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .content("请为以下笔记内容提供一个简洁的总结：\n\n" + content)
                    .build();
            messages.add(userMessage);
            
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(modelEndpoint)
                    .messages(messages)
                    .build();
            
            Object responseContentObj = service.createChatCompletion(chatCompletionRequest)
                    .getChoices().get(0).getMessage().getContent();
            
            // 正确解析返回的内容
            String responseContent = "";
            if (responseContentObj != null) {
                // 如果是字符串类型，直接使用
                if (responseContentObj instanceof String) {
                    responseContent = (String) responseContentObj;
                } else {
                    // 如果是其他对象类型，尝试获取其文本内容
                    // 根据豆包AI SDK的文档，getContent()可能返回字符串或复杂对象
                    responseContent = responseContentObj.toString();
                    
                    // 如果toString()返回"[object Object]"，则需要进一步解析
                    if ("[object Object]".equals(responseContent)) {
                        // 尝试通过反射获取text属性
                        try {
                            java.lang.reflect.Method textMethod = responseContentObj.getClass().getMethod("getText");
                            Object textObj = textMethod.invoke(responseContentObj);
                            if (textObj != null) {
                                responseContent = textObj.toString();
                            }
                        } catch (Exception e) {
                            // 如果无法通过反射获取，记录日志并使用原始toString()结果
                            System.err.println("无法解析AI返回内容: " + e.getMessage());
                        }
                    }
                }
            }
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("message", "总结成功");
            responseData.put("data", responseContent);
            
            return responseData;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "总结失败: " + e.getMessage());
            return errorResponse;
        }
    }
    
    @Override
    public Map<String, Object> chatWithNote(String question, String content) {
        try {
            ArkService service = ArkService.builder()
                    .dispatcher(dispatcher)
                    .connectionPool(connectionPool)
                    .baseUrl(baseUrl)
                    .apiKey(apiKey)
                    .build();
            
            final List<ChatMessage> messages = new ArrayList<>();
            final ChatMessage userMessage = ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .content("基于以下笔记内容回答问题：" + question + "\n\n笔记内容：\n" + content)
                    .build();
            messages.add(userMessage);
            
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(modelEndpoint)
                    .messages(messages)
                    .build();
            
            Object responseContentObj = service.createChatCompletion(chatCompletionRequest)
                    .getChoices().get(0).getMessage().getContent();
            
            // 正确解析返回的内容
            String responseContent = "";
            if (responseContentObj != null) {
                // 如果是字符串类型，直接使用
                if (responseContentObj instanceof String) {
                    responseContent = (String) responseContentObj;
                } else {
                    // 如果是其他对象类型，尝试获取其文本内容
                    // 根据豆包AI SDK的文档，getContent()可能返回字符串或复杂对象
                    responseContent = responseContentObj.toString();
                    
                    // 如果toString()返回"[object Object]"，则需要进一步解析
                    if ("[object Object]".equals(responseContent)) {
                        // 尝试通过反射获取text属性
                        try {
                            java.lang.reflect.Method textMethod = responseContentObj.getClass().getMethod("getText");
                            Object textObj = textMethod.invoke(responseContentObj);
                            if (textObj != null) {
                                responseContent = textObj.toString();
                            }
                        } catch (Exception e) {
                            // 如果无法通过反射获取，记录日志并使用原始toString()结果
                            System.err.println("无法解析AI返回内容: " + e.getMessage());
                        }
                    }
                }
            }
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("message", "问答成功");
            responseData.put("data", responseContent);
            
            return responseData;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "问答失败: " + e.getMessage());
            return errorResponse;
        }
    }
}