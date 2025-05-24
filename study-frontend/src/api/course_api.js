import axios from 'axios';

const API_BASE_URL = "http://localhost:8080/course";

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
});

axiosInstance.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    console.error('API 请求失败:', error);
    return Promise.reject(error);
  }
);

export const courseApi = {
  getAllCourses: () => axiosInstance.get('/list'),

  addCourse: (course) => axiosInstance.post('/add', course),

  deleteCourse: (id) => axiosInstance.delete(`/delete/${id}`),

  updateCourse: (id, course) => axiosInstance.put(`/update/${id}`, course),

  getCourseById: (id) => axiosInstance.get(`/get/${id}`),

  uploadImage: (id, file) => {
    let formData = new FormData();
    formData.append('file', file);
    return axiosInstance.post(`/${id}/upload-image`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },

  searchCourses: (courseName, className) => {
    const params = {};
    if (courseName) params.course_name = courseName;
    if (className) params.class_name = className;

    return axiosInstance.get('/search', { params });
  },
};