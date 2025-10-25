import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CommentsView from '../components/CommentsView.vue'
import CourseStudentsList from "../components/CourseStudentsList.vue";
import GradesView from "@/components/GradesView.vue";
import CourseAnalyticsView from "@/views/CourseAnalyticsView.vue";
import AllCoursesView from "@/views/AllCoursesView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/courses/:courseId/students/:studentId',
      name: 'students comments',
      component: CommentsView,
    },
    {
      path: '/courses',
      name: 'courses',
      component: AllCoursesView,
    },
    {
      path: '/courses/:courseId/students',
      name: 'course students',
      component: CourseStudentsList,
      props: route => ({
        courseId: route.params.courseId,
        courseName: route.params.courseName,
      })
    },
    {
      path: '/courses/:courseId',
      name: 'course',
      component: CourseAnalyticsView,
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