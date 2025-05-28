<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { courseApi } from '@/api/course_api'; 
import MainLayout from '@/components/MainLayout.vue';
import CourseCard from '@/components/CourseCard.vue';
import { ElMessage, ElDialog, ElForm, ElFormItem, ElInput } from 'element-plus';
import { ElDatePicker } from 'element-plus'
const router = useRouter();

const courses = ref([]); // 存放课程数据
const courseName = ref(''); // 课程名称输入框的绑定变量
const className = ref('');  // 班级名称输入框的绑定变量

const initCourseList = async () => {
  try {
    const response = await courseApi.getMyCourses();
    if (response.data.code === 200) {
      courses.value = response.data.data;
    }
  } catch (error) {
    console.error('获取课程列表失败:', error);
  }
};

onMounted(() => {
  initCourseList();
});

const searchAll = ref(false); // 添加新的响应式变量

const searchCourses = async () => {
  try {
    const response = await courseApi.searchCourses({
      courseName: courseName.value,
      className: className.value,
      all: searchAll.value
    });
    
    if (response.data.code === 200) {
      courses.value = response.data.data;
    } else {
      ElMessage.error(response.data.msg || '搜索失败');
    }
  } catch (error) {
    console.error('搜索课程失败:', error);
    ElMessage.error('搜索失败');
  }
};


const viewCourseDetail = (courseId) => {
  router.push({ name: 'CourseDetail', params: { id: courseId } });
};

const dialogVisible = ref(false);
const newCourse = ref({
  courseName: '',
  className: '',
  startTime: '',
  endTime: '',
  description: ''
});

const formRef = ref(null);

// 表单校验规则
const rules = {
  courseName: [
    { required: true, message: '请输入课程名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  className: [
    { required: true, message: '请输入班级名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  description: [
    { max: 200, message: '长度不能超过 200 个字符', trigger: 'blur' }
  ]
};

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const response = await courseApi.addCourse(newCourse.value);
        if (response.data.code === 200) {
          ElMessage.success(response.data.message || '课程创建成功');
          dialogVisible.value = false;
          initCourseList(); // 刷新课程列表
          // 重置表单
          newCourse.value = {
            courseName: '',
            className: '',
            startTime: '',
            endTime: '',
            description: ''
          };
        } else {
          ElMessage.error(response.data.message || '创建失败');
        }
      } catch (error) {
        console.error('创建课程失败:', error);
        ElMessage.error(error.response?.data?.message || '创建课程失败');
      }
    }
  });
};
</script>

<template>
  <MainLayout>
<div class="home">
      <header>
        <h1>我的课程</h1>
        <div class="controls">
          <button class="create-btn" @click="dialogVisible = true">
            新建课程
          </button>
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
            <label class="search-all">
              <input 
                type="checkbox" 
                v-model="searchAll"
                @change="searchCourses"
              />
              <span>查看所有课程</span>
            </label>
            <button @click="searchCourses" class="search-btn">查询</button>
          </div>
        </div>
      </header>

      <div class="course-list">
        <CourseCard 
          v-for="course in courses" 
          :key="course.id" 
          :course="course"
          @refresh="initCourseList"
        />
      </div>
      <ElDialog
        v-model="dialogVisible"
        title="新建课程"
        width="500px"
        :close-on-click-modal="false"
      >
        <ElForm
          ref="formRef"
          :model="newCourse"
          :rules="rules"
          label-width="100px"
        >
          <ElFormItem label="课程名称" prop="courseName">
            <ElInput v-model="newCourse.courseName" placeholder="请输入课程名称" />
          </ElFormItem>
          <ElFormItem label="班级名称" prop="className">
            <ElInput v-model="newCourse.className" placeholder="请输入班级名称" />
          </ElFormItem>
          <ElFormItem label="开始时间" prop="startTime">
            <ElDatePicker
              v-model="newCourse.startTime"
              type="datetime"
              placeholder="选择开始时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DDTHH:mm:ss"
            />
          </ElFormItem>
          <ElFormItem label="结束时间" prop="endTime">
            <ElDatePicker
              v-model="newCourse.endTime"
              type="datetime"
              placeholder="选择结束时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DDTHH:mm:ss"
            />
          </ElFormItem>
          <ElFormItem label="课程描述" prop="description">
            <ElInput
              v-model="newCourse.description"
              type="textarea"
              rows="4"
              placeholder="请输入课程描述"
            />
          </ElFormItem>
        </ElForm>
        <template #footer>
          <span class="dialog-footer">
            <button class="cancel-btn" @click="dialogVisible = false">取消</button>
            <button class="confirm-btn" @click="submitForm">确定</button>
          </span>
        </template>
      </ElDialog>
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

.search-all {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  cursor: pointer;
  
  input[type="checkbox"] {
    width: 16px;
    height: 16px;
    cursor: pointer;
  }
  
  span {
    font-size: 14px;
  }
}

.course-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding: 20px;
  justify-content: flex-start;
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
.create-btn {
  padding: 10px 20px;
  background-color: #bfdbea;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: white;
  font-size: 16px;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #d9eaf3;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
}

.cancel-btn, .confirm-btn {
  padding: 8px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.cancel-btn {
  padding: 10px 20px;
  background-color: #d7dbdb;
  color: #666;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
  
  &:hover {
    background-color: #e8e8e8;
  }
}

.confirm-btn {
  padding: 10px 20px;
  background-color: #bfdbea;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: white;
  font-size: 16px;
  transition: background-color 0.3s ease;
  
  &:hover {
    background-color: #679ec6;
  }
}

// 确保弹窗中的表单样式正确
:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-input__inner) {
  border-radius: 4px;
}
</style>
