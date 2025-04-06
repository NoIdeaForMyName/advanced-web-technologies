import { ref } from 'vue'
import activityService from '@/services/activityService'

export default function useActivities() {
  const activities = ref([])
  const isLoading = ref(false)
  const error = ref(null)
  const pagination = ref({
    page: 1,
    perPage: 10,
    total: 0
  })

  const fetchActivities = async () => {
    isLoading.value = true
    try {
      const response = await activityService.getActivities(
        pagination.value.page, 
        pagination.value.perPage
      )
      activities.value = response.data
      pagination.value.total = response.headers['x-total-count'] || response.data.length
    } catch (err) {
      error.value = err.response?.data?.message || err.message
    } finally {
      isLoading.value = false
    }
  }

  const fetchRecentActivities = async (limit = 5) => {
    isLoading.value = true
    try {
      const response = await activityService.getRecentActivities(limit)
      activities.value = response.data.slice(0, limit);
    } catch (err) {
      error.value = err.response?.data?.message || err.message
    } finally {
      isLoading.value = false
    }
  }

  return {
    activities,
    isLoading,
    error,
    pagination,
    fetchActivities,
    fetchRecentActivities
  }
}