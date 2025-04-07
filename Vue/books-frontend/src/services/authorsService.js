import apiClient from './api'

export default {
  getAuthors(page = 1, perPage = 10) {
    return apiClient.get(`/authors?page=${page}&per_page=${perPage}`)
  },

  getAuthor(id) {
    return apiClient.get(`/authors/${id}`)
  },

  createAuthor(author) {
    return apiClient.post('/authors', author)
  },

  updateAuthor(id, author) {
    return apiClient.put(`/authors/${id}`, author)
  },
  
  deleteAuthor(id) {
    return apiClient.delete(`/authors/${id}`)
  }
}