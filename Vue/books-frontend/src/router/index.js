import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '@/views/DashboardView.vue'
import BooksView from '@/views/BooksView.vue'
import AuthorsView from '@/views/AuthorsView.vue'
import RentalsView from '@/views/RentalsView.vue'

const routes = [
  {
    path: '/',
    name: 'dashboard',
    component: DashboardView
  },
  {
    path: '/books',
    name: 'books',
    component: BooksView
  },
  {
    path: '/authors',
    name: 'authors',
    component: AuthorsView
  },
  {
    path: '/rentals',
    name: 'rentals',
    component: RentalsView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router