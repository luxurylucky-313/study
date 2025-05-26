import request from '@/utils/request';

const BASE_URL = '/homework';

/**
 * 获取所有作业列表
 * @returns {Promise} 返回作业列表数据
 */
export const getAllHomework = () => {
  return request({
    url: `${BASE_URL}/all`,
    method: 'get'
  });
};

/**
 * 根据ID获取作业详情
 * @param {number} id - 作业ID
 * @returns {Promise} 返回作业详情数据
 */
export const getHomeworkById = (id) => {
  return request({
    url: `${BASE_URL}/${id}`,
    method: 'get'
  });
};

/**
 * 添加新作业
 * @param {Object} homework - 作业对象
 * @returns {Promise} 返回添加结果
 */
export const addHomework = (homework) => {
  return request({
    url: `${BASE_URL}/add`,
    method: 'post',
    data: homework
  });
};

/**
 * 更新作业信息
 * @param {Object} homework - 包含id的作业对象
 * @returns {Promise} 返回更新结果
 */
export const updateHomework = (homework) => {
  return request({
    url: `${BASE_URL}/update`,
    method: 'put',
    data: homework
  });
};

/**
 * 删除作业
 * @param {number} id - 作业ID
 * @returns {Promise} 返回删除结果
 */
export const deleteHomework = (id) => {
  return request({
    url: `${BASE_URL}/delete/${id}`,
    method: 'delete'
  });
};

/**
 * 根据教师ID查询作业
 * @param {number} teacherId - 教师ID
 * @returns {Promise} 返回该教师的作业列表
 */
export const getHomeworkByTeacher = (teacherId) => {
  return request({
    url: `${BASE_URL}/teacher/${teacherId}`,
    method: 'get'
  });
};

/**
 * 根据课程ID查询作业
 * @param {number} courseId - 课程ID
 * @returns {Promise} 返回该课程的作业列表
 */
export const getHomeworkByCourse = (courseId) => {
  return request({
    url: `${BASE_URL}/course/${courseId}`,
    method: 'get'
  });
};

/**
 * 根据教师ID及过滤条件分页查询作业
 * @param {Object} params - 查询参数
 * @param {number} [params.teacherId] - 教师ID（可选）
 * @param {string} [params.title] - 作业标题（可选，模糊匹配）
 * @param {number} [params.courseId] - 课程ID（可选）
 * @param {number} [params.status] - 作业状态（可选，0:未发布,1:已发布）
 * @param {number} params.pageNum - 当前页码
 * @param {number} params.pageSize - 每页记录数
 * @returns {Promise<{
 *   records: Array,
 *   total: number,
 *   size: number,
 *   current: number,
 *   pages: number
 * }>} 返回分页数据
 */
export const getHomeworkByTeacherWithFilter = (params) => {
  return request({
    url: `${BASE_URL}/filter-by-teacher`,
    method: 'get',
    params: {
      teacherId: params.teacherId,
      title: params.title,
      courseId: params.courseId,
      status: params.status,
      pageNum: params.pageNum || 1,
      pageSize: params.pageSize || 10
    }
  });
};