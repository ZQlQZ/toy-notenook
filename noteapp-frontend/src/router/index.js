import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import NoteList from '../views/NoteList.vue'


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/notes',
    name: 'NoteList',
    component: NoteList,
    meta: { requiresAuth: true }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫 - 检查登录状态
router.beforeEach((to, from, next) => {
  // 使用认证工具检查登录状态
  const token = localStorage.getItem('auth_token')
  const isAuthenticated = !!token
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/')
  } else if (to.path === '/' && isAuthenticated) {
    // 已登录用户访问根路径时重定向到笔记列表页面
    next('/notes')
  } else {
    next()
  }
})

export default router