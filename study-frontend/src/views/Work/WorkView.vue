<template>
  <MainLayout>
    <div class="work-container">
      <div class="controls fixed-controls">
        <h2>已布置的作业</h2>
        <hr>
        <div class="search-box">
          <input 
            type="text" 
            v-model="searchTitle" 
            placeholder="搜索作业标题"
            @keyup.enter="handleSearch"
          />
          <input 
            type="text" 
            v-model="searchCourseId" 
            placeholder="输入课程ID"
            @keyup.enter="handleSearch"
          />
          <select v-model="searchStatus" @change="handleSearch">
            <option value="">全部状态</option>
            <option value="1">已发布</option>
            <option value="0">未发布</option>
          </select>
          <button class="search-btn" @click="handleSearch">搜索</button>
          <button class="add-btn" @click="showAddModal">新建作业</button>
        </div>
      </div>
      <div class="assignment-list">
        <ul>
          <li v-for="assignment in assignments" :key="assignment.id">
            <div class="assignment-item-content" @click="viewWorkDetail(assignment.id)">
              <strong>{{ assignment.title }}</strong>
              <div class="assignment-meta">
                <span><i class="fa-solid fa-book-open"></i> 课程ID: {{ assignment.courseId }}</span>
                <span><i class="fa-solid fa-calendar"></i> 
                  {{ new Date(assignment.startTime).toLocaleDateString() }} - 
                  {{ new Date(assignment.endTime).toLocaleDateString() }}
                </span>
                <span><i class="fa-solid fa-flag"></i> 
                  状态: {{ assignment.status === 1 ? '已发布' : '未发布' }}
                </span>
              </div>
              <p class="description">{{ assignment.description }}</p>
            </div>
            <div class="assignment-actions">
              <button class="action-btn edit" @click.stop="handleEditClick(assignment)">修改</button>
              <button class="action-btn status" @click.stop="toggleStatus(assignment)">
                {{ assignment.status === 1 ? '取消发布' : '发布' }}
              </button>
              <button class="action-btn delete" @click.stop="handleDelete(assignment)">删除</button>
            </div>
          </li>
        </ul>
        <div v-if="assignments.length === 0">
          <p>暂无作业数据。</p>
        </div>
      </div>
      <!-- 修改分页组件的显示逻辑 -->
      <div class="pagination" v-if="showPagination">
        <button 
          :disabled="currentPage === 1"
          @click="handlePageChange(currentPage - 1)"
        >上一页</button>
        <span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
        <button 
          :disabled="currentPage >= totalPages"
          @click="handlePageChange(currentPage + 1)"
        >下一页</button>
      </div>

      <!-- 新建作业模态框 -->
      <div v-if="showModal" class="modal">
        <div class="modal-content">
          <h3>新建作业</h3>
          <div class="form-group">
            <label>课程ID</label>
            <input type="number" v-model="newHomework.courseId" required>
          </div>
          <div class="form-group">
            <label>作业标题</label>
            <input type="text" v-model="newHomework.title" required>
          </div>
          <div class="form-group">
            <label>作业描述</label>
            <textarea v-model="newHomework.description" required></textarea>
          </div>
          <div class="form-group">
            <label>起始时间</label>
            <input type="datetime-local" v-model="newHomework.startTime" required>
          </div>
          <div class="form-group">
            <label>结束时间</label>
            <input type="datetime-local" v-model="newHomework.endTime" required>
          </div>
          <div class="modal-footer">
            <button class="cancel-btn" @click="closeModal">取消</button>
            <button class="submit-btn" @click="submitNewHomework">提交</button>
          </div>
        </div>
      </div>

      <!-- 添加修改作业模态框 -->
      <div v-if="showEditModal" class="modal">
        <div class="modal-content">
          <h3>修改作业</h3>
          <div class="form-group">
            <label>课程ID</label>
            <input type="number" v-model="editingHomework.courseId" required>
          </div>
          <div class="form-group">
            <label>作业标题</label>
            <input type="text" v-model="editingHomework.title" required>
          </div>
          <div class="form-group">
            <label>作业描述</label>
            <textarea v-model="editingHomework.description" required></textarea>
          </div>
          <div class="modal-footer">
            <button class="cancel-btn" @click="closeEditModal">取消</button>
            <button class="submit-btn" @click="submitEdit">保存</button>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getHomeworkByTeacherWithFilter, addHomework, updateHomework, deleteHomework } from '@/api/homework_api';
import MainLayout from '@/components/MainLayout.vue';
import { useUserStore } from '@/stores/user';

const router = useRouter();
const userStore = useUserStore();

// 数据模型
const assignments = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const searchTitle = ref('');
const searchCourseId = ref('');
const searchStatus = ref('');

// 新建作业相关
const showModal = ref(false);
const newHomework = ref({
  teacherId: userStore.id, // 添加教师ID
  courseId: '',
  title: '',
  description: '',
  startTime: '',
  endTime: '',
  status: 0 // 默认未发布
});

// 编辑作业相关
const showEditModal = ref(false);
const editingHomework = ref({
  id: null,
  teacherId: userStore.id,
  courseId: '',
  title: '',
  description: '',
  status: 0
});

// 获取作业数据
const fetchAssignments = async () => {
  try {
    const params = {
      teacherId: userStore.id,
      title: searchTitle.value,
      courseId: searchCourseId.value || undefined,
      status: searchStatus.value || undefined,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    };

    const response = await getHomeworkByTeacherWithFilter(params);
    
    if (response.data && response.data.code === 200 && response.data.data) {
      const { records, total: totalCount, size, current, pages } = response.data.data;
      assignments.value = records;
      total.value = totalCount;  // 使用后端返回的总记录数
      pageSize.value = size;
      currentPage.value = current;
      console.log('分页数据:', {
        当前页: current,
        每页条数: size,
        总条数: totalCount,
        总页数: pages
      });
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
  fetchAssignments();
});

function viewWorkDetail(workId) {
  router.push({ name: 'WorkDetail', params: { id: workId } });
}

// 搜索处理函数
const handleSearch = () => {
  currentPage.value = 1; // 重置页码
  fetchAssignments();
};

const showAddModal = () => {
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  // 重置表单时保留教师ID
  newHomework.value = {
    teacherId: userStore.id,
    courseId: '',
    title: '',
    description: '',
    startTime: '',
    endTime: '',
    status: 0
  };
};

const submitNewHomework = async () => {
  try {
    // 表单验证
    if (!newHomework.value.courseId || !newHomework.value.title || 
        !newHomework.value.description || !newHomework.value.startTime || 
        !newHomework.value.endTime) {
      alert('请填写完整信息');
      return;
    }
    
    await addHomework(newHomework.value);
    alert('作业创建成功');
    closeModal();
    fetchAssignments(); // 刷新列表
  } catch (error) {
    console.error('创建作业失败:', error);
    alert('创建作业失败，请重试');
  }
};

const handleEditClick = (assignment) => {
  editingHomework.value = { ...assignment };
  showEditModal.value = true;
};

const closeEditModal = () => {
  showEditModal.value = false;
  editingHomework.value = {
    id: null,
    teacherId: userStore.id,
    courseId: '',
    title: '',
    description: '',
    status: 0
  };
};

const submitEdit = async () => {
  try {
    if (!editingHomework.value.courseId || !editingHomework.value.title || 
        !editingHomework.value.description) {
      alert('请填写完整信息');
      return;
    }
    
    await updateHomework(editingHomework.value);
    alert('修改成功');
    closeEditModal();
    fetchAssignments();
  } catch (error) {
    console.error('修改作业失败:', error);
    alert('修改失败，请重试');
  }
};

const toggleStatus = async (assignment) => {
  try {
    const updatedHomework = {
      ...assignment,
      status: assignment.status === 1 ? 0 : 1
    };
    await updateHomework(updatedHomework);
    fetchAssignments();
  } catch (error) {
    console.error('更新状态失败:', error);
    alert('更新状态失败，请重试');
  }
};

const handleDelete = async (assignment) => {
  if (confirm('确定要删除该作业吗？')) {
    try {
      await deleteHomework(assignment.id);
      alert('删除成功');
      fetchAssignments();
    } catch (error) {
      console.error('删除失败:', error);
      alert('删除失败，请重试');
    }
  }
};

// 修改分页计算属性
const totalPages = computed(() => {
  return Math.ceil(total.value / pageSize.value) || 1;
});

const showPagination = computed(() => {
  return total.value > pageSize.value;
});

// 修改分页处理函数
const handlePageChange = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
  fetchAssignments(); // 直接调用后端获取对应页的数据
};
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

.search-box {
  display: flex;
  gap: 10px;
  margin-right: 20px;
}

.search-box input,
.search-box select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  width: 150px;
}

.search-box select {
  background-color: white;
  cursor: pointer;
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

.add-btn {
  padding: 5px 20px;
  background-color: #bfdbea;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: white;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.add-btn:hover {
  background-color: #d9eaf3;
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

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 20px;
  padding: 20px 0;
}

.pagination button {
  background-color: #bfdbea;
  border: none;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pagination button:hover:not(:disabled) {
  background-color: #d9eaf3;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.pagination span {
  color: #666;
}
.description {
  color: #666;
  font-size: 14px;
  margin-top: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
}

.assignment-meta {
  display: flex;
  gap: 16px;
  margin-top: 8px;
  color: #666;
  font-size: 0.9em;
}

.assignment-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.assignment-meta i {
  font-size: 14px;
  color: #8aa8c4;
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
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #666;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-group textarea {
  height: 100px;
  resize: vertical;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.cancel-btn,
.submit-btn {
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  border: none;
}

.cancel-btn {
  background-color: #ddd;
}

.submit-btn {
  background-color: #bfdbea;
  color: white;
}

.submit-btn:hover {
  background-color:  #45a7dc;
}

.assignment-actions {
  display: flex;
  gap: 8px;
  margin-left: 16px;
}

.action-btn {
  padding: 4px 8px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  color: white;
  font-size: 14px;
}

.action-btn.edit {
  background-color: #68c35e;
}

.action-btn.status {
  background-color: #62a4da;
}

.action-btn.delete {
  background-color: #d75250;
}

.action-btn:hover {
  opacity: 0.8;
}
</style>
