<template>
    <div class="author-list">
      <div class="header">
        <h2>Authors</h2>
        <button @click="showAddModal = true">Add Author</button>
      </div>
      
      <div v-if="isLoading">Loading...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <template v-else>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Last Name</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="author in authors" :key="author.id">
              <td>{{ author.id }}</td>
              <td>{{ author.name }}</td>
              <td>{{ author.lastName }}</td>
              <td>
                <button @click="editAuthor(author.id)">Edit</button>
                <button @click="deleteAuthor(author.id)">Delete</button>
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
      
      <AuthorForm 
        v-if="showAddModal" 
        @close="showAddModal = false" 
        @save="handleAddAuthor"
      />
      <AuthorForm 
        v-if="showEditModal" 
        :author="currentAuthor"
        @close="showEditModal = false" 
        @save="handleUpdateAuthor"
      />
    </div>
  </template>
  
  <script>
  import { ref, onMounted, computed } from 'vue'
  import useAuthors from '@/composables/useAuthors'
  import AuthorForm from './AuthorForm.vue'
  
  export default {
    components: { AuthorForm },
    setup() {
      const {
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
      } = useAuthors()

      const page = computed(() => pagination.value.page)
      const perPage = computed(() => pagination.value.perPage)
      const total = computed(() => pagination.value.total)
      const totalPages = computed(() => Math.ceil(total.value / perPage.value))
      
      const showAddModal = ref(false)
      const showEditModal = ref(false)
      
      onMounted(fetchAuthors)
      
      const handleAddAuthor = async (authorData) => {
        await createAuthor(authorData)
        showAddModal.value = false
      }
      
      const editAuthor = async (id) => {
        await fetchAuthor(id)
        showEditModal.value = true
      }
      
      const handleUpdateAuthor = async (authorData) => {
        await updateAuthor(currentAuthor.value.id, authorData)
        showEditModal.value = false
      }
      
      const deleteAuthorHandler = async (id) => {
        if (confirm('Are you sure you want to delete this author?')) {
          await deleteAuthor(id)
        }
      }
      
      const changePage = (page) => {
        const maxPage = Math.ceil(pagination.value.total / pagination.value.perPage)
        if (page >= 1 && page <= maxPage) {
          fetchAuthors(page)
        }
      }

      
      return {
        authors,
        currentAuthor,
        isLoading,
        error,
        pagination,
        showAddModal,
        showEditModal,
        handleAddAuthor,
        editAuthor,
        handleUpdateAuthor,
        deleteAuthor: deleteAuthorHandler,
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
  .author-list {
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