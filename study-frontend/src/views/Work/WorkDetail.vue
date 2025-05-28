<template>
  <MainLayout>
    <div class="work-detail-container">
      <!-- 添加操作按钮区域 -->
      <div class="action-buttons">
        <button class="back-btn" @click="goBack">
          <i class="fa-solid fa-arrow-left"></i> 返回
        </button>
        <button class="delete-all-btn" @click="handleDeleteAll">
          <i class="fa-solid fa-trash"></i> 删除所有题目
        </button>
      </div>

      <!-- 作业基本信息 -->
      <div class="homework-info">
        <h2>{{ homework.title }}</h2>
        <div class="meta-info">
          <span><i class="fa-solid fa-book-open"></i> 课程ID: {{ homework.courseId }}</span>
          <span><i class="fa-solid fa-calendar"></i> 
            {{ new Date(homework.startTime).toLocaleDateString() }} - 
            {{ new Date(homework.endTime).toLocaleDateString() }}
          </span>
          <span><i class="fa-solid fa-flag"></i> 
            状态: {{ homework.status === 1 ? '已发布' : '未发布' }}
          </span>
        </div>
        <p class="description">{{ homework.description }}</p>
      </div>

      <!-- 题目列表 -->
      <div class="questions-section">
        <div class="section-header">
          <h3>题目列表</h3>
          <button class="add-question-btn" @click="showAddModal = true">
            新增题目
          </button>
        </div>

        <div class="questions-list">
          <div v-for="(question, index) in questions" :key="index" class="question-item">
            <div class="question-header">
              <div class="left">
                <span class="question-type">
                  {{ getQuestionTypeName(question.type) }}
                </span>
                <span class="question-score">分值：{{ question.score }}分</span>
              </div>
              <button class="delete-btn" @click="handleDeleteQuestion(question.id)">
                <i class="fa-solid fa-trash"></i>
              </button>
            </div>
            <div class="question-content">
              <p class="question-text">{{ index + 1 }}. {{ question.content }}</p>
              <!-- 选择题选项 -->
              <div v-if="question.type === QuestionType.CHOICE && question.options" class="options">
                <div v-for="(option, key) in JSON.parse(question.options)" :key="key" class="option">
                  {{ key }}. {{ option }}
                </div>
              </div>
              <!-- 判断题答案 -->
              <div v-if="question.type === QuestionType.JUDGE" class="judge-answer">
                正确答案：{{ question.answer === 'true' ? '正确' : '错误' }}
              </div>
              <!-- 简答题答案 -->
              <div v-if="question.type === QuestionType.SHORT_ANSWER" class="short-answer">
                参考答案：{{ question.answer }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 新增题目模态框 -->
      <div v-if="showAddModal" class="modal">
        <div class="modal-content">
          <h3>新增题目</h3>
          <div class="form-group">
            <label>题目类型</label>
            <select v-model="newQuestion.type">
              <option :value="QuestionType.CHOICE">选择题</option>
              <option :value="QuestionType.JUDGE">判断题</option>
              <option :value="QuestionType.SHORT_ANSWER">简答题</option>
            </select>
          </div>

          <div class="form-group">
            <label>题目内容</label>
            <textarea v-model="newQuestion.content" required></textarea>
          </div>

          <!-- 选择题选项 -->
          <div v-if="newQuestion.type === QuestionType.CHOICE" class="form-group">
            <label>选项</label>
            <div v-for="(option, index) in optionsArray" :key="index" class="option-input">
              <input 
                :placeholder="'选项' + String.fromCharCode(65 + index)"
                v-model="optionsArray[index]"
              >
              <button @click="removeOption(index)" class="remove-option">删除</button>
            </div>
            <button @click="addOption" class="add-option">添加选项</button>
          </div>

          <div class="form-group">
            <label>答案</label>
            <div v-if="newQuestion.type === QuestionType.CHOICE">
              <input type="text" v-model="newQuestion.answer" placeholder="输入正确选项(A/B/C/D)">
            </div>
            <div v-else-if="newQuestion.type === QuestionType.JUDGE">
              <select v-model="newQuestion.answer">
                <option value="true">正确</option>
                <option value="false">错误</option>
              </select>
            </div>
            <div v-else>
              <textarea v-model="newQuestion.answer" placeholder="输入参考答案"></textarea>
            </div>
          </div>

          <div class="form-group">
            <label>分值</label>
            <input type="number" v-model="newQuestion.score" required>
          </div>

          <div class="modal-footer">
            <button class="cancel-btn" @click="closeAddModal">取消</button>
            <button class="submit-btn" @click="submitQuestion">提交</button>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import MainLayout from '@/components/MainLayout.vue';
import { getHomeworkById } from '@/api/homework_api';
import { 
  getQuestionsByHomeworkId, 
  batchSaveQuestions, 
  deleteQuestionsByHomeworkId,
  deleteQuestion,
  QuestionType 
} from '@/api/homework-questions_api';

const router = useRouter();
const route = useRoute();
const homework = ref({});
const questions = ref([]);
const showAddModal = ref(false);
const optionsArray = ref(['', '', '', '']);

const newQuestion = ref({
  homeworkId: null,
  type: QuestionType.CHOICE,
  content: '',
  options: null,
  answer: '',
  score: 5,
  sequence: 0
});

// 获取作业信息和题目列表
const fetchData = async () => {
  try {
    const homeworkId = route.params.id;
    const [homeworkRes, questionsRes] = await Promise.all([
      getHomeworkById(homeworkId),
      getQuestionsByHomeworkId(homeworkId)
    ]);

    if (homeworkRes.data ) {
      homework.value = homeworkRes.data;
    }

    if (questionsRes.data?.code === 200) {
      questions.value = questionsRes.data.data;
    }
  } catch (error) {
    console.error('获取数据失败:', error);
    alert('获取数据失败，请重试');
  }
};

onMounted(fetchData);

// 获取题目类型名称
const getQuestionTypeName = (type) => {
  switch (type) {
    case QuestionType.CHOICE: return '选择题';
    case QuestionType.JUDGE: return '判断题';
    case QuestionType.SHORT_ANSWER: return '简答题';
    default: return '未知类型';
  }
};

// 选项管理
const addOption = () => {
  if (optionsArray.value.length < 26) { // 限制最多26个选项
    optionsArray.value.push('');
  }
};

const removeOption = (index) => {
  optionsArray.value.splice(index, 1);
};

// 提交新题目
const submitQuestion = async () => {
  try {
    if (!newQuestion.value.content || !newQuestion.value.answer || !newQuestion.value.score) {
      alert('请填写完整信息');
      return;
    }

    // 处理选择题选项
    if (newQuestion.value.type === QuestionType.CHOICE) {
      const validOptions = optionsArray.value.filter(opt => opt.trim() !== '');
      if (validOptions.length < 2) {
        alert('选择题至少需要两个选项');
        return;
      }
      // 将选项数组转换为对象
      const optionsObj = {};
      validOptions.forEach((opt, index) => {
        optionsObj[String.fromCharCode(65 + index)] = opt;
      });
      newQuestion.value.options = JSON.stringify(optionsObj);
    }

    newQuestion.value.homeworkId = route.params.id;
    newQuestion.value.sequence = questions.value.length + 1;

    await batchSaveQuestions([newQuestion.value]);
    alert('添加成功');
    closeAddModal();
    fetchData(); // 刷新题目列表
  } catch (error) {
    console.error('添加题目失败:', error);
    alert('添加失败，请重试');
  }
};

// 关闭模态框
const closeAddModal = () => {
  showAddModal.value = false;
  newQuestion.value = {
    homeworkId: null,
    type: QuestionType.CHOICE,
    content: '',
    options: null,
    answer: '',
    score: 5,
    sequence: 0
  };
  optionsArray.value = ['', '', '', ''];
};

// 返回上一页
const goBack = () => {
  router.go(-1); // 返回上一页
};

// 删除所有题目
const handleDeleteAll = async () => {
  try {
    if (!confirm('确定要删除该作业的所有题目吗？此操作不可恢复！')) {
      return;
    }

    const homeworkId = route.params.id;
    await deleteQuestionsByHomeworkId(homeworkId);
    alert('删除成功');
    questions.value = []; // 清空题目列表
  } catch (error) {
    console.error('删除题目失败:', error);
    alert('删除失败，请重试');
  }
};

// 删除单个题目
const handleDeleteQuestion = async (questionId) => {
  try {
    if (!confirm('确定要删除该题目吗？此操作不可恢复！')) {
      return;
    }

    await deleteQuestion(questionId);
    alert('删除成功');
    fetchData(); // 刷新题目列表
  } catch (error) {
    console.error('删除题目失败:', error);
    alert('删除失败，请重试');
  }
};
</script>

<style scoped>
.work-detail-container {
  max-width: 1200px;
  margin: auto;
  padding: 20px;
}

.action-buttons {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.back-btn,
.delete-all-btn {
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  border: none;
  display: flex;
  align-items: center;
  gap: 8px;
}

.back-btn {
  background-color: #62a4da;
  color: white;
}

.delete-all-btn {
  background-color: #d75250;
  color: white;
}

.back-btn:hover,
.delete-all-btn:hover {
  opacity: 0.9;
}

.homework-info {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 24px;
}

.meta-info {
  display: flex;
  gap: 16px;
  margin: 12px 0;
  color: #666;
}

.description {
  color: #555;
  line-height: 1.6;
}

.questions-section {
  margin-top: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.add-question-btn {
  background-color: #68c35e;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.question-item {
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.question-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  align-items: center;
}

.question-header .left {
  display: flex;
  gap: 12px;
  align-items: center;
}

.delete-btn {
  background-color: #ff5252;
  color: white;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: opacity 0.2s;
}

.delete-btn:hover {
  opacity: 0.8;
}

.question-type {
  background-color: #e3f2fd;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
}

.question-score {
  color: #f57c00;
}

.question-content {
  color: #333;
  line-height: 1.6;
}

.options .option {
  margin: 8px 0;
  padding: 8px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 24px;
  border-radius: 8px;
  width: 500px;
  max-width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
}

.form-group input,
.form-group select,
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

.option-input {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.remove-option {
  background-color: #ff5252;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
}

.add-option {
  background-color: #2196F3;
  color: white;
  border: none;
  padding: 8px;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 8px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.cancel-btn,
.submit-btn {
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  border: none;
}

.cancel-btn {
  background-color: #e0e0e0;
}

.submit-btn {
  background-color: #4CAF50;
  color: white;
}
</style>