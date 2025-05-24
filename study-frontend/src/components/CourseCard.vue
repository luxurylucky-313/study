<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { courseApi } from '@/api/course_api';
import { ElMessageBox, ElMessage } from 'element-plus';

const props = defineProps({
  course: Object
});

const router = useRouter();

const formatDate = (dateString) => {
  if (!dateString) return '';
  try {
    const date = new Date(dateString);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch {
    return dateString; // 如果格式化失败，返回原始字符串
  }
};

const goToCourseDetail = (courseId) => {
  router.push({ name: 'CourseDetail', params: { id: courseId } });
};

const handleImageError = (e) => {
  e.target.src = '/src/assets/images/course1.jpg'; // 设置默认图片路径
};
const emit = defineEmits(['refresh']); // 定义事件

const deleteCourse = async (event) => {
  // 阻止事件冒泡，避免触发卡片的点击事件
  event.stopPropagation();
  
  try {
    await ElMessageBox.confirm(
      '确定要删除这个课程吗？此操作不可恢复',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );

    const response = await courseApi.deleteCourse(props.course.id);
    if (response.data.code === 200) {
      ElMessage.success(response.data.message || '删除成功');
      emit('refresh'); // 通知父组件刷新列表
    } else {
      ElMessage.error(response.data.message || '删除失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除课程失败:', error);
      ElMessage.error('删除失败');
    }
  }
};
</script>

<template>
  <div class="course-card" @click="goToCourseDetail(course.id)">
<div class="course-header">
      <img 
        :src="course.image || '/src/assets/images/course1.jpg'"
        :alt="course.courseName" 
        class="course-image"
        @error="handleImageError"
      />
      <button class="delete-btn" @click="deleteCourse">
        <i class="el-icon-delete">删除</i>
      </button>
    </div>
    <div class="course-content">
      <h3 class="course-title">{{ course.courseName }}</h3>
      <div class="course-info">
        <p class="class-name">
          <span class="label">班级：</span>
          <span>{{ course.className }}</span>
        </p>
        <p class="time">
          <span class="label">开始时间：</span>
          <span>{{ formatDate(course.startTime) }}</span>
        </p>
        <p class="time">
          <span class="label">结束时间：</span>
          <span>{{ formatDate(course.endTime) }}</span>
        </p>
        <p class="description">
          <span class="label">描述：</span>
          <span>{{ course.description || '暂无描述' }}</span>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.course-card {
  width: 300px;
  background: white;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
  cursor: pointer;
}

.course-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.course-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 8px;
}

.course-content {
  padding: 15px 0;
}

.course-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.course-info p {
  margin: 8px 0;
  font-size: 14px;
  color: #666;
}

.label {
  font-weight: 500;
  color: #4a5568;
  margin-right: 5px;
}

.description {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  color: #718096;
  line-height: 1.5;
}
</style>
