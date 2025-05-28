<template>
  <div class="login-container">
    <ParticlesBackground />
    <div class="login-wrapper">
      <div class="login-left">
        <h3>智慧校园学习平台</h3>
        <p>知识 · 互动 · 成长</p>
        <!-- <div class="role-selector">
          <label class="role-option">
            <input type="checkbox" v-model="roles" value="student" />
            <span class="role-text">学生</span>
          </label>
          <label class="role-option">
            <input type="checkbox" v-model="roles" value="teacher" />
            <span class="role-text">教师</span>
          </label>
        </div> -->
      </div>
      <div class="login-right">
        <div class="login-box">
          <h3>用户登录</h3>
          <form @submit.prevent="handleLogin">
            <div class="input-group">
              <span class="icon-user"><i class="fa fa-user"></i></span>
              <input 
                type="text" 
                v-model="username" 
                placeholder="请输入用户名"
              >
            </div>
            <div class="input-group">
              <span class="icon-password"><i class="fa fa-lock"></i></span>
              <input
                :type="showPassword ? 'text' : 'password'"
                v-model="password"
                placeholder="请输入密码"
              >
              <i 
                class="toggle-password"
                :class="showPassword ? 'fa fa-eye' : 'fa fa-eye-slash'"
                @click="showPassword = !showPassword"
              ></i>
            </div>
            <button 
              type="submit" 
              :class="['login-btn']"
            >
              登录
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { teacherApi } from '@/api/teacher_api'

const router = useRouter()
const userStore = useUserStore()
const username = ref('')
const password = ref('')
const showPassword = ref(false)

const isFormValid = computed(() => {
  return username.value.length >= 1 && password.value.length >= 1
})

const handleLogin = async () => {
  if (!isFormValid.value) {
    alert('请输入有效的用户名和密码');
    return;
  }

  try {
    const response = await teacherApi.login(username.value, password.value);
    console.log('登录响应:', response); // 调试日志
    if (response.code === 200 && response.data) {
      const token = response.data;
      localStorage.setItem('userToken', token);
      const userInfoResp = await teacherApi.searchTeachers(username.value);
      if (userInfoResp.code === 200 && userInfoResp.data && userInfoResp.data.length > 0) {
        const userInfo = userInfoResp.data[0];
        userStore.login({
          id: userInfo.id,
          name: userInfo.name,
          username: userInfo.username,
          avatar: userInfo.avatar || '/src/assets/images/default-avatar.jpg',
          gender: userInfo.gender,
          phone: userInfo.phone,
        });
        localStorage.setItem('Name', userInfo.name);
        localStorage.setItem('userAvatar', userInfo.avatar || '/src/assets/images/default-avatar.jpg');
        router.push('/home');
      } else {
        alert('登录成功，但未能获取用户信息');
      }
    } else {
      alert(response.message || '登录失败，请检查用户名或密码');
    }
  } catch (error) {
    console.error('登录请求失败:', error);
    alert('登录失败，请稍后再试');
  }
};
</script>

<style scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(225deg, #c9d9b4, #b0cbe1, #cefff7);
  background-size: 400% 400%;
  animation: gradientAnimation 15s ease infinite;
  display: flex;
  justify-content: center;
  align-items: center;
}

@keyframes gradientAnimation {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.login-wrapper {
  width: 80%;
  max-width: 1200px;
  height: 600px;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  display: flex;
  overflow: hidden;
}

.login-left {
  flex: 1;
  background-image:url("@/assets/images/background1.jpg");
  padding: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.login-left h3 {
  font-size: 32px;
  color: #333;
  margin-bottom: 20px;
}

.login-left p {
  font-size: 18px;
  color: #666;
}

.role-selector {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.role-option {
  display: flex;
  align-items: center;
  font-size: 16px;
  color: #333;
  cursor: pointer;
  transition: color 0.3s ease, transform 0.3s ease;
}

.role-option input {
  display: none;
}

.role-option .role-text {
  padding: 10px 20px;
  border: 2px solid #ddd;
  border-radius: 20px;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.role-option input:checked + .role-text {
  background-color: #1c92d2;
  color: #fff;
  border-color: #1c92d2;
}

.role-option:hover .role-text {
  transform: scale(1.05);
}

.login-right {
  flex: 1;
  padding: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 100%;
  max-width: 400px;
}

.login-box h3 {
  font-size: 24px;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

.input-group {
  position: relative;
  margin-bottom: 20px;
}

.input-group span {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
}

.input-group input {
  width: 87%;
  padding: 12px 12px 12px 40px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.input-group input:focus {
  border-color: #1c92d2;
  outline: none;
  box-shadow: 0 0 5px rgba(28, 146, 210, 0.5);
  transition: box-shadow 0.3s ease;
}

.toggle-password {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #1c92d2;
  cursor: pointer;
  font-size: 14px;
}

.login-btn {
  width: 100%;
  padding: 12px;
  background:  #77c1e9;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 20px;
  transition: background 0.3s ease, transform 0.3s ease;
}

.login-btn:hover {
  background: #bfdbea;
  transform: scale(1.05);
}

.login-btn.disabled {
  background: #ccc;
  cursor: not-allowed;
}

.login-footer {
  margin-top: 20px;
  text-align: center;
  color: #666;
}

.login-footer a {
  color: #1c92d2;
  text-decoration: none;
}

.login-footer a:hover {
  text-decoration: underline;
}
</style>
