import request from '@/utils/request';

const API_BASE_URL = "/teacher";

export const teacherApi = {
    // 用户登录（登录接口不需要 token）
    login: (username, password) => request.post(
        `${API_BASE_URL}/login`,
        new URLSearchParams({ username, password }),
        { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
    ).then(res => res.data),

    // 获取教师信息（自动带 token）
    getTeacher: (id) => request.get(`${API_BASE_URL}/${id}`).then(res => res.data),

    // 增加教师
    addTeacher: (teacher) => request.post(`${API_BASE_URL}/add`, teacher)
        .then(response => response.data)
        .catch(error => {
            throw error;
        }),

    // 删除教师
    deleteTeacher: (id) => request.delete(`${API_BASE_URL}/delete/${id}`)
        .then(response => response.data)
        .catch(error => {
            throw error;
        }),

    // 更新教师信息
    updateTeacher: (teacher) => request.put(`${API_BASE_URL}/update`, teacher)
        .then(response => response.data)
        .catch(error => {
            throw error;
        }),

    // 获取教师信息
    getTeacher: (id) => request.get(`${API_BASE_URL}/${id}`)
        .then(response => response.data)
        .catch(error => {
            throw error;
        }),

    // 更新教师头像
    updateAvatar: (id, avatarUrl) => request.post(`${API_BASE_URL}/update-avatar/${id}`, {}, { params: { avatarUrl } })
        .then(response => response.data)
        .catch(error => {
            throw error;
        }),

    // 搜索教师
    searchTeachers: (username, gender) => request.get(`${API_BASE_URL}/search`, { params: { username, gender } })
        .then(response => response.data)
        .catch(error => {
            throw error;
        })
};