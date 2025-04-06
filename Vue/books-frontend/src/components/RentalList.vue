<template>
    <div class="rental-list">
      <div class="header">
        <h2>Rentals</h2>
        <button @click="showRentModal = true">Rent Book</button>
      </div>
      
      <div v-if="isLoading">Loading...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <template v-else>
        <table>
          <thead>
            <tr>
              <th>Book Title</th>
              <th>Author</th>
              <th>Rented By</th>
              <th>Email</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="rental in rentals" :key="rental.book.id">
              <td>{{ rental.book.title }}</td>
              <td>{{ rental.book.author }}</td>
              <td>{{ rental.reader.name }}</td>
              <td>{{ rental.reader.email }}</td>
              <td>{{ rental.returned ? 'Returned' : 'Active' }}</td>
              <td>
                <button 
                  v-if="!rental.returned" 
                  @click="returnBook(rental.book.id)"
                >
                  Return
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </template>
      
      <RentalForm 
        v-if="showRentModal" 
        @close="showRentModal = false" 
        @rent="handleRentBook"
      />
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue'
  import useRentals from '@/composables/useRentals'
  import RentalForm from './RentalForm.vue'
  
  export default {
    components: { RentalForm },
    setup() {
      const {
        rentals,
        isLoading,
        error,
        fetchRentals,
        rentBook,
        returnBook
      } = useRentals()
      
      const showRentModal = ref(false)
      
      onMounted(fetchRentals)
      
      const handleRentBook = async (bookId, reader) => {
        await rentBook(bookId, reader)
        showRentModal.value = false
      }
      
      return {
        rentals,
        isLoading,
        error,
        showRentModal,
        handleRentBook,
        returnBook
      }
    }
  }
  </script>
  
  <style scoped>
  .rental-list {
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
  
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .error {
    color: red;
  }
  </style>