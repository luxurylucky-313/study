import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    id: null,
    name: '',
    username: '',
    avatar: '',
    gender: '', 
    phone: '',   
  }),
  actions: {
    login(userInfo) {
      
      this.id = userInfo.id;
      this.name = userInfo.name;
      this.username = userInfo.username; 
      this.avatar = userInfo.avatar || '/src/assets/images/default-avatar.jpg'; 
      this.gender = userInfo.gender; 
      this.phone = userInfo.phone; 
    },
    logout() {
      this.username = '';
      this.avatar = '';
      localStorage.removeItem('userToken');  
    },
  }
});
