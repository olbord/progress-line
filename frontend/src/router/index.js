import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CommentsView from '@/views/CommentsView.vue'
import CourseStudentsView from "@/views/CourseStudentsView.vue";
import GradesView from "@/views/GradesView.vue";
import FeedbackView from "@/views/FeedbackView.vue";
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
      path: '/courses/:courseId/students/:studentId/comments',
      name: 'students comments',
      component: CommentsView,
      props: true
    },
    {
      path: '/courses',
      name: 'courses',
      component: AllCoursesView,
    },
    {
      path: '/courses/:courseId/students',
      name: 'course students',
      component: CourseStudentsView,
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
      path: '/courses/:courseId/students/:studentId/grades',
      name: 'students grades',
      component: GradesView,
      props: true
    },
    {
      path: '/courses/:courseId/students/:studentId/feedback',
      name: 'students feedback',
      component: FeedbackView,
      props: true
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
  ],
})

export default router
