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
        <div class="note-footer">
          <span>创建时间: {{ formatTime(note.createTime) }}</span>
          <span>更新时间: {{ formatTime(note.updateTime) }}</span>
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
import { authService } from '@/services';

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
    const userInfo = authService.getUserInfo();
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
        
        if (data.success) {
          this.notes = data.data || [];
        }
      } catch (error) {
        console.error('加载笔记失败:', error);
      }
    },
    
    async handleCreateNote() {
      try {
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
        
        if (result.success) {
          this.showCreateForm = false;
          this.newNote = { title: '', content: '' };
          await this.loadNotes();
        }
      } catch (error) {
        console.error('创建笔记失败:', error);
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
        
        if (result.success) {
          this.showEditForm = false;
          await this.loadNotes();
        }
      } catch (error) {
        console.error('更新笔记失败:', error);
      }
    },
    
    async deleteNote(noteId) {
      if (!confirm('确定要删除这个笔记吗？')) return;
      
      try {
        const response = await fetch(`/api/notes/${noteId}`, {
          method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.success) {
          await this.loadNotes();
        }
      } catch (error) {
        console.error('删除笔记失败:', error);
      }
    },
    
    handleLogout() {
      authService.clearAuthData();
      this.$router.push('/');
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
  font-size: 12px;
  color: #999;
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