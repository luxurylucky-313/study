<template>
  <div class="work-detail">
    <header class="header">
      <h2>作业分析</h2>
      <button class="back-button" @click="goBack">返回</button>
    </header>
    <div class="stats">
      <div class="stat-item">
        <i class="fa-solid fa-user-check"></i>
        <div>
          <p>提交人数</p>
          <h3>{{ submittedCount }}</h3>
        </div>
      </div>
      <div class="stat-item">
        <i class="fa-solid fa-user-times"></i>
        <div>
          <p>未提交人数</p>
          <h3>{{ notSubmittedCount }}</h3>
        </div>
      </div>
      <div class="stat-item">
        <i class="fa-solid fa-chart-bar"></i>
        <div>
          <p>平均分</p>
          <h3>{{ averageScore }}</h3>
        </div>
      </div>
      <div class="stat-item">
        <i class="fa-solid fa-arrow-up"></i>
        <div>
          <p>最高分</p>
          <h3>{{ highestScore }}</h3>
        </div>
      </div>
      <div class="stat-item">
        <i class="fa-solid fa-arrow-down"></i>
        <div>
          <p>最低分</p>
          <h3>{{ lowestScore }}</h3>
        </div>
      </div>
    </div>
    <div class="analysis">
      <h3>大题分析</h3>
      <table class="analysis-table">
        <thead>
          <tr>
            <th>大题名称</th>
            <th>对应题号</th>
            <th>分值</th>
            <th>占比</th>
            <th>平均分</th>
            <th>得分率</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in bigQuestionAnalysis" :key="index">
            <td>{{ item.name }}</td>
            <td>{{ item.questionNumbers }}</td>
            <td>{{ item.score }}</td>
            <td>{{ item.percentage }}%</td>
            <td>{{ item.averageScore }}</td>
            <td>{{ item.scoreRate }}%</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="analysis">
      <h3>小题分析</h3>
      <table class="analysis-table">
        <thead>
          <tr>
            <th>小题号</th>
            <th>难度</th>
            <th>区分度</th>
            <th>学生标答正确率</th>
            <th>答题情况</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in smallQuestionAnalysis" :key="index">
            <td>{{ item.number }}</td>
            <td>{{ item.difficulty }}</td>
            <td>{{ item.discrimination }}</td>
            <td>{{ item.correctRate }}%</td>
            <td>{{ item.answerStatus }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();

const submittedCount = ref(0);
const notSubmittedCount = ref(0);
const averageScore = ref(0);
const highestScore = ref(0);
const lowestScore = ref(0);

const bigQuestionAnalysis = ref([]);
const smallQuestionAnalysis = ref([]);

const fetchWorkDetail = (workId) => {
  // 模拟获取作业详情数据
  submittedCount.value = 85;
  notSubmittedCount.value = 15;
  averageScore.value = 61.38;
  highestScore.value = 85;
  lowestScore.value = 46.5;

  bigQuestionAnalysis.value = [
    { name: '单选题', questionNumbers: '1, 2, 3, 4, 5, 6', score: 30, percentage: 30, averageScore: 12.5, scoreRate: 42 },
    { name: '多选题', questionNumbers: '7, 8', score: 10, percentage: 10, averageScore: 7.5, scoreRate: 75 },
    { name: '简答题', questionNumbers: '9, 10, 11', score: 60, percentage: 60, averageScore: 41.38, scoreRate: 69 }
  ];

  smallQuestionAnalysis.value = [
    { number: 1, difficulty: 2.5, discrimination: 0.5, correctRate: 50, answerStatus: '略有难度' },
    { number: 2, difficulty: 3.75, discrimination: 0.25, correctRate: 75, answerStatus: '较易' },
    { number: 3, difficulty: 4.75, discrimination: 0.75, correctRate: 25, answerStatus: '困难' },
  ];
};

onMounted(() => {
  const workId = route.params.id;
  fetchWorkDetail(workId);
});

const goBack = () => {
  router.push('/work');
};
</script>

<style scoped>
.work-detail {
  padding: 20px;
  max-width: 1380px;
  height: 100%;
  margin: 0 auto;
  margin-top: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  color: #4e6f82;
}

.back-button {
  padding: 10px 20px;
  background:#66b1ff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.back-button:hover {
  background: #88bef7;
}

.stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 20px;
  background: #f5f5f5;
  padding: 20px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.178);
}

.stat-item i {
  font-size: 24px;
  color: #4e6f82;
}

.stat-item div {
  text-align: left;
}

.stat-item p {
  width: 160px;
  margin: 0;
  color: #999;
  font-size: 18px;
}

.stat-item h3 {
  margin: 0;
  color: #4e6f82;
}

.analysis {
  margin-bottom: 20px;
}

.analysis h3 {
  color: #4e6f82;
  margin-bottom: 10px;
}

.analysis-table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.analysis-table th, .analysis-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: center;
}

.analysis-table th {
  background: #f0f4ff;
  color: #4e6f82;
}

.analysis-table td {
  background: #fff;
  color: #333;
}
</style>
