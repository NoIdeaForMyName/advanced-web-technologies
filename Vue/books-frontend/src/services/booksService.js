import apiClient from './api'

export default {
  getBooks(page = 1, perPage = 10) {
    return apiClient.get(`/books?page=${page}&per_page=${perPage}`)
  },

  getBook(id) {
    return apiClient.get(`/books/${id}`)
  },

  createBook(book) {
    return apiClient.post('/books', book)
  },

  updateBook(id, book) {
    return apiClient.put(`/books/${id}`, book)
  },
  
  deleteBook(id) {
    return apiClient.delete(`/books/${id}`)
  }
}