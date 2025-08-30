import { http } from './httpClient';
import { config } from './config';

// AI服务类
class AiService {
  constructor() {
    this.baseUrl = '';
  }

  // 分析笔记内容
  async analyzeNote(content) {
    try {
      // 添加调试日志
      const url = `${this.baseUrl}${config.ai.endpoints.analyze}`;
      console.log('AI分析请求URL:', url);
      const response = await http.post(url, { content });
      return response;
    } catch (error) {
      console.error('AI分析笔记失败:', error);
      // 返回统一的错误格式
      return {
        code: 500,
        msg: error.message || '网络请求失败',
        data: null
      };
    }
  }

  // 总结笔记内容
  async summarizeNote(content) {
    try {
      // 添加调试日志
      const url = `${this.baseUrl}${config.ai.endpoints.summarize}`;
      console.log('AI总结请求URL:', url);
      const response = await http.post(url, { content });
      return response;
    } catch (error) {
      console.error('AI总结笔记失败:', error);
      // 返回统一的错误格式
      return {
        code: 500,
        msg: error.message || '网络请求失败',
        data: null
      };
    }
  }

  // 基于笔记内容进行问答
  async chatWithNote(question, content) {
    try {
      // 添加调试日志
      const url = `${this.baseUrl}${config.ai.endpoints.chat}`;
      console.log('AI问答请求URL:', url);
      const response = await http.post(url, { question, content });
      return response;
    } catch (error) {
      console.error('AI问答失败:', error);
      // 返回统一的错误格式
      return {
        code: 500,
        msg: error.message || '网络请求失败',
        data: null
      };
    }
  }
}

// 创建单例实例
const aiService = new AiService();

export default aiService;