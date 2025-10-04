import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CommentsView from '../views/CommentsView.vue'
import AllStudentsView from "../views/AllStudentsView.vue";
import GradesView from "@/views/GradesView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/comment/students/:id',
      name: 'students comments',
      component: CommentsView,
    },
    {
      path: '/students',
      name: 'students',
      component: AllStudentsView,
    },
    {
      path: '/grades',
      name: 'grades',
      component: GradesView,
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
  ],
})

export default router