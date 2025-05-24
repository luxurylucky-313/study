<!-- filepath: c:\Users\斌斌犇犇\Desktop\ai助教\study\study\src\views\News\NewsView.vue -->
<template>
  <div class="chat-container">
    <div class="sidebar">
      <div class="system-notifications">
        <h3>消息通知</h3>
        <div class="notification" @click="viewNotification">
          <p>系统通知</p><span class="notification-count">99+</span>
        </div>
      </div>
      <div class="student-list">
        <h3>联系人</h3>
        <div v-for="student in students" :key="student.id" class="student" @click="selectStudent(student)">
          <img src="@/assets/images/head2.jpg" alt="avatar" class="avatar" />
          <p>{{ student.name }}</p>
          <span class="message-count">{{ student.messageCount }}</span>
        </div>
      </div>
    </div>
    <div class="chat-area">
      <div class="chat-header">
        <h3>{{ selectedNotification ? '系统通知' : (selectedStudent ? selectedStudent.name : '请选择联系人') }}</h3>
      </div>
      <div class="messages">
        <div v-if="selectedNotification" class="notification-detail">
          <div class="notification-item" v-for="notification in systemNotifications" :key="notification.id">
            <p>系统通知</p>
            <p>{{ notification.course }}的作业{{ notification.work }}已成功发布</p>
            <span class="notification-time">{{ notification.time }}</span>
          </div>
        </div>
        <div v-else v-for="message in messages" :key="message.id" class="message">
          <div class="message-content"
            :class="{ 'from-student': message.from === 'student', 'from-teacher': message.from === 'teacher' }">
            <p>{{ message.text }}</p>
            <img v-if="message.image" src="@/assets/images/head2.jpg" alt="Image" />
            <audio v-if="message.audio" :src="message.audio" controls></audio>
            <a v-if="message.file" :href="message.file" download>
              <img src="@/assets/images/head2.jpg" alt="Download" class="link-icon" />
            </a>
          </div>
        </div>
      </div>
      <div class="input-area" v-if="!selectedNotification">
        <input v-model="newMessage" type="text" placeholder="请输入..." />
        <input type="file" @change="handleFileUpload" id="file-upload" />
        <label for="file-upload" class="file-upload-label">
          <i class="fas fa-paperclip"></i>
        </label>
        <button @click="sendMessage">
          <i class="fas fa-paper-plane"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      messages: [],
      newMessage: '',
      file: null,
      systemNotifications: [
        { id: 1, course: '22计算机1，2班', work: '软件工程', time: '22:22' },
        { id: 2, course: '23计算机1，2班', work: '高级数据库', time: '22:22' },
        { id: 3, course: '22信管1，2班', work: '软件工程', time: '22:22' },
        { id: 4, course: '22互金1，2班', work: 'web开发', time: '22:22' },
        { id: 5, course: '22电商1，2班', work: '人工智能', time: '22:22' }
        // ...更多系统通知
      ],
      students: [
        { id: 1, name: '马立奥', avatar: '@/assets/images/head2.jpg', messageCount: 3 },
        { id: 2, name: 'ting', avatar: '@/assets/images/head2.jpg', messageCount: 2 },
        // ...更多学生
      ],
      selectedStudent: null,
      selectedNotification: false
    };
  },
  methods: {
    sendMessage() {
      if (this.newMessage.trim() || this.file) {
        const message = {
          id: Date.now(),
          from: 'teacher',
          text: this.newMessage,
          image: this.file && this.file.type.startsWith('image/') ? URL.createObjectURL(this.file) : null,
          audio: this.file && this.file.type.startsWith('audio/') ? URL.createObjectURL(this.file) : null,
          file: this.file && !this.file.type.startsWith('image/') && !this.file.type.startsWith('audio/') ? URL.createObjectURL(this.file) : null
        };
        this.messages.push(message);
        this.newMessage = '';
        this.file = null;
      }
    },
    handleFileUpload(event) {
      this.file = event.target.files[0];
    },
    viewNotification() {
      this.selectedNotification = true;
      this.selectedStudent = null;
    },
    selectStudent(student) {
      this.selectedStudent = student;
      this.selectedNotification = false;
      // 加载学生消息
      this.messages = [
        { id: 1, from: 'student', text: '老师在吗？' },
        { id: 2, from: 'student', text: '请问今天的作业可以在布置一下吗？' },
        { id: 3, from: 'student', text: '我没有收到作业' },
        { id: 4, from: 'teacher', text: '好的，我马上布置' },
        { id: 5, from: 'teacher', text: '下次上课一定要认真听！' }
        // ...更多学生消息
      ];
    }
  }
};
</script>

<style scoped>
.chat-container {
  display: flex;
  height: 100vh;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  overflow: hidden;
  background-image: linear-gradient(to bottom, #f0f4ff 0%, #e6f9ff 100%);
}

.sidebar {
  width: 280px;
  border-right: 1px solid #e0e0e0;
  padding: 15px;
  background-image: linear-gradient(to bottom, #f0f4ff 0%, #e6f9ff 100%);
}

.system-notifications,
.student-list {
  margin-bottom: 25px;
}

.system-notifications h3,
.student-list h3 {
  margin-bottom: 15px;
  color: #333333;
}

.notification-count {
  background-color: #ff4d4f;
  color: white;
  border-radius: 50%;
  padding: 5px 12px;
}

.notification,
.student {
  display: flex;
  align-items: center;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  margin-bottom: 12px;
  cursor: pointer;
  background-color: #ffffff;
  transition: background-color 0.3s, box-shadow 0.3s;
}

.notification:hover,
.student:hover {
  background-color: #f0f0f0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.avatar {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  margin-right: 15px;
}

.message-count {
  margin-left: auto;
  background-color: #ff4d4f;
  color: white;
  border-radius: 50%;
  padding: 5px 12px;
}

.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
}

.chat-header {
  padding: 15px;
  border-bottom: 1px solid #e0e0e0;
  background-image: linear-gradient(to bottom, #f0f4ff 0%, #e6f9ff 100%);
  text-align: center;
  color: #333333;
}

.messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}

.message {
  margin-bottom: 12px;
  display: flex;
  justify-content: flex-start;
}

.message-content {
  padding: 12px;
  border-radius: 10px;
  max-width: 70%;
  transition: background-color 0.3s, box-shadow 0.3s;
}

.from-student {
  background-color: #f5f5f5;
  align-self: flex-start;
}

.from-teacher {
  background-color: #e6f7ff;
  align-self: flex-end;
  margin-left: auto;
}

.input-area {
  display: flex;
  align-items: center;
  padding: 15px;
  border-top: 1px solid #e0e0e0;
  background-color: #f5f5f5;
}

input[type="text"] {
  flex: 1;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  margin-right: 15px;
}

input[type="file"] {
  display: none;
}

.file-upload-label {
  cursor: pointer;
  margin-right: 15px;
  color: #1890ff;
}

button {
  padding: 12px 25px;
  border: none;
  border-radius: 10px;
  background-color: #1890ff;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
}

button:hover {
  background-color: #40a9ff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

button i,
.file-upload-label i {
  font-size: 18px;
}

.link-icon {
  width: 22px;
  height: 22px;
}

.notification-detail {
  padding: 25px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  background-color: #ffffff;
  margin-bottom: 12px;
}

.notification-item {
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  margin-bottom: 12px;
  background-color: #ffffff;
}

.notification-item p {
  margin: 0;
}

.notification-time {
  text-align: right;
  color: #999999;
}
</style>