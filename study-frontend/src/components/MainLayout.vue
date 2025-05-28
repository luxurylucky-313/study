<template>
  <div class="main-layout">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="header-content">
        <div class="logo-area">
          <h2 class="title">教师端</h2>
        </div>
        <div class="right-area">
          <div class="user-menu">
            <img :src="userAvatar" alt="用户头像" class="avatar">
            <span class="username">{{ Name }}</span>
            <div class="dropdown" @click="toggleDropdown">
              <i class="arrow-down"></i>
              <div v-if="showDropdown" class="dropdown-content">
                <div @click="logout">退出登录</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <div class="container" :class="{ 'blurred': activeTool }">
      <!-- 左侧导航栏 -->
      <aside class="sidebar">
        <div class="user-info">
          <img :src="userAvatar" alt="用户头像" class="avatar1">
          <div class="info">
            <span class="username">{{ Name }}</span>
            <span class="role">教师</span>
          </div>
        </div>
        <nav class="nav-menu">
          <ul style="margin-left: 20px;">
            <router-link to="/home">
              <li>
                <i class="fa-solid fa-book-open"></i>
                <span>课程</span>
              </li>
            </router-link>
            <router-link to="/work">
              <li>
                <i class="fa-solid fa-file-pen"></i>
                <span>作业</span>
              </li>
            </router-link>
            <router-link to="/AI">
              <li>
                <i class="fa-solid fa-chart-line"></i>
                <span>AI智能问答</span>
              </li>
            </router-link>
            <router-link to="/news">
              <li>
                <i class="fa-solid fa-comment-dots"></i>
                <span>消息</span>
              </li>
            </router-link>
          </ul>
          <div class="teaching-tools">
            <h3>教学工具</h3>
            <ul>
              <li v-for="(tool, index) in tools" :key="index" @click="openTool(tool)">
                <i :class="tool.icon"></i>
                <span>{{ tool.name }}</span>
              </li>
            </ul>
          </div>
        </nav>
      </aside>

      <!-- 主要内容区域 -->
      <main class="content">
        <div>
          <slot></slot>
        </div>
      </main>
    </div>

    <!-- 浮窗 -->
    <div v-if="activeTool" class="overlay" ref="overlayRef"
      @mousedown="activeTool.name === '投屏' && screenShareState.selecting ? onOverlayMouseDown($event) : null"
      style="user-select:none;">
      <div class="tool-popup">
        <div class="tool-popup-header">
          <h3>{{ activeTool.name }}</h3>
          <button class="close-button" @click="() => { closeTool(); resetScreenShare(); }">×</button>
        </div>
        <div class="tool-popup-body">
          <div v-if="activeTool.name === '投屏'">
            <i class="fa-solid fa-tv fa-3x tool-icon"></i>
            <div v-if="!screenShareState.region">
              <button class="action-button" @click="startSelectRegion" :disabled="screenShareState.selecting">选择区域</button>
              <span v-if="screenShareState.selecting" style="color:#888;">请在页面上拖动鼠标选择区域</span>
            </div>
            <div v-else>
              <div style="margin-bottom:10px; color:#4e6f82;">
                已选择区域：{{ screenShareState.region.width }} × {{ screenShareState.region.height }}
              </div>
              <button class="action-button" @click="startScreenShare">开启投屏</button>
              <button class="action-button" style="margin-left:10px;background:#eee;color:#666;" @click="resetScreenShare">重选区域</button>
            </div>
            <!-- 区域选择高亮框 -->
            <div v-if="screenShareState.region && (screenShareState.selecting || screenShareState.region.width>0)"
              class="select-rect"
              :style="{
                left: screenShareState.region.x + 'px',
                top: screenShareState.region.y + 'px',
                width: screenShareState.region.width + 'px',
                height: screenShareState.region.height + 'px',
                display: screenShareState.region.width>0 && screenShareState.region.height>0 ? 'block' : 'none'
              }"
            ></div>
          </div>
          <!-- <div v-if="activeTool.name === '直播'">
            <i class="fa-solid fa-video fa-3x tool-icon"></i>
            <button class="action-button" @click="startLiveStream">开启直播</button>
          </div> -->
          <div v-if="activeTool.name === '选人'">
            <i class="fa-solid fa-user fa-3x tool-icon"></i>
            <button class="action-button" @click="randomSelectStudent">随机抽人</button>
            <p v-if="selectedStudent">选中的学生: {{ selectedStudent }}</p>
            <button class="action-button" @click="confirmSelection">确定</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();
const router = useRouter();
const showDropdown = ref(false);
const activeTool = ref(null);
const selectedStudent = ref('');
const isSelecting = ref(false);
const areaSelecting = ref(false);
const selectedArea = ref(null);
const startX = ref(0);
const startY = ref(0);
const currentX = ref(0);
const currentY = ref(0);
const screenShareState = ref({
  selecting: false, // 是否正在选择区域
  region: null,     // {x, y, width, height}
  start: null,      // 鼠标按下起点
  stream: null      // 屏幕共享流
});

const overlayRef = ref(null);

// 获取用户信息
const Name = computed(() => localStorage.getItem('Name'));   
const userAvatar = computed(() => localStorage.getItem('userAvatar')); 
console.log(Name.value, userAvatar.value);

// 教学工具数组
const tools = [
  // { name: '发签到', icon: 'fa-solid fa-calendar-check' },
  { name: '投屏', icon: 'fa-solid fa-tv' },
  // { name: '直播', icon: 'fa-solid fa-video' },
  { name: '选人', icon: 'fa-solid fa-user' }
];

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const logout = () => {
  // 清除 localStorage 中的用户信息
  localStorage.removeItem('Name');
  localStorage.removeItem('userAvatar');
  userStore.logout();
  router.push('/');
};

const openTool = (tool) => {
  activeTool.value = tool;
};

const closeTool = () => {
  activeTool.value = null;
};

const startSelectRegion = () => {
  screenShareState.value.selecting = true;
  screenShareState.value.region = null;
  screenShareState.value.start = null;
};

const onOverlayMouseDown = (e) => {
  if (!screenShareState.value.selecting) return;
  const rect = overlayRef.value.getBoundingClientRect();
  screenShareState.value.start = {
    x: e.clientX - rect.left,
    y: e.clientY - rect.top
  };
  screenShareState.value.region = { x: 0, y: 0, width: 0, height: 0 };
  window.addEventListener('mousemove', onOverlayMouseMove);
  window.addEventListener('mouseup', onOverlayMouseUp);
};

const onOverlayMouseMove = (e) => {
  if (!screenShareState.value.selecting || !screenShareState.value.start) return;
  const rect = overlayRef.value.getBoundingClientRect();
  const x = e.clientX - rect.left;
  const y = e.clientY - rect.top;
  const sx = screenShareState.value.start.x;
  const sy = screenShareState.value.start.y;
  screenShareState.value.region = {
    x: Math.min(sx, x),
    y: Math.min(sy, y),
    width: Math.abs(x - sx),
    height: Math.abs(y - sy)
  };
};

const onOverlayMouseUp = () => {
  window.removeEventListener('mousemove', onOverlayMouseMove);
  window.removeEventListener('mouseup', onOverlayMouseUp);
  screenShareState.value.selecting = false;
};

const startScreenShare = async () => {
  try {
    const stream = await navigator.mediaDevices.getDisplayMedia({ video: true });
    screenShareState.value.stream = stream;
    alert('投屏已开启！');
    // 可在此处将 stream 传递给 video 元素或后续逻辑
  } catch (err) {
    alert('投屏失败: ' + err.message);
  }
};

const resetScreenShare = () => {
  screenShareState.value.region = null;
  screenShareState.value.selecting = false;
  screenShareState.value.start = null;
  if (screenShareState.value.stream) {
    screenShareState.value.stream.getTracks().forEach(t => t.stop());
    screenShareState.value.stream = null;
  }
};

const startLiveStream = () => {
  alert('直播已开启');
};

const randomSelectStudent = () => {
  const students = ['学生1', '学生2', '学生3', '学生4', '学生5'];
  selectedStudent.value = students[Math.floor(Math.random() * students.length)];
};

const confirmCheckIn = () => {
  alert('签到已确认');
};

const confirmSelection = () => {
  alert(`选中的学生: ${selectedStudent.value}`);
};

const startAreaSelection = () => {
  isSelecting.value = true;
  areaSelecting.value = true;
  selectedArea.value = null;
  startX.value = 0;
  startY.value = 0;
  currentX.value = 0;
  currentY.value = 0;
};

const onAreaMouseDown = (e) => {
  if (!areaSelecting.value) return;
  startX.value = e.clientX;
  startY.value = e.clientY;
  currentX.value = e.clientX;
  currentY.value = e.clientY;
};

const onAreaMouseMove = (e) => {
  if (!areaSelecting.value) return;
  currentX.value = e.clientX;
  currentY.value = e.clientY;
};

const onAreaMouseUp = () => {
  if (!areaSelecting.value) return;
  isSelecting.value = false;
  areaSelecting.value = false;
  const width = Math.abs(currentX.value - startX.value);
  const height = Math.abs(currentY.value - startY.value);
  selectedArea.value = { width, height };
};

const resetArea = () => {
  selectedArea.value = null;
};
</script>

<style scoped>
.main-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f5f5;
}

.header {
  height: 80px;
  background-image: linear-gradient(to right, #f0f4ff 0%, #e6f9ff 100%);
  color: rgb(78, 111, 130);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 100;
  padding: 0 20px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.user-menu {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: rgb(78, 111, 130);
}

.dropdown {
  position: relative;
}

.arrow-down {
  display: inline-block;
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 5px solid #fff;
  transition: transform 0.2s;
}

.dropdown-content {
  position: absolute;
  right: 0;
  top: 100%;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  padding: 8px 0;
  min-width: 120px;
  z-index: 100;
}

.dropdown-content div {
  padding: 8px 16px;
  cursor: pointer;
  transition: background 0.2s;
}

.dropdown-content div:hover {
  background: #f5f5f5;
}

.container {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.sidebar {
  width: 220px;
  background-image: linear-gradient(to bottom, #f0f4ff 0%, #e6f9ff 100%);
  color: rgb(78, 111, 130);
  overflow-y: auto;
  padding: 20px;
  /* 隐藏滚动条 */
  scrollbar-width: none;
  /* Firefox */
  -ms-overflow-style: none;
  /* Internet Explorer 10+ */
}

.sidebar::-webkit-scrollbar {
  display: none;
  /* Safari 和 Chrome */
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;

  color: rgb(78, 111, 130);

  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-bottom: 10px;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.avatar1 {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  margin-bottom: 10px;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.info {
  text-align: center;
  /* 文本居中 */
  font-weight: bold;
}

.username {
  color: rgb(78, 111, 130);
}

.nav-menu ul {
  list-style: none;
  padding: 10px;

}

.nav-menu li {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 20px;
  color: #4e6f82;
  text-decoration: none;
  /* 添加此行 */
  border-radius: 10px;
  margin: 6px 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  font-size: 15px;
}

.nav-menu li:hover {
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateX(6px);
}

.nav-menu li::before {
  content: "";
  position: absolute;
  left: -20px;
  top: 50%;
  transform: translateY(-50%);
  height: 24px;
  width: 4px;
  background: #8aa8c4;
  border-radius: 2px;
  opacity: 0;
  transition: all 0.3s ease;
}

.nav-menu li:hover::before,
.router-link-active li::before {
  opacity: 1;
  left: -12px;
}

.teaching-tools {
  margin-top: 20px;
  padding: 10px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  text-align: center;
}

.teaching-tools h3 {
  margin-bottom: 10px;
  color: rgb(78, 111, 130);
}

.teaching-tools ul {
  list-style: none;
  padding: 0;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.teaching-tools li {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 10px;
  color: #4e6f82;
  text-decoration: none;
  /* 添加此行 */
  border-radius: 10px;
  margin: 6px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  font-size: 14px;
  width: calc(30% - 2px);
  /* 两个教学工具在一行显示 */
}

.teaching-tools li i {
  font-size: 24px;
}

.teaching-tools li:hover {
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateX(6px);
}

.teaching-tools li::before {
  content: "";
  position: absolute;
  left: -20px;
  top: 50%;
  transform: translateY(-50%);
  height: 24px;
  width: 4px;
  background: #8aa8c4;
  border-radius: 2px;
  opacity: 0;
  transition: all 0.3s ease;
}

.teaching-tools li:hover::before {
  opacity: 1;
  left: -12px;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  z-index: 150;
  display: flex;
  justify-content: center;
  align-items: center;
}

.tool-popup {
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  z-index: 200;
  width: 400px;
  max-width: 90%;
  padding: 20px;
  position: relative;
}

.tool-popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.tool-popup-header h3 {
  margin: 0;
  color: rgb(78, 111, 130);
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
}

.close-button:hover {
  color: #666;
}

.tool-popup-body {
  margin-top: 20px;
  text-align: center;
}

.tool-icon {
  display: flex;
  flex-direction: column;
  color: #8aa8c4;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
  width: 100%;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.action-button {
  margin-top: 20px;
  padding: 10px 20px;
  background: rgb(123, 175, 203);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.action-button:hover {
  background: rgb(140, 199, 231);
}

.iconfont {
  font-size: 18px;
  width: 24px;
  text-align: center;
}

.router-link-active li {
  color: rgb(78, 111, 130);
  font-weight: 500;
}

.content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  background: #fff;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
  margin: 16px;
  border-radius: 12px;
}

.blurred {
  filter: blur(5px);
}

/* 投屏区域选择样式 */
.area-select-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 250;
  cursor: crosshair;
}

.area-select-rect {
  position: absolute;
  border: 2px dashed rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.3);
  pointer-events: none;
}

.select-rect {
  position: fixed;
  border: 2px dashed #4e6f82;
  background: rgba(123,175,203,0.08);
  pointer-events: none;
  z-index: 9999;
}
</style>
