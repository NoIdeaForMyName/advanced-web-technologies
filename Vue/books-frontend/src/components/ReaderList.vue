<template>
    <div class="reader-list">
      <div class="header">
        <h2>Readers</h2>
        <button @click="showAddModal = true">Add Reader</button>
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
              <th>Email</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="reader in readers" :key="reader.id">
              <td>{{ reader.id }}</td>
              <td>{{ reader.name }}</td>
              <td>{{ reader.lastName }}</td>
              <td>{{ reader.email }}</td>
              <td>
                <button @click="editReader(reader.id)">Edit</button>
                <button @click="deleteReader(reader.id)">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
        
        <div class="pagination">
          <button 
            @click="changePage(pagination.page - 1)" 
            :disabled="pagination.page === 1"
          >
            Previous
          </button>
          <span>Page {{ pagination.page }} of {{ Math.ceil(pagination.total / pagination.perPage) }}</span>
          <button 
            @click="changePage(pagination.page + 1)" 
            :disabled="pagination.page * pagination.perPage >= pagination.total"
          >
            Next
          </button>
        </div>
      </template>
      
      <ReaderForm 
        v-if="showAddModal" 
        @close="showAddModal = false" 
        @save="handleAddReader"
      />
      <ReaderForm 
        v-if="showEditModal" 
        :reader="currentReader"
        @close="showEditModal = false" 
        @save="handleUpdateReader"
      />
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue'
  import useReaders from '@/composables/useReaders'
  import ReaderForm from './ReaderForm.vue'
  
  export default {
    components: { ReaderForm },
    setup() {
      const {
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
      } = useReaders()
      
      const showAddModal = ref(false)
      const showEditModal = ref(false)
      
      onMounted(fetchReaders)
      
      const handleAddReader = async (readerData) => {
        await createReader(readerData)
        showAddModal.value = false
      }
      
      const editReader = async (id) => {
        await fetchReader(id)
        showEditModal.value = true
      }
      
      const handleUpdateReader = async (readerData) => {
        await updateReader(currentReader.value.id, readerData)
        showEditModal.value = false
      }
      
      const deleteReaderHandler = async (id) => {
        if (confirm('Are you sure you want to delete this reader?')) {
          await deleteReader(id)
        }
      }
      
      const changePage = (page) => {
        if (page >= 1 && page <= Math.ceil(pagination.value.total / pagination.value.perPage)) {
          pagination.value.page = page
          fetchReaders()
        }
      }
      
      return {
        readers,
        currentReader,
        isLoading,
        error,
        pagination,
        showAddModal,
        showEditModal,
        handleAddReader,
        editReader,
        handleUpdateReader,
        deleteReader: deleteReaderHandler,
        changePage
      }
    }
  }
  </script>
  
  <style scoped>
  .reader-list {
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