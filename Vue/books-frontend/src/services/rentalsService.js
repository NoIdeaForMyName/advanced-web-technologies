import apiClient from './api'

export default {
  getAllRentals() {
    return apiClient.get('/rentals')
  },

  rentBook(bookId, reader) {
    return apiClient.post('/rentals/rent', reader, {
      params: { bookId },
      data: reader
    })
  },

  returnBook(bookId) {
    return apiClient.post(`/rentals/return/${bookId}`)
  },
  
  getBookRenter(bookId) {
    return apiClient.get(`/rentals/book/${bookId}`)
  }
}