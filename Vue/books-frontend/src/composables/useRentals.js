import { ref } from 'vue'
import rentalsService from '@/services/rentalsService'

export default function useRentals() {
  const rentals = ref([])
  const currentRental = ref(null)
  const isLoading = ref(false)
  const error = ref(null)

  const fetchRentals = async () => {
    isLoading.value = true
    try {
      const response = await rentalsService.getAllRentals()
      rentals.value = response.data
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

  const returnBook = async (bookId) => {
    isLoading.value = true
    try {
      await rentalsService.returnBook(bookId)
      await fetchRentals()
    } catch (err) {
      error.value = err.message
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

  return {
    rentals,
    currentRental,
    isLoading,
    error,
    fetchRentals,
    rentBook,
    returnBook,
    getBookRenter
  }
}