<script setup>
import { useRouter, useRoute } from 'vue-router';
import { ref, onMounted } from 'vue';
import { courseApi } from '@/api/course_api';

const route = useRoute();
const router = useRouter();
const resourceFile = ref(null);
const uploadProgress = ref(false);

// 课程详情状态
const courseDescription = ref('');
const courseName = ref('');
const className = ref('');
const startTime = ref('');
const endTime = ref('');
const courseImage = ref('');
const courseId = ref(null);
const resourceUrl = ref('');
const loading = ref(true);
const error = ref('');

// 模态框状态
const isModalVisible = ref(false);
const imageFile = ref(null);
const imagePreview = ref('');

// 获取课程详情
const fetchCourseDetail = async () => {
  const id = route.params.id;
  courseId.value = id;
  loading.value = true;
  error.value = '';
  
  try {
    const response = await courseApi.getCourseById(id);
    console.log('课程详情响应:', response.data);
    
    if (response.data.code === 200 && response.data) {
      const course = response.data.data;
      courseName.value = course.courseName;
      className.value = course.className;
      startTime.value = formatDateTime(course.startTime);
      endTime.value = formatDateTime(course.endTime);
      courseDescription.value = course.description;
      courseImage.value = course.image || '/src/assets/images/course1.jpg';
      resourceUrl.value = course.resourceUrl || '无资源链接';
    } else {
      error.value = '获取课程详情失败';
      console.error('获取课程详情失败:', response);
    }
  } catch (err) {
    error.value = '获取课程详情出错';
    console.error('获取课程详情异常:', err);
  } finally {
    loading.value = false;
  }
};

// 时间格式化函数
const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return '';
  const date = new Date(dateTimeString);
  return date.toISOString().slice(0, 16);
};

const formatDisplayTime = (dateTimeString) => {
  if (!dateTimeString) return '未设置';
  return new Date(dateTimeString).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 保存课程信息
const saveCourse = async () => {
  try {
    if (imageFile.value) {
      const uploadResponse = await courseApi.uploadImage(courseId.value, imageFile.value);
      if (uploadResponse.code === 200) {
        courseImage.value = uploadResponse.data.imagePath;
      } else {
        throw new Error('图片上传失败');
      }
    }

    const updatedCourse = {
      id: courseId.value,
      courseName: courseName.value,
      className: className.value,
      startTime: startTime.value,
      endTime: endTime.value,
      description: courseDescription.value,
      image: courseImage.value,
    };
    console.log('更新课程信息:', updatedCourse);
    const response = await courseApi.updateCourse(courseId.value, updatedCourse);
    if (response.data.code === 200) {
      alert('课程更新成功！');
      isModalVisible.value = false;
      fetchCourseDetail(); // 重新获取课程信息
    } else {
      throw new Error(response.message || '更新失败');
    }
  } catch (error) {
    console.error('更新课程失败:', error);
    alert(error.message || '更新课程失败，请重试');
  }
};

// 返回首页
const goBack = () => {
  router.push({ name: 'Home' });
};

// 处理图片上传
const handleImageUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    imageFile.value = file;
    const reader = new FileReader();
    reader.onload = (e) => {
      imagePreview.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

// 图片加载错误处理
const handleImageError = (e) => {
  e.target.src = '/src/assets/images/course1.jpg';
};

// 页面加载时获取课程详情
onMounted(() => {
  fetchCourseDetail();
});

// 处理资源文件上传
const handleResourceUpload = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // 文件类型检查
  const allowedTypes = [
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document', // .docx
    'application/vnd.openxmlformats-officedocument.presentationml.presentation', // .pptx
    'application/pdf', // .pdf
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' // .xlsx
  ];

  if (!allowedTypes.includes(file.type)) {
    ElMessage.error('只支持 Word、PPT、PDF、Excel 等办公文件格式');
    return;
  }

  // 文件大小检查（50MB）
  if (file.size > 50 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过 50MB');
    return;
  }

  try {
    uploadProgress.value = true;
    const response = await courseApi.uploadResource(courseId.value, file);
    
    if (response.data.code === 200) {
      ElMessage.success(response.data.message || '资源上传成功');
      await fetchCourseDetail(); // 重新获取课程信息以更新资源链接
    } else {
      throw new Error(response.data.message || '上传失败');
    }
  } catch (error) {
    console.error('上传资源失败:', error);
    ElMessage.error(error.message || '上传资源失败');
  } finally {
    uploadProgress.value = false;
    // 清空文件输入框
    event.target.value = '';
  }
};
</script>

<template>
  <div class="course-detail">
    <div v-if="loading" class="loading">
      加载中...
    </div>

    <div v-else-if="error" class="error-message">
      <p>{{ error }}</p>
      <button @click="fetchCourseDetail" class="retry-btn">重试</button>
    </div>

    <template v-else>
      <header class="header">
        <h2>{{ courseName }}</h2>
        <div class="header-buttons">
          <button @click="goBack" class="back-btn">返回</button>
          <button @click="isModalVisible = true" class="edit-btn">修改</button>
        </div>
      </header>

      <div class="course-info">
        <div class="image-container">
          <img 
            :src="courseImage" 
            :alt="courseName"
            class="course-image"
            @error="handleImageError"
          />
        </div>
        
        <div class="info-container">
          <h3 class="section-title">课程介绍</h3>
          <p class="description">{{ courseDescription || '暂无介绍' }}</p>
          <div class="course-details">
            <p><strong>班级：</strong>{{ className }}</p>
            <p><strong>开始时间：</strong>{{ formatDisplayTime(startTime) }}</p>
            <p><strong>结束时间：</strong>{{ formatDisplayTime(endTime) }}</p>
            <p><strong>课程资源：</strong>{{ resourceUrl }}</p>
          </div>
        </div>
        <div class="resource-section">
          <h3 class="section-title">课程资源</h3>
          <div class="resource-upload">
            <input 
              type="file"
              accept=".docx,.pptx,.pdf,.xlsx"
              @change="handleResourceUpload"
              :disabled="uploadProgress"
              class="resource-input"
              id="resource-upload"
            />
            <label for="resource-upload" class="upload-label">
              {{ uploadProgress ? '上传中...' : '选择文件' }}
            </label>
          </div>
          
          <!-- 显示当前资源链接 -->
          <div v-if="course?.resourceUrl" class="resource-link">
            <a :href="course.resourceUrl" target="_blank" class="download-link">
              下载课程资源
            </a>
          </div>
        </div>        
      </div>

      <!-- 编辑模态框 -->
      <div v-if="isModalVisible" class="modal-overlay" @click.self="isModalVisible = false">
        <div class="modal">
          <h3 class="modal-title">编辑课程信息</h3>
          <div class="modal-body">
            <div class="form-group">
              <label>课程名称</label>
              <input type="text" v-model="courseName" />
            </div>
            <div class="form-group">
              <label>班级</label>
              <input type="text" v-model="className" />
            </div>
            <div class="form-group">
              <label>开始时间</label>
              <input type="datetime-local" v-model="startTime" />
            </div>
            <div class="form-group">
              <label>结束时间</label>
              <input type="datetime-local" v-model="endTime" />
            </div>
            <div class="form-group">
              <label>课程介绍</label>
              <textarea v-model="courseDescription"></textarea>
            </div>
            <div class="image-upload">
              <label>课程图片</label>
              <div class="upload-container">
                <img 
                  :src="imagePreview || courseImage" 
                  class="image-preview"
                  @error="handleImageError"
                />
                <input 
                  type="file" 
                  accept="image/*"
                  @change="handleImageUpload"
                />
              </div>
            </div>
          </div>
          <div class="modal-actions">
            <button @click="saveCourse" class="save-btn">保存</button>
            <button @click="isModalVisible = false" class="cancel-btn">取消</button>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<style scoped>
.course-detail {
  padding: 30px;
  max-width: 1200px;
  margin: 20px auto;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  font-size: 2rem;
  color: #333;
  font-weight: 600;
}

.header-buttons {
  display: flex;
  gap: 12px;
}

button {
  padding: 12px 24px;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
}

.back-btn {
  background-color: #f4f4f4;
  color: #333;
  border: 1px solid #ddd;
}

.back-btn:hover {
  background-color: #f0f0f0;
}

.edit-btn {
  background-color: #66b1ff;
  color: #ffffff;
}

.edit-btn:hover {
  background-color: #4a91d1;
}

.course-info {
  display: flex;
  gap: 30px;
}

.image-container {
  flex: 1;
}

.course-image {
  width: 100%;
  max-width: 400px;
  height: 250px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.info-container {
  flex: 2;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #4e6f82;
  margin-bottom: 15px;
}

.description {
  font-size: 1rem;
  color: #555;
  margin-bottom: 20px;
}

.course-details {
  font-size: 1rem;
  color: #333;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: white;
  padding: 30px;
  border-radius: 8px;
  width: 80%;
  max-width: 600px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transform: translateY(50px);
  animation: fadeIn 0.3s ease-out forwards;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
    transform: translateY(30px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-title {
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 20px;
}

.modal-body {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
}

input,
textarea {
  width: 100%;
  padding: 12px;
  margin-top: 8px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  box-sizing: border-box;
}

textarea {
  min-height: 100px;
  resize: vertical;
}

.image-upload {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.upload-container {
  display: flex;
  gap: 10px;
  align-items: center;
}

.image-preview {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
}

.upload-text {
  font-size: 1rem;
  color: #888;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
}

.save-btn,
.cancel-btn {
  padding: 12px 24px;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  width: 48%;
}

.save-btn {
  background-color: #66b1ff;
  color: white;
}

.save-btn:hover {
  background-color: #4a91d1;
}

.cancel-btn {
  background-color: #f4f4f4;
  color: #333;
  border: 1px solid #ddd;
}

.cancel-btn:hover {
  background-color: #f0f0f0;
}
.resource-section {
  margin-top: 30px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.resource-upload {
  margin: 15px 0;
}

.resource-input {
  display: none;
}

.upload-label {
  display: inline-block;
  padding: 10px 20px;
  background-color: #66b1ff;
  color: white;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.upload-label:hover {
  background-color: #4a91d1;
}

.resource-link {
  margin-top: 15px;
}

.download-link {
  display: inline-flex;
  align-items: center;
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.download-link:hover {
  background-color: #45a049;
}

/* 禁用状态样式 */
.upload-label[disabled] {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
