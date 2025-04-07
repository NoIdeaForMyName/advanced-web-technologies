import { ref } from 'vue'
import rentalsService from '@/services/rentalsService'

export default function useRentals() {
  const rentals = ref([])
  const currentRental = ref(null)
  const isLoading = ref(false)
  const error = ref(null)

  const pagination = ref({
    page: 1,
    perPage: 2,
    total: 0
  })

  const fetchRentals = async (page = pagination.value.page) => {
    isLoading.value = true
    try {
      pagination.value.page = page
      const response = await rentalsService.getAllRentals(page, pagination.value.perPage)
      rentals.value = response.data.data
      pagination.value.total = response.data.total_items
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const fetchRental = async (id) => {
    isLoading.value = true
    try {
      const response = await rentalsService.getRental(id)
      currentRental.value = response.data
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const rentBook = async (bookId, reader) => {
    isLoading.value = true
    try {
      await rentalsService.rentBook(bookId, reader)
      await fetchRentals()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const updateRental = async (id, rentalData) => {
    isLoading.value = true
    try {
      await rentalsService.updateRental(id, rentalData)
      await fetchRentals()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  } 

  const returnBook = async (rentalId) => {
    isLoading.value = true
    error.value = null
    try {
      await rentalsService.returnBook(rentalId)
      await fetchRentals()
    } catch (err) {
      error.value = err.message || 'Failed to return book'
    } finally {
      isLoading.value = false
    }
  }

  const getBookRenter = async (bookId) => {
    isLoading.value = true
    try {
      const response = await rentalsService.getBookRenter(bookId)
      currentRental.value = response.data
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const deleteRental = async (id) => {
    isLoading.value = true
    try {
      await rentalsService.deleteRental(id)
      await fetchRentals()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  return {
    rentals,
    currentRental,
    isLoading,
    error,
    pagination,
    fetchRentals,
    fetchRental,
    rentBook,
    updateRental,
    returnBook,
    getBookRenter,
    deleteRental
  }
}