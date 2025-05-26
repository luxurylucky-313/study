import request from '@/utils/request';

const BASE_URL = '/homework-questions';

/**
 * 获取作业下的所有问题
 * @param {number} homeworkId - 作业ID
 * @returns {Promise<{
 *   id: number,
 *   homeworkId: number,
 *   type: number,
 *   content: string,
 *   options: string|null,
 *   answer: string,
 *   score: number,
 *   sequence: number,
 *   createdAt: string,
 *   updatedAt: string
 * }[]>} 返回题目列表
 */
export const getQuestionsByHomeworkId = (homeworkId) => {
  return request({
    url: `${BASE_URL}/${homeworkId}`,
    method: 'get'
  });
};

/**
 * 批量保存作业题目
 * @param {Array<{
 *   homeworkId: number,
 *   type: number,
 *   content: string,
 *   options: string|null,
 *   answer: string,
 *   score: number,
 *   sequence: number
 * }>} questions - 题目对象数组
 * @returns {Promise<boolean>} 返回保存结果
 */
export const batchSaveQuestions = (questions) => {
  return request({
    url: `${BASE_URL}/batch`,
    method: 'post',
    data: questions
  });
};

/**
 * 删除作业下的所有题目
 * @param {number} homeworkId - 作业ID
 * @returns {Promise<boolean>} 返回删除结果
 */
export const deleteQuestionsByHomeworkId = (homeworkId) => {
  return request({
    url: `${BASE_URL}/${homeworkId}`,
    method: 'delete'
  });
};

/**
 * 删除指定题目
 * @param {number} questionId - 题目ID
 * @returns {Promise<boolean>} 返回删除结果
 */
export const deleteQuestion = (questionId) => {
  return request({
    url: `${BASE_URL}/delete/${questionId}`,
    method: 'delete'
  });
};

/**
 * 题目类型枚举
 */
export const QuestionType = {
  CHOICE: 1,    // 选择题
  JUDGE: 2,     // 判断题
  SHORT_ANSWER: 3 // 简答题
};