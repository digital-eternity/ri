import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { authService } from '@/api/authService'

import LoginView from '@/views/LoginView.vue'
import UsersView from '@/views/UsersView.vue'
import MainView from '@/views/MainView.vue'
import UserProfileView from '@/views/UserProfileView.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/',
    name: 'main',
    component: MainView,
    meta: { requiresAuth: true }
  },
  {
    path: '/users',
    name: 'users',
    component: UsersView,
    meta: { requiresAuth: true } // Mark route as requiring authentication
  },
  {
    path: '/profile',
    name: 'profile',
    component: UserProfileView,
    meta: { requiresAuth: true } // Mark route as requiring authentication
  },
  {
    path: '/about',
    name: 'about',
    meta: { requiresAuth: true }, // Mark route as requiring authentication
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ '@/views/AboutView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// router.beforeEach((to, from, next) => {
//   if (to.meta.requiresAuth && !authService.isLoggedIn()) {
//     // If route requires authentication and user is not logged in, redirect to login
//     next('/login')
//   } else {
//     next() // Allow navigation
//   }
// })

export default router
