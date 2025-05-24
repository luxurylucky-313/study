import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/homework';

// 获取所有作业
export const getAllHomework = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/all`);
    return response.data;
  } catch (error) {
    console.error('Error fetching homework:', error);
    throw error;
  }
};

export const getHomeworkById = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching homework with ID ${id}:`, error);
    throw error;
  }
};

export const addHomework = async (homework) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/add`, homework);
    return response.data;
  } catch (error) {
    console.error('Error adding homework:', error);
    throw error;
  }
};

export const updateHomework = async (homework) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/update`, homework);
    return response.data;
  } catch (error) {
    console.error('Error updating homework:', error);
    throw error;
  }
};

export const deleteHomework = async (id) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/delete/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error deleting homework with ID ${id}:`, error);
    throw error;
  }
};
