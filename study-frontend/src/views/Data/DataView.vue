<template>
  <MainLayout>
    <div class="data-view">
      <h1>学生学习数据分析</h1>
      <div class="grid-container">
        <div class="card student-info">
          <h2>学生信息</h2>
          <div class="icons">
            <el-icon><User /></el-icon>
            <el-icon><Download /></el-icon>
          </div>
        </div>
        <div class="card chapter-progress">
          <h2>章节学习进度</h2>
          <div class="progress-circle">
            <el-progress type="circle" :percentage="55"></el-progress>
          </div>
        </div>
        <div class="card knowledge-status">
          <h2>学生知识掌握情况</h2>
          <img :src="heatmap" alt="Heatmap" />
        </div>
        <div class="card problem-solving">
          <h2>学生问题解决能力</h2>
          <img :src="radar" alt="Radar Chart" />
        </div>
        <div class="card recent-performance">
          <h2>近两周学习效率对比</h2>
          <img :src="linechart" alt="Line Chart" />
        </div>
        <div class="card comprehensive-ability">
          <h2>学生综合能力测评</h2>
          <img :src="radar2" alt="Radar Chart" />
        </div>
        <div class="card teaching-feedback">
          <h2>教学反馈</h2>
          <div class="feedback-icons">
            <el-icon><Message /></el-icon>
            <el-icon><Notebook /></el-icon>
            <el-icon><Edit /></el-icon>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref } from 'vue';
import MainLayout from '@/components/MainLayout.vue';
import { User, Download, Message, Notebook, Edit } from '@element-plus/icons-vue';

// 导入图片
import heatmap from '@/assets/images/heatmap.png';
import radar from '@/assets/images/radar.png';
import linechart from '@/assets/images/linechart.png';
import radar2 from '@/assets/images/radar2.png';

const studentData = ref([
  { name: '张三', subject: '数学', score: 95, progress: 80 },
  { name: '李四', subject: '英语', score: 88, progress: 70 },
  // ...更多数据
]);

const teacherEvaluation = ref([
  { teacher: '王老师', evaluation: '优秀', comments: '学生表现非常好' },
  { teacher: '李老师', evaluation: '良好', comments: '学生有进步空间' },
  // ...更多数据
]);
</script>

<style scoped>
.data-view {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  overflow-y: auto;
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  grid-gap: 20px;
}

.card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
}

.card:hover {
  transform: translateY(-10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.icons, .feedback-icons {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.progress-circle {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
}

h1 {
  color: #333;
  text-align: center;
  margin-bottom: 20px;
}

h2 {
  color:#8aa8c4;
  margin-bottom: 10px;
}
</style>
