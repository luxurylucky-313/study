<template>
  <MainLayout>
    <div class="ai-chat">
      <div class="chat-messages" ref="messagesContainer">
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
      
      <div class="input-area">
        <el-input
          v-model="userInput"
          type="textarea"
          :rows="3"
          placeholder="请输入您的问题..."
          @keyup.enter.exact="sendMessage"
        />
        <el-button 
          type="primary" 
          :loading="isLoading"
          @click="sendMessage"
          class="send-button"
        >
          <i class="fa-solid fa-paper-plane"></i>
          发送
        </el-button>
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

const messages = ref([])
const userInput = ref('')
const isLoading = ref(false)
const messagesContainer = ref(null)

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
</script>

<style scoped>
.ai-chat {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f8f9fa;
}

.message {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  animation: fadeIn 0.3s ease;
}

/* 修改用户消息的样式 */
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

.input-area {
  padding: 20px;
  background: white;
  border-top: 1px solid #eee;
  display: flex;
  gap: 12px;
}

.send-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 24px;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Markdown 样式 */
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