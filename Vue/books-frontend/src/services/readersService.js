import apiClient from './api'

export default {
  getReaders(page = 1, perPage = 2) {
    return apiClient.get(`/readers?page=${page}&per_page=${perPage}`)
  },

  getReader(id) {
    return apiClient.get(`/readers/${id}`)
  },

  createReader(reader) {
    return apiClient.post('/readers', reader)
  },

  updateReader(id, reader) {
    return apiClient.put(`/readers/${id}`, reader)
  },
  
  deleteReader(id) {
    return apiClient.delete(`/readers/${id}`)
  }
}