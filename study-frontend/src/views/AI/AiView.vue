<template>
  <MainLayout>
    <div class="ai-welcome-container">
      <div v-if="messages.length === 0">
        <div class="welcome-header">
          <h2>Hi~我是你的AI助教</h2>
          <div class="welcome-sub">你可以向我咨询任何学习上的问题哦~</div>
        </div>
        <div class="welcome-divider"></div>
        <div class="example-questions">
          <div class="example-bubble">如何有效地制定学习计划以提高学习效率？</div>
          <div class="example-bubble">为什么光合作用对生态系统至关重要？</div>
          <div class="example-bubble">在Python中，如何处理文件读取时可能出现的异常？</div>
        </div>
      </div>
      <div class="chat-messages-welcome" ref="messagesContainer" v-show="messages.length > 0">
        <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
          <div class="avatar">
            <i :class="msg.role === 'user' ? 'fa-solid fa-user' : 'fa-solid fa-robot'"></i>
          </div>
          <div class="message-content">
            <div class="message-header">
              <span class="role-name">{{ msg.role === 'user' ? '我' : 'AI助手' }}</span>
              <span class="time">{{ formatTime(msg.timestamp) }}</span>
            </div>
            <div class="content markdown-body" v-html="renderMarkdown(msg.content)"></div>
          </div>
        </div>
      </div>
      <div class="input-area-welcome">
        <el-button
          class="search-btn"
          :icon="Search"
          :class="{ selected: searchSelected }"
          @click="toggleSearch"
        >联网搜索</el-button>
        <el-input
          v-model="userInput"
          class="welcome-input"
          placeholder="请在此输入您的提问~"
          @keyup.enter.exact="sendMessage"
        />
        <div class="input-actions">
          <el-button class="icon-btn" @click="addImage" circle>
            <el-icon><Plus /></el-icon>
          </el-button>
          <el-button class="icon-btn"  @click="addFile" circle>
            <el-icon><Link /></el-icon>
          </el-button>
          <el-button type="primary" :loading="isLoading" @click="sendMessage" class="icon-btn send-btn" circle>
            <el-icon><Promotion /></el-icon>
          </el-button>
        </div>
        <input type="file" ref="imageInput" style="display: none;" accept="image/*" @change="handleImageUpload" />
        <input type="file" ref="fileInput" style="display: none;" @change="handleFileUpload" />
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { marked } from 'marked'
import hljs from 'highlight.js'
import MainLayout from '@/components/MainLayout.vue'
import { chatWithDeepseek } from '@/api/deepseek_api'
import { ElButton, ElIcon, ElInput } from "element-plus";
import { Promotion, Search,Plus, Link } from "@element-plus/icons-vue";

const messages = ref([])
const userInput = ref('')
const isLoading = ref(false)
const messagesContainer = ref(null)
const searchSelected = ref(false) 

// 配置 marked
marked.setOptions({
  highlight: function(code, lang) {
    const language = hljs.getLanguage(lang) ? lang : 'plaintext';
    return hljs.highlight(code, { language }).value;
  },
  breaks: true
})

// 渲染 Markdown
const renderMarkdown = (content) => {
  return marked(content)
}

// 格式化时间
const formatTime = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

const sendMessage = async () => {
  if (!userInput.value.trim() || isLoading.value) return
  
  try {
    isLoading.value = true
    
    messages.value.push({
      role: 'user',
      content: userInput.value,
      timestamp: new Date()
    })

    await scrollToBottom()
    const response = await chatWithDeepseek(userInput.value)
    
    messages.value.push({
      role: 'assistant',
      content: response,
      timestamp: new Date()
    })

    userInput.value = ''
    await scrollToBottom()
    
  } catch (error) {
    console.error('发送消息失败:', error)
    messages.value.push({
      role: 'system',
      content: error.message,
      timestamp: new Date()
    })
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  scrollToBottom()
})
const toggleSearch = () => {
  searchSelected.value = !searchSelected.value
}
// 图片上传相关
const imageInput = ref(null)

const addImage = () => {
  imageInput.value.click()
}

const handleImageUpload = (event) => {
  const file = event.target.files[0]
  if (file && /^image\//.test(file.type)) {
    const reader = new FileReader()
    reader.onload = (e) => {
      messages.value.push({
        role: 'user',
        content: `<img src="${e.target.result}" alt="用户上传的图片" style="max-width: 100%; border-radius: 8px;" />`,
        timestamp: new Date()
      })
      scrollToBottom()
    }
    reader.readAsDataURL(file)
  }
}

// 文件上传相关
const fileInput = ref(null)

const addFile = () => {
  fileInput.value.click()
}

const handleFileUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    // 这里可以扩展为上传至服务器逻辑
    const url = URL.createObjectURL(file)
    messages.value.push({
      role: 'user',
      content: `📎 已上传文件：<a href="${url}" download>${file.name}</a>`,
      timestamp: new Date()
    })
    scrollToBottom()
  }
}
</script>

<style scoped>
.ai-welcome-container {
  margin: 40px auto 0 auto;
  background: #f7fbff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  padding: 40px 32px 24px 32px;
  min-height: 65vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  /* 移除 padding-bottom: 90px; */
  padding-bottom: 0;
}
.welcome-header {
  text-align: center;
}
.welcome-header h2 {
  font-size: 2.2rem;
  font-weight: bold;
  margin-bottom: 8px;
}
.welcome-sub {
  color: #7b8ba3;
  font-size: 1.1rem;
  margin-bottom: 8px;
}
.welcome-divider {
  width: 100%;
  height: 1px;
  background: #e3eaf2;
  margin: 16px 0 24px 0;
}
.example-questions {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  margin-bottom: 32px;
}
.example-bubble {
  background: #f2f6fa;
  color: #4a5a6a;
  border-radius: 20px;
  padding: 8px 18px;
  font-size: 1rem;
  box-shadow: 0 1px 4px rgba(0,0,0,0.03);
  margin-bottom: 0;
  width: 100%;
  max-width: 420px;
  text-align: center;
}
.input-area-welcome {
  display: flex;
  align-items: center;
  width: 100%;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  padding: 12px 20px;
  gap: 12px;
  /* 固定在 ai-welcome-container 底部 */
  position: absolute;
  left: 0;
  bottom: 0;
  z-index: 10;
  max-width: 95%;
  margin: 0 auto;
}
.search-btn {
  background: #f2f6fa;
  color: #4a5a6a;
  border: none;
  font-weight: 500;
  margin-right: 8px;
}
.search-btn.selected {
  background: #409eff;
  color: #fff;
}
.welcome-input {
  flex: 1;
  border-radius: 8px;
  font-size: 1rem;
  margin-right: 8px;
}
.input-actions {
  display: flex;
  align-items: center;
  gap: 6px;
}
.icon-btn {
  background: #f2f6fa;
  color: #4a5a6a;
  border: none;
}
.send-btn {
  background: #409eff;
  color: #fff;
}
.chat-messages-welcome {
  width: 100%;
  margin-top: 16px;
  flex: 1;
  overflow-y: auto;
  min-height: 120px;
}
.message {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  animation: fadeIn 0.3s ease;
}
.message.user {
  flex-direction: row-reverse;
}
.message.user .message-content {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}
.message.user .message-header {
  flex-direction: row-reverse;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.message.user .avatar {
  background: #e1f5fe;
  color: #0288d1;
}
.message.assistant .avatar {
  background: #f3e5f5;
  color: #7b1fa2;
}
.message-content {
  flex: 1;
  max-width: 80%;
}
.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
  color: #666;
  font-size: 12px;
}
.content {
  background: white;
  padding: 12px 16px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}
.message.user .content {
  background: #e3f2fd;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
:deep(.markdown-body) {
  font-size: 14px;
  line-height: 1.6;
}
:deep(.markdown-body pre) {
  background: #282c34;
  padding: 16px;
  border-radius: 6px;
  overflow-x: auto;
  margin: 8px 0;
}
:deep(.markdown-body code) {
  font-family: Consolas, Monaco, 'Courier New', monospace;
  font-size: 13px;
}
:deep(.markdown-body p) {
  margin: 8px 0;
}
:deep(.markdown-body ul, .markdown-body ol) {
  padding-left: 20px;
}
:deep(.markdown-body table) {
  border-collapse: collapse;
  width: 100%;
  margin: 8px 0;
}
:deep(.markdown-body th, .markdown-body td) {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}
:deep(.markdown-body blockquote) {
  border-left: 4px solid #ddd;
  margin: 8px 0;
  padding-left: 16px;
  color: #666;
}
</style>