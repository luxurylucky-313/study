<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router'; // 引入 useRouter
import { courseApi } from '@/api/course_api'; // 假设你的 course_api.js 文件位于 src/api/ 目录下

const props = defineProps({
  course: Object  // 确保接收到整个课程对象
});

const router = useRouter();

const goToCourseDetail = (courseId) => {
  router.push({ name: 'CourseDetail', params: { id: courseId } });
};
</script>

<template>
  <div class="course-card" @click="goToCourseDetail(course.id)">
    <img :src="course.image" alt="课程封面" class="course-image" />

    <h3>{{ course.courseName }}</h3> <!-- 显示课程名称 -->
    <p><strong>班级：</strong>{{ course.className }}</p> <!-- 显示班级名称 -->
    <p><strong>开始时间：</strong>{{ course.startTime }}</p> <!-- 显示开始时间 -->
    <p><strong>结束时间：</strong>{{ course.endTime }}</p> <!-- 显示结束时间 -->
    <p><strong>描述：</strong>{{ course.description }}</p> <!-- 显示课程描述 -->
  </div>
</template>

<style scoped>
.course-card {
  width: 300px;
  background: white;
  padding: 20px;
  border-radius: 10px;
  color: #333;
  margin: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  cursor: pointer;
}

.course-card:hover {
  transform: scale(1.05);
}

.course-card img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 10px;
}

.course-card h3 {
  font-size: 18px;
  margin: 10px 0;
  color: #4e6f82;
}

.course-card p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}
</style>
