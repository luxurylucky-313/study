<script setup>
import { useRouter, useRoute } from 'vue-router';
import { ref, onMounted } from 'vue';
import { courseApi } from '@/api/course_api'; // Import the API functions

const route = useRoute();
const router = useRouter();

// State to hold course details
const courseDescription = ref('');
const courseName = ref('');
const className = ref('');
const startTime = ref('');
const endTime = ref('');
const courseImage = ref('');
const courseId = ref(null);

// State to control modal visibility
const isModalVisible = ref(false);

// State to hold uploaded image file
const imageFile = ref(null);
const imagePreview = ref(''); // For displaying the image preview

// Fetch course details
const fetchCourseDetail = async () => {
  const id = route.params.id;
  courseId.value = id;
  try {
    const response = await courseApi.getCourseById(id);
    const course = response.data;

    // Assign course data to component variables
    courseName.value = course.courseName;
    className.value = course.className;
    startTime.value = course.startTime;
    endTime.value = course.endTime;
    courseDescription.value = course.description;
    courseImage.value = course.image;
    console.log('Image path:', course.image);
  } catch (error) {
    console.error('Fetching course details failed:', error);
  }
};

// Handle save operation in modal
const saveCourse = async () => {
  if (imageFile.value) {
    // If an image is selected, upload it first
    try {
      const uploadResponse = await courseApi.uploadImage(courseId.value, imageFile.value);
      courseImage.value = uploadResponse.data.imagePath; // Use the new image path from the response
      console.log(uploadResponse.data); // Log the upload result
    } catch (error) {
      console.error('Image upload failed:', error);
      alert('图片上传失败');
      return;
    }
  }

  // Prepare updated course data
  const updatedCourse = {
    id: courseId.value,
    courseName: courseName.value,
    className: className.value,
    startTime: startTime.value,
    endTime: endTime.value,
    description: courseDescription.value,
    image: courseImage.value, // Use the updated image path
  };

  try {
    // Update course information using the API
    await courseApi.updateCourse(courseId.value, updatedCourse);
    alert('Course updated successfully!');
    isModalVisible.value = false; // Close the modal
  } catch (error) {
    console.error('Error updating course:', error);
    alert('Failed to update course.');
  }
};

// Go back to the Home page
const goBack = () => {
  router.push({ name: 'Home' });
};

// Image upload preview handler
const handleImageUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    imageFile.value = file;

    // Create a preview of the selected image
    const reader = new FileReader();
    reader.onload = (e) => {
      imagePreview.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

onMounted(() => {
  fetchCourseDetail();
});
</script>

<template>
  <div class="course-detail">
    <header class="header">
      <h2>{{ courseName }}</h2>
      <div class="header-buttons">
        <button @click="goBack" class="back-btn">返回</button>
        <button @click="isModalVisible = true" class="edit-btn">修改</button>
      </div>
    </header>

    <div class="course-info">
      <div class="image-container">
        <img :src="courseImage" alt="课程封面" class="course-image" />
      </div>
      <div class="info-container">
        <h3 class="section-title">课程介绍</h3>
        <p class="description">{{ courseDescription }}</p>
        <div class="course-details">
          <p><strong>班级：</strong>{{ className }}</p>
          <p><strong>开始时间：</strong>{{ startTime }}</p>
          <p><strong>结束时间：</strong>{{ endTime }}</p>
        </div>
      </div>
    </div>

    <!-- Modal for editing course details -->
    <div v-if="isModalVisible" class="modal-overlay">
      <div class="modal">
        <h3 class="modal-title">编辑课程</h3>
        <div class="modal-body">
          <div class="form-group">
            <label for="courseName">课程名称：</label>
            <input v-model="courseName" id="courseName" type="text" />
          </div>
          <div class="form-group">
            <label for="className">班级：</label>
            <input v-model="className" id="className" type="text" />
          </div>
          <div class="form-group">
            <label for="startTime">开始时间：</label>
            <input v-model="startTime" id="startTime" type="datetime-local" />
          </div>
          <div class="form-group">
            <label for="endTime">结束时间：</label>
            <input v-model="endTime" id="endTime" type="datetime-local" />
          </div>
          <div class="form-group">
            <label for="courseDescription">课程介绍：</label>
            <textarea v-model="courseDescription" id="courseDescription"></textarea>
          </div>
          <div class="form-group image-upload">
            <label for="courseImage">课程封面图片：</label>
            <div class="upload-container">
              <img v-if="imagePreview" :src="imagePreview" alt="预览" class="image-preview" />
              <input type="file" id="courseImage" @change="handleImageUpload" />
              <span v-if="!imagePreview" class="upload-text">选择图片</span>
            </div>
          </div>
        </div>

        <div class="modal-actions">
          <button @click="saveCourse" class="save-btn">保存</button>
          <button @click="isModalVisible = false" class="cancel-btn">取消</button>
        </div>
      </div>
    </div>
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
</style>
