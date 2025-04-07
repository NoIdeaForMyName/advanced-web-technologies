import { ref } from 'vue'
import booksService from '@/services/booksService'

export default function useBooks() {
  const books = ref([])
  const currentBook = ref(null)
  const isLoading = ref(false)
  const error = ref(null)
  const pagination = ref({
    page: 1,
    perPage: 2,
    total: 0
  })

  const fetchBooks = async (page = pagination.value.page) => {
    isLoading.value = true
    try {
      pagination.value.page = page
      const response = await booksService.getBooks(
        pagination.value.page,
        pagination.value.perPage
      )
      books.value = response.data.data
      pagination.value.total = response.data.total_items
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const fetchBook = async (id) => {
    isLoading.value = true
    try {
      const response = await booksService.getBook(id)
      currentBook.value = response.data
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const createBook = async (bookData) => {
    isLoading.value = true
    try {
      await booksService.createBook(bookData)
      await fetchBooks()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const updateBook = async (id, bookData) => {
    isLoading.value = true
    try {
      await booksService.updateBook(id, bookData)
      await fetchBooks()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const deleteBook = async (id) => {
    isLoading.value = true
    try {
      await booksService.deleteBook(id)
      await fetchBooks()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  return {
    books,
    currentBook,
    isLoading,
    error,
    pagination,
    fetchBooks,
    fetchBook,
    createBook,
    updateBook,
    deleteBook
  }
}