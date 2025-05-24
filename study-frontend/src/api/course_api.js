import request from '../utils/request';

const API_PREFIX = '/course';

export const courseApi = {
  // 获取所有课程列表
  getAllCourses: () => request.get(`${API_PREFIX}/list`),

  // 添加新课程
  addCourse: (course) => request.post(`${API_PREFIX}/add`, course),

  // 删除课程
  deleteCourse: (id) => request.delete(`${API_PREFIX}/delete/${id}`),

  // 更新课程信息
 updateCourse: (id, course) => request.put(`${API_PREFIX}/update/${id}`, course),

  // 获取当前教师的所有课程
  getMyCourses: () => request.get(`${API_PREFIX}/my-courses`),

  // 根据ID获取课程详情
  getCourseById: (id) => request.get(`${API_PREFIX}/get/${id}`),

  // 上传课程图片
  uploadImage: (id, file) => {
    const formData = new FormData();
    formData.append('file', file);
    return request.post(`${API_PREFIX}/${id}/upload-image`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },

  // 搜索课程
  searchCourses: (params) => {
    const searchParams = {
      course_name: params?.courseName || '',
      class_name: params?.className || '',
      all: params?.all ?? false
    };
    
    return request.get(`${API_PREFIX}/search`, { params: searchParams });
  },

  uploadResource: (id, file) => {
    const formData = new FormData();
    formData.append('file', file);
    return request.post(`${API_PREFIX}/${id}/upload-resource`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },
};