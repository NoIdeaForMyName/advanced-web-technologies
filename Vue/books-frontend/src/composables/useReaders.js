import { ref } from 'vue'
import readersService from '@/services/readersService'

export default function useReaders() {
  const readers = ref([])
  const currentReader = ref(null)
  const isLoading = ref(false)
  const error = ref(null)
  
  const pagination = ref({
    page: 1,
    perPage: 10,
    total: 0
  })
  
  const fetchReaders = async (page = pagination.value.page) => {
    isLoading.value = true
    error.value = null
    try {
      pagination.value.page = page
      const response = await readersService.getReaders(pagination.value.page, pagination.value.perPage)
      readers.value = response.data.data
      pagination.value.total = response.data.total_items
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }
  
  const fetchReader = async (id) => {
    isLoading.value = true
    error.value = null
    try {
      const response = await readersService.getReader(id)
      currentReader.value = response.data
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }
  
  const createReader = async (readerData) => {
    isLoading.value = true
    error.value = null
    try {
      await readersService.createReader(readerData)
      await fetchReaders()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }
  
  const updateReader = async (id, readerData) => {
    isLoading.value = true
    error.value = null
    try {
      await readersService.updateReader(id, readerData)
      await fetchReaders()
    } catch (err) {
      error.value = err.message
    } finally {
      isLoading.value = false
    }
  }
  
  const deleteReader = async (id) => {
    isLoading.value = true
    error.value = null
    try {
      await readersService.deleteReader(id)
      await fetchReaders()
    } catch (err) {
      //error.value = err.message
      confirm(err.response.data)
    } finally {
      isLoading.value = false
    }
  }
  
  return {
    readers,
    currentReader,
    isLoading,
    error,
    pagination,
    fetchReaders,
    fetchReader,
    createReader,
    updateReader,
    deleteReader
  }
}