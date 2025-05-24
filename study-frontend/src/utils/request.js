import axios from 'axios';

// 创建 axios 实例
const service = axios.create({
  baseURL: 'http://localhost:8080', // 根据实际情况修改
  timeout: 10000
});

// 请求拦截器，自动携带 token
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('userToken');
    if (token) {
      config.headers['Authorization'] =token;
    }
    return config;
  },
  error => Promise.reject(error)
);

// 响应拦截器（可选，统一处理错误）
service.interceptors.response.use(
  response => response,
  error => Promise.reject(error)
);

export default service;