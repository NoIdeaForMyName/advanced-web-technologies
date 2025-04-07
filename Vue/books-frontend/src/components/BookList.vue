<template>
    <div class="book-list">
      <div class="header">
        <h2>Books</h2>
        <button @click="showAddModal = true">Add Book</button>
      </div>
      
      <div v-if="isLoading">Loading...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <template v-else>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
              <th>Author</th>
              <th>Pages</th>
              <th>Available</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="book in books" :key="book.id">
              <td>{{ book.id }}</td>
              <td>{{ book.title }}</td>
              <td>{{ book.author ? `${book.author.name} ${book.author.lastName}` : 'N/A' }}</td>
              <td>{{ book.pages }}</td>
              <td>{{ book.available ? 'Yes' : 'No' }}</td>
              <td>
                <button @click="editBook(book.id)">Edit</button>
                <button @click="deleteBook(book.id)">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
        
        <div class="pagination">
          <button 
            @click="changePage(page - 1)" 
            :disabled="page === 1"
          >
            Previous
          </button>
          <span>Page {{ page }} of {{ totalPages }}</span>
          <button 
            @click="changePage(page + 1)" 
            :disabled="page >= totalPages"
          >
            Next
          </button>
        </div>
      </template>
      
      <BookForm 
        v-if="showAddModal" 
        @close="showAddModal = false" 
        @save="handleAddBook"
      />
      <BookForm 
        v-if="showEditModal" 
        :book="currentBook"
        @close="showEditModal = false" 
        @save="handleUpdateBook"
      />
    </div>
  </template>
  
  <script>
  import { ref, onMounted, computed } from 'vue'
  import useBooks from '@/composables/useBooks'
  import BookForm from './BookForm.vue'
  
  export default {
    components: { BookForm },
    setup() {
      const {
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
      } = useBooks()

      const page = computed(() => pagination.value.page)
      const perPage = computed(() => pagination.value.perPage)
      const total = computed(() => pagination.value.total)
      const totalPages = computed(() => Math.ceil(total.value / perPage.value))
      
      const showAddModal = ref(false)
      const showEditModal = ref(false)
      
      onMounted(fetchBooks)
      
      const handleAddBook = async (bookData) => {
        await createBook(bookData)
        showAddModal.value = false
      }
      
      const editBook = async (id) => {
        await fetchBook(id)
        showEditModal.value = true
      }
      
      const handleUpdateBook = async (bookData) => {
        await updateBook(currentBook.value.id, bookData)
        showEditModal.value = false
      }
      
      const deleteBookHandler = async (id) => {
        if (confirm('Are you sure you want to delete this book?')) {
          await deleteBook(id)
        }
      }
      
      const changePage = (page) => {
        if (page >= 1 && page <= Math.ceil(pagination.value.total / pagination.value.perPage)) {
          pagination.value.page = page
          fetchBooks()
        }
      }
      
      return {
        books,
        currentBook,
        isLoading,
        error,
        pagination,
        showAddModal,
        showEditModal,
        handleAddBook,
        editBook,
        handleUpdateBook,
        deleteBook: deleteBookHandler,
        changePage,
        page,
        perPage,
        total,
        totalPages
      }
    }
  }
  </script>
  
  <style scoped>
  .book-list {
    padding: 20px;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
  }
  
  th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
  }
  
  th {
    background-color: #f2f2f2;
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
  }
  
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .error {
    color: red;
  }
  </style>