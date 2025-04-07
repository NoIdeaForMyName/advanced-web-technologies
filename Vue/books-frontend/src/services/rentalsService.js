import apiClient from './api'

export default {
  getAllRentals() {
    return apiClient.get('/rentals')
  },

  rentBook(bookId, reader) {
    return apiClient.post('/rentals/rent', {
      bookId,
      readerId: reader.id
    })
  },

  getRental(id) {
    return apiClient.get(`/rentals/${id}`)
  },

  updateRental(id, rentalData) {
    return apiClient.put(`/rentals/${id}`, rentalData)
  },

  returnBook(rentalId) {
    return apiClient.put(`/rentals/return/${rentalId}`)
  },
  
  getBookRenter(bookId) {
    return apiClient.get(`/rentals/book/${bookId}`)
  },

  deleteRental(id) {
    return apiClient.delete(`/rentals/${id}`)
  }
}