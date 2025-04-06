import apiClient from './api'

export default {
  getActivities(page = 1, perPage = 10) {
    return apiClient.get('/activity', {
      params: { page, limit: perPage }
    })
  },

  getRecentActivities(limit = 5) {
    return apiClient.get('/activity', {
      params: { limit, sort: 'timestamp,desc' }
    })
  }
}