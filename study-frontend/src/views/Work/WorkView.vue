<template>
  <MainLayout>
    <div class="work-container">
      <div class="controls fixed-controls">
        <h2>已布置的作业</h2>
        <hr>
        <div class="dropdown">
          <button class="add-btn" @click="toggleClassDropdown">
            {{ selectedClass ? selectedClass : '选择班级' }}
          </button>
          <ul v-if="showClassDropdown" class="dropdown-menu">
            <li v-for="className in uniqueClassNames" :key="className" @click="selectClass(className)">
              {{ className }}
            </li>
          </ul>
        </div>
        <div class="dropdown">
          <button class="add-btn" @click="toggleCourseDropdown">
            {{ selectedCourse ? selectedCourse : '选择课程' }}
          </button>
          <ul v-if="showCourseDropdown" class="dropdown-menu">
            <li v-for="courseName in uniqueCourseNames" :key="courseName" @click="selectCourse(courseName)">
              {{ courseName }}
            </li>
          </ul>
        </div>
        <button class="add-btn" @click="addAssignment">添加作业</button>
      </div>
      <div class="assignment-list">
        <ul>
          <li v-for="assignment in filteredAssignments" :key="assignment.id" @click="viewWorkDetail(assignment.id)">
            <div class="assignment-item-content">
              <strong>{{ assignment.className }} - {{ assignment.courseName }}</strong>
              <div class="assignment-meta">
                <span><i class="fa-solid fa-book-open"></i> {{ assignment.courseName }}</span>
                <span><i class="fa-solid fa-users"></i> {{ assignment.className }}</span>
              </div>
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: Math.random() * 100 + '%' }"></div>
              </div>
            </div>
            <svg class="progress-ring">
              <circle class="progress-ring-bg" cx="20" cy="20" r="16" />
              <circle class="progress-ring-fill" cx="20" cy="20" r="16"
                :stroke-dashoffset="100 - Math.random() * 100" />
            </svg>
          </li>
        </ul>
        <div v-if="filteredAssignments.length === 0">
          <p>暂无作业数据。</p>
        </div>
      </div>
      <div v-if="showAddAssignmentModal" class="modal">
        <div class="modal-content">
          <h2>添加作业</h2>
          <div class="modal-controls">
            <button class="add-btn" @click="selectRecommended">智能推荐</button>
            <button class="add-btn" @click="selectManual">手动导入</button>
          </div>
          <div v-if="showRecommended" class="recommended-assignments">
            <h3>智能推荐的作业</h3>
            <ul>
              <li v-for="assignment in recommendedAssignments" :key="assignment.id">
                <strong>{{ assignment.title }}</strong> - {{ assignment.courseName }}
                <button class="add-btn" @click="confirmAddRecommendedAssignment(assignment)">选择</button>
              </li>
            </ul>
          </div>
          <div v-if="showManual" class="manual-assignment">
            <h3>手动导入作业</h3>
            <input type="text" v-model="manualTitle" placeholder="作业标题" />
            <textarea v-model="manualDescription" placeholder="作业描述"></textarea>
            <button class="add-btn" @click="confirmAddManualAssignment">添加</button>
          </div>
          <button class="close-btn" @click="closeModal">关闭</button>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getAllHomework, addHomework, deleteHomework } from '@/api/homework_api'; // 导入API函数
import MainLayout from '@/components/MainLayout.vue';

const router = useRouter();

// 数据模型
const assignments = ref([]);
const showClassDropdown = ref(false);
const showCourseDropdown = ref(false);
const showAddAssignmentModal = ref(false);
const showRecommended = ref(false);
const showManual = ref(false);
const selectedClass = ref(null);
const selectedCourse = ref(null);
const manualTitle = ref('');
const manualDescription = ref('');

// 获取作业数据
const fetchAssignments = async () => {
  try {
    const response = await getAllHomework(); // 调用 API 获取作业数据
    console.log('API Response:', response); // 调试日志

    // 检查响应格式是否正确
    if (response && response.code === 200 && Array.isArray(response.data)) {
      assignments.value = response.data; // 提取 data 字段中的数组
    } else {
      console.error('API 返回的数据格式不正确:', response);
      alert('无法加载作业数据，请检查后端服务！');
    }
  } catch (error) {
    console.error("获取作业数据失败:", error);
    alert('无法加载作业数据，请稍后再试！');
  }
};

onMounted(() => {
  console.log('Component mounted, fetching assignments...');
  fetchAssignments(); // 组件挂载时获取作业数据
});

// 提取唯一的班级名
const uniqueClassNames = computed(() => {
  const classNames = new Set();
  assignments.value.forEach(assignment => {
    classNames.add(assignment.className.trim());
  });
  return Array.from(classNames);
});

// 提取唯一的课程名
const uniqueCourseNames = computed(() => {
  const courseNames = new Set();
  assignments.value.forEach(assignment => {
    courseNames.add(assignment.courseName.trim());
  });
  return Array.from(courseNames);
});

// 过滤作业
const filteredAssignments = computed(() => {
  let filtered = assignments.value;
  if (selectedClass.value) {
    filtered = filtered.filter(assignment => assignment.className === selectedClass.value);
  }
  if (selectedCourse.value) {
    filtered = filtered.filter(assignment => assignment.courseName === selectedCourse.value);
  }
  return filtered;
});

// 智能推荐的作业
const recommendedAssignments = ref([
  { id: 6, title: "推荐作业1", courseName: "高级数据库" },
  { id: 7, title: "推荐作业2", courseName: "Web系统开发与设计" },
]);

function toggleClassDropdown() {
  showClassDropdown.value = !showClassDropdown.value;
}

function toggleCourseDropdown() {
  showCourseDropdown.value = !showCourseDropdown.value;
}

function selectClass(className) {
  selectedClass.value = className;
  showClassDropdown.value = false;
}

function selectCourse(courseName) {
  selectedCourse.value = courseName;
  showCourseDropdown.value = false;
}

function addAssignment() {
  showAddAssignmentModal.value = true;
}

function selectRecommended() {
  showRecommended.value = true;
  showManual.value = false;
}

function selectManual() {
  showRecommended.value = false;
  showManual.value = true;
}

function confirmAddRecommendedAssignment(assignment) {
  if (confirm(`确定要添加作业 "${assignment.title}" 吗？`)) {
    const newAssignment = {
      title: assignment.title,
      courseName: assignment.courseName,
      className: selectedClass.value,
    };
    addNewAssignment(newAssignment); // 调用 API 添加作业
  }
}

async function addNewAssignment(newAssignment) {
  try {
    await addHomework(newAssignment); // 调用 API 添加作业
    fetchAssignments(); // 刷新作业列表
    closeModal();
    alert('作业添加成功！');
  } catch (error) {
    console.error("添加作业失败:", error);
    alert('作业添加失败，请稍后再试！');
  }
}

function confirmAddManualAssignment() {
  if (manualTitle.value && manualDescription.value) {
    const newAssignment = {
      title: manualTitle.value,
      description: manualDescription.value,
      courseName: selectedCourse.value,
      className: selectedClass.value,
    };
    addNewAssignment(newAssignment); // 调用 API 添加作业
    manualTitle.value = '';
    manualDescription.value = '';
  } else {
    alert('请输入作业标题和描述');
  }
}

function closeModal() {
  showAddAssignmentModal.value = false;
  showRecommended.value = false;
  showManual.value = false;
}

function viewWorkDetail(workId) {
  router.push({ name: 'WorkDetail', params: { id: workId } });
}

async function deleteAssignment(id) {
  if (confirm(`确定要删除作业吗？`)) {
    try {
      await deleteHomework(id); // 调用 API 删除作业
      fetchAssignments(); // 刷新作业列表
      alert('作业删除成功！');
    } catch (error) {
      console.error("删除作业失败:", error);
      alert('作业删除失败，请稍后再试！');
    }
  }
}
</script>

<style scoped>
.work-container {
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

.fixed-controls {
  position: sticky;
  top: 0;
  z-index: 1000;
  background: white;
  padding: 10px 0;
}

.controls {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.dropdown {
  position: relative;
}

.dropdown button.add-btn {
  background-color: #bfdbea;
  border: 2px solid white;
  color: white;
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    background-color: #d9eaf3;
    transform: translateY(-2px);
  }
}

.dropdown-menu {
  position: absolute;
  left: 10px;
  top: 100%;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(8px);
  min-width: 220px;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(78, 111, 130, 0.1);
  overflow: hidden;
  transform-origin: top;
  animation: scaleIn 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

@keyframes scaleIn {
  from {
    transform: scale(0.9);
    opacity: 0;
  }

  to {
    transform: scale(1);
    opacity: 1;
  }
}

.dropdown-menu li {
  padding: 12px 16px;
  color: #333;
  display: flex;
  flex-direction: column;
  transition: all 0.2s ease;

  &:hover {
    background-color: #f5f5f5;
    padding-left: 20px;
  }

  &:active {
    background-color: #e0e0e0;
  }
}

.assignment-list {
  margin-bottom: 20px;
  max-height: 600px;
  overflow-y: auto;
}

.assignment-list h3 {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.assignment-list ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.assignment-list li {
  position: relative;
  padding: 16px;
  background: rgb(246, 250, 252);
  border-radius: 12px;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 4px 12px rgba(78, 111, 130, 0.1);
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  cursor: pointer;
  overflow: hidden;
}

.assignment-list li:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(78, 111, 130, 0.15);
}

.assignment-list li::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 4px;
  background: linear-gradient(to bottom, #8aa8c4, #6e8ba7);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.assignment-list li:hover::before {
  opacity: 1;
}

.assignment-item-content {
  flex: 1;
  margin-right: 20px;
}

.assignment-meta {
  display: flex;
  gap: 12px;
  color: #6e8ba7;
  font-size: 0.9em;
  margin-top: 8px;
}

.progress-ring {
  width: 40px;
  height: 40px;
  transform: rotate(-90deg);
}

.progress-ring circle {
  fill: none;
  stroke-width: 4;
  stroke-linecap: round;
}

.progress-ring-bg {
  stroke: #e0e9f1;
}

.progress-ring-fill {
  stroke: #8aa8c4;
  stroke-dasharray: 100;
  transition: stroke-dashoffset 0.5s ease;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 12px;
  width: 400px;
  max-height: 80%;
  overflow-y: auto;
}

.modal-content h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.modal-controls {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.modal-controls button.add-btn {
  background-color: #bfdbea;
  border: 2px solid white;
  color: white;
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    background-color: rgba(255, 255, 255, 0.3);
    transform: translateY(-2px);
  }
}

.recommended-assignments h3,
.manual-assignment h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 10px;
}

.recommended-assignments ul,
.manual-assignment ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.recommended-assignments li,
.manual-assignment li {
  padding: 10px;
  background: #f9f9f9;
  border-radius: 8px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.recommended-assignments li button.add-btn,
.manual-assignment li button.add-btn {
  background-color: #88c7e9;
  border: none;
  color: white;
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    background-color: #bfdbea;
  }
}

.manual-assignment input,
.manual-assignment textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  margin-bottom: 10px;
}

.manual-assignment textarea {
  resize: vertical;
  min-height: 100px;
}

.close-btn {
  background-color: #ccc;
  border: none;
  color: white;
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    background-color: #aaa;
  }
}

.add-btn {
  background-color: #bfdbea;
  border: 2px solid white;
  color: white;
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    background-color: #d9eaf3;
    transform: translateY(-2px);
  }
}
</style>
