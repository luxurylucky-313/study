import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import HomeView from '@/views/Course/HomeView.vue'
import WorkView from '@/views/Work/WorkView.vue'
import WorkDetail from '@/views/Work/WorkDetail.vue'
import CourseDetail from '@/views/Course/CourseDetail.vue'
import NewsView from '@/views/News/NewsView.vue'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('@/views/Login/LoginView.vue')
  },
  {
    path: '/home',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/work',
    name: 'Work',
    component: WorkView
  },
  {
    path: '/work/:id',
    name: 'WorkDetail',
    component: WorkDetail
  },
  {
    path: '/course/:id',
    name: 'CourseDetail',
    component: CourseDetail
  },
  {
    path: '/AI',
    name: 'AI',
    component: () => import('@/views/AI/AiView.vue')
  },
  {
    path: '/news',
    name: 'News',
    component: NewsView
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/')
  } else {
    next()
  }
})

export default router
