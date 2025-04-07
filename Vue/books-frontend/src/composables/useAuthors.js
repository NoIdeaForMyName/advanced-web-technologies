import { ref } from 'vue'
import authorsService from '@/services/authorsService'

export default function useAuthors() {
  const authors = ref([])
  const currentAuthor = ref(null)
  const isLoading = ref(false)
  const error = ref(null)
  const pagination = ref({
    page: 1,
    perPage: 2,
    total: 0
  })

  const fetchAuthors = async (page = pagination.value.page) => {
    isLoading.value = true
    try {
      pagination.value.page = page
      const response = await authorsService.getAuthors(
        pagination.value.page,
        pagination.value.perPage
      )
      authors.value = response.data.data
      pagination.value.total = response.data.total_items
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }
  

  const fetchAuthor = async (id) => {
    isLoading.value = true
    try {
      const response = await authorsService.getAuthor(id)
      currentAuthor.value = response.data
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const createAuthor = async (authorData) => {
    isLoading.value = true
    try {
      await authorsService.createAuthor(authorData)
      await fetchAuthors()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const updateAuthor = async (id, authorData) => {
    isLoading.value = true
    try {
      await authorsService.updateAuthor(id, authorData)
      await fetchAuthors()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  const deleteAuthor = async (id) => {
    isLoading.value = true
    try {
      await authorsService.deleteAuthor(id)
      await fetchAuthors()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }

  return {
    authors,
    currentAuthor,
    isLoading,
    error,
    pagination,
    fetchAuthors,
    fetchAuthor,
    createAuthor,
    updateAuthor,
    deleteAuthor
  }
}