<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { courseApi } from '@/api/course_api'; // 假设你的 course_api.js 文件位于 src/api/ 目录下
import MainLayout from '@/components/MainLayout.vue';
import CourseCard from '@/components/CourseCard.vue';

const router = useRouter();

const courses = ref([]); // 存放课程数据
const courseName = ref(''); // 课程名称输入框的绑定变量
const className = ref('');  // 班级名称输入框的绑定变量

// 搜索课程的函数
const searchCourses = async () => {
  console.log("Searching for:", courseName.value, className.value);
  try {
    // 调用后端的搜索接口
    const response = await courseApi.searchCourses(courseName.value, className.value);
    
    // 检查响应是否成功
    if (response.code === 200) {
      // 从 response.data 中获取课程数据
      courses.value = response.data || [];
    } else {
      console.error('搜索失败:', response.msg || '未知错误');
      courses.value = []; // 清空当前课程数据
    }
  } catch (error) {
    console.error('搜索课程失败:', error);
    courses.value = []; // 清空当前课程数据
  }
};

// 在组件挂载时获取课程数据
onMounted(() => {
  // 初始加载所有课程
  searchCourses();
});

// 处理跳转到课程详情页
const viewCourseDetail = (courseId) => {
  router.push({ name: 'CourseDetail', params: { id: courseId } });
};
</script>

<template>
  <MainLayout>
    <div class="home">
      <header>
        <h1>我的课程</h1>
        <div class="controls">
          <div class="search">
            <input 
              type="text" 
              v-model="courseName" 
              placeholder="搜索课程名" 
              class="search-input" 
              @keyup.enter="searchCourses" 
            />
            <input 
              type="text" 
              v-model="className" 
              placeholder="搜索班级名" 
              class="search-input" 
              @keyup.enter="searchCourses" 
            />
            <button @click="searchCourses" class="search-btn">查询</button>
          </div>
        </div>
      </header>

      <!-- 课程列表展示 -->
      <div class="course-list">
        <div class="course-item" v-for="course in courses" :key="course.id" @click="viewCourseDetail(course.id)">
          <CourseCard :course="course" />  <!-- 传递课程数据 -->
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<style lang="scss" scoped>
.home {
  max-width: 1200px;
  margin: auto;
  padding: 20px;
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding: 10px;
}

h1 {
  color: #333;
  font-size: 28px;
  margin: 0;
}

.controls {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search {
  display: flex;
  gap: 10px;
}

.search-input {
  padding: 10px 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 16px;
  width: 200px;
  transition: all 0.3s ease;
}

.search-btn {
  padding: 10px 20px;
  background-color: #bfdbea;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: white;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.search-btn:hover {
  background-color: #d9eaf3;
}

.course-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  padding: 20px 0;
}

.course-item {
  cursor: pointer;
}

@media (max-width: 768px) {
  header {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }
  
  .controls {
    flex-direction: column;
    align-items: flex-start;
  }

  .search {
    flex-direction: column;
    align-items: flex-start;
  }

  .search-input {
    width: 100%;
  }

  .course-list {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  }
}
</style>
