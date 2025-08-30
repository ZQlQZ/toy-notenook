import { http } from './httpClient';
import { config } from './config';

// 笔记服务类
class NoteService {
  constructor() {
    this.baseUrl = '';
  }

  // 获取用户笔记列表
  async getNotesByUserId(userId) {
    try {
      const response = await http.get(`/api/notes/user/${userId}`);
      return response;
    } catch (error) {
      throw error;
    }
  }

  // 获取笔记详情
  async getNoteById(id) {
    try {
      const response = await http.get(`/api/notes/${id}`);
      return response;
    } catch (error) {
      throw error;
    }
  }

  // 创建笔记
  async createNote(noteData) {
    try {
      const response = await http.post('/api/notes', noteData);
      return response;
    } catch (error) {
      throw error;
    }
  }

  // 更新笔记
  async updateNote(noteData) {
    try {
      const response = await http.put('/api/notes', noteData);
      return response;
    } catch (error) {
      throw error;
    }
  }

  // 删除笔记
  async deleteNote(id) {
    try {
      const response = await http.delete(`/api/notes/${id}`);
      return response;
    } catch (error) {
      throw error;
    }
  }


}

// 创建单例实例
const noteService = new NoteService();

export default noteService;