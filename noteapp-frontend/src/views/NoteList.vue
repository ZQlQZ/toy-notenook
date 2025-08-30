<template>
  <div class="note-container">
    <div class="header">
      <h1>我的笔记</h1>
      <div class="user-info">
        <span>欢迎, {{ username }}</span>
        <button @click="handleLogout" class="logout-btn">退出登录</button>
      </div>
    </div>
    
    <div class="note-actions">
      <button @click="showCreateForm = true" class="create-btn">新建笔记</button>
    </div>
    
    <!-- 新建笔记表单 -->
    <div v-if="showCreateForm" class="note-form">
      <h2>新建笔记</h2>
      <form @submit.prevent="handleCreateNote">
        <div class="form-group">
          <input v-model="newNote.title" type="text" placeholder="笔记标题" required class="form-control" />
        </div>
        <div class="form-group">
          <textarea v-model="newNote.content" placeholder="笔记内容" required class="form-control" rows="5"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="save-btn">保存</button>
          <button type="button" @click="showCreateForm = false" class="cancel-btn">取消</button>
        </div>
      </form>
    </div>
    
    <!-- 笔记列表 -->
    <div v-if="notes.length > 0" class="note-list">
      <div v-for="note in notes" :key="note.id" class="note-item">
        <div class="note-header">
          <h3>{{ note.title }}</h3>
          <div class="note-actions">
            <button @click="editNote(note)" class="edit-btn">编辑</button>
            <button @click="deleteNote(note.id)" class="delete-btn">删除</button>
          </div>
        </div>
        <div class="note-content">
          <p>{{ note.content }}</p>
        </div>
        <div class="note-ai-summary" v-if="note.aiSummary">
          <strong>AI总结:</strong> {{ note.aiSummary }}
        </div>
        <div class="note-footer">
          <span>创建时间: {{ formatTime(note.createTime) }}</span>
          <span>更新时间: {{ formatTime(note.updateTime) }}</span>
          <button @click="summarizeNote(note)" class="ai-summary-btn">AI总结</button>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-else class="empty-state">
      <p>暂无笔记，点击"新建笔记"开始创建</p>
    </div>
    
    <!-- 编辑笔记表单 -->
    <div v-if="showEditForm" class="note-form">
      <h2>编辑笔记</h2>
      <form @submit.prevent="handleUpdateNote">
        <div class="form-group">
          <input v-model="editingNote.title" type="text" placeholder="笔记标题" required class="form-control" />
        </div>
        <div class="form-group">
          <textarea v-model="editingNote.content" placeholder="笔记内容" required class="form-control" rows="5"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="save-btn">更新</button>
          <button type="button" @click="showEditForm = false" class="cancel-btn">取消</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { authService, authUtils, aiService } from '@/services';

export default {
  name: 'NoteList',
  data() {
    return {
      username: '',
      userId: null,
      notes: [],
      showCreateForm: false,
      showEditForm: false,
      newNote: {
        title: '',
        content: ''
      },
      editingNote: {
        id: null,
        title: '',
        content: ''
      }
    };
  },
  async created() {
    // 获取用户信息
    const userInfo = authUtils.getUserInfo();
    if (userInfo) {
      this.username = userInfo.username || '';
      this.userId = userInfo.userId || userInfo.id || null;
      await this.loadNotes();
    }
  },
  methods: {
    async loadNotes() {
      if (!this.userId) return;
      
      try {
        const response = await fetch(`/api/notes/user/${this.userId}`);
        const data = await response.json();
        
        // 检查后端返回的状态码，200表示成功
        if (data.code === 200) {
          this.notes = data.data || [];
        } else {
          console.error('加载笔记失败:', data.msg);
          alert('加载笔记失败: ' + (data.msg || '未知错误'));
        }
        

      } catch (error) {
        console.error('加载笔记失败:', error);
      }
    },
    
    async handleCreateNote() {
      try {
        // 添加用户ID检查
        if (!this.userId) {
          console.error('用户ID不存在，无法创建笔记');
          alert('用户信息不完整，请重新登录');
          return;
        }
        
        // 添加输入验证
        if (!this.newNote.title || !this.newNote.content) {
          alert('请填写笔记标题和内容');
          return;
        }
        
        const noteData = {
          userId: this.userId,
          title: this.newNote.title,
          content: this.newNote.content
        };
        
        const response = await fetch('/api/notes', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(noteData)
        });
        
        const result = await response.json();
        
        // 检查后端返回的状态码，200表示成功
        if (result.code === 200) {
          this.showCreateForm = false;
          this.newNote = { title: '', content: '' };
          await this.loadNotes();
          // 添加成功提示
          alert('笔记创建成功');
        } else {
          // 处理后端返回的错误
          console.error('创建笔记失败:', result.msg);
          alert('创建笔记失败: ' + (result.msg || '未知错误'));
        }
      } catch (error) {
        console.error('创建笔记失败:', error);
        alert('创建笔记失败: ' + error.message);
      }
    },
    
    editNote(note) {
      this.editingNote = { ...note };
      this.showEditForm = true;
    },
    
    async handleUpdateNote() {
      try {
        const response = await fetch('/api/notes', {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.editingNote)
        });
        
        const result = await response.json();
        
        // 检查后端返回的状态码，200表示成功
        if (result.code === 200) {
          this.showEditForm = false;
          await this.loadNotes();
          alert('笔记更新成功');
        } else {
          console.error('更新笔记失败:', result.msg);
          alert('更新笔记失败: ' + (result.msg || '未知错误'));
        }
      } catch (error) {
        console.error('更新笔记失败:', error);
        alert('更新笔记失败: ' + error.message);
      }
    },
    
    async deleteNote(noteId) {
      if (!confirm('确定要删除这个笔记吗？')) return;
      
      try {
        const response = await fetch(`/api/notes/${noteId}`, {
          method: 'DELETE'
        });
        
        const result = await response.json();
        
        // 检查后端返回的状态码，200表示成功
        if (result.code === 200) {
          await this.loadNotes();
          alert('笔记删除成功');
        } else {
          console.error('删除笔记失败:', result.msg);
          alert('删除笔记失败: ' + (result.msg || '未知错误'));
        }
      } catch (error) {
        console.error('删除笔记失败:', error);
        alert('删除笔记失败: ' + error.message);
      }
    },
    
    async handleLogout() {
      try {
        // 调用认证服务的登出方法
        await authService.logout();
        console.log('用户已登出');
        // 重定向到登录页面
        this.$router.push('/');
      } catch (error) {
        console.error('登出失败:', error);
        // 即使登出失败，也清除本地数据并重定向到登录页面
        authService.clearAuthData();
        this.$router.push('/');
      }
    },
    
    // AI总结笔记
    async summarizeNote(note) {
      try {
        // 调用AI服务进行笔记总结
        const result = await aiService.summarizeNote(note.content);
        
        // 检查后端返回的状态码，200表示成功
        if (result.code === 200) {
          // 正确提取AI总结内容
          let summaryContent = '';
          if (typeof result.data === 'string') {
            // 如果data是字符串，直接使用
            summaryContent = result.data;
          } else if (result.data && typeof result.data === 'object') {
            // 如果data是对象，尝试提取内容
            if (result.data.analysis) {
              // analyzeNote返回的结构
              summaryContent = result.data.analysis;
            } else if (result.data.data) {
              // 可能的嵌套结构
              summaryContent = result.data.data;
            } else {
              // 尝试将对象转换为JSON字符串
              summaryContent = JSON.stringify(result.data);
            }
          } else {
            // 其他情况，转换为字符串
            summaryContent = String(result.data);
          }
          
          // 更新笔记对象，添加AI总结
          note.aiSummary = summaryContent;
          
          // 询问用户是否将总结写入笔记内容
          if (confirm('AI总结完成：' + summaryContent + '\n\n是否将总结写入笔记内容？')) {
            // 更新笔记内容，将AI总结添加到原文末尾
            note.content = note.content + '\n\n---AI总结---\n' + summaryContent;
            
            // 调用后端API更新笔记
            const updateResult = await fetch('/api/notes', {
              method: 'PUT',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify(note)
            });
            
            const updateResponse = await updateResult.json();
            
            if (updateResponse.code === 200) {
              alert('AI总结已写入笔记内容');
              // 重新加载笔记列表以显示更新后的内容
              await this.loadNotes();
            } else {
              console.error('更新笔记失败:', updateResponse.msg);
              alert('更新笔记失败: ' + (updateResponse.msg || '未知错误'));
            }
          } else {
            alert('AI总结完成');
          }
        } else {
          console.error('AI总结失败:', result.msg);
          alert('AI总结失败: ' + (result.msg || '未知错误'));
        }
      } catch (error) {
        console.error('AI总结失败:', error);
        alert('AI总结失败: ' + (error.message || '网络请求失败'));
      }
    },
    
    formatTime(time) {
      if (!time) return '';
      return new Date(time).toLocaleString('zh-CN');
    }
  }
};
</script>

<style scoped>
.note-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.header h1 {
  margin: 0;
  color: #333;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.logout-btn {
  padding: 8px 16px;
  background: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background: #e45656;
}

.note-actions {
  margin-bottom: 20px;
}

.create-btn {
  padding: 10px 20px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.create-btn:hover {
  background: #66b1ff;
}

.note-form {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.note-form h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

.form-control {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-actions {
  display: flex;
  gap: 10px;
}

.save-btn, .cancel-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.save-btn {
  background: #409eff;
  color: white;
}

.save-btn:hover {
  background: #66b1ff;
}

.cancel-btn {
  background: #909399;
  color: white;
}

.cancel-btn:hover {
  background: #a6a9ad;
}

.note-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.note-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.note-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.note-header h3 {
  margin: 0;
  color: #333;
  flex: 1;
}

.note-actions {
  display: flex;
  gap: 10px;
}

.edit-btn, .delete-btn {
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.edit-btn {
  background: #409eff;
  color: white;
}

.edit-btn:hover {
  background: #66b1ff;
}

.delete-btn {
  background: #f56c6c;
  color: white;
}

.delete-btn:hover {
  background: #e45656;
}

.note-content p {
  margin: 0 0 15px 0;
  color: #666;
  line-height: 1.6;
}

.note-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #999;
}

.note-ai-summary {
  margin: 10px 0;
  padding: 10px;
  background-color: #f0f9ff;
  border: 1px solid #b3d8ff;
  border-radius: 4px;
  font-size: 14px;
}

.ai-summary-btn {
  padding: 4px 8px;
  background: #67c23a;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.ai-summary-btn:hover {
  background: #85ce61;
}

.empty-state {
  text-align: center;
  padding: 50px;
  color: #999;
}

.empty-state p {
  margin: 0;
  font-size: 18px;
}
</style>