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
              <th>Rental Date</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="rental in rentals" :key="rental.book.id">
              <td>{{ rental.book.title }}</td>
              <td>{{ rental.book.author ? `${rental.book.author.name} ${rental.book.author.lastName}` : 'N/A' }}</td>
              <td>{{ rental.reader ? `${rental.reader.name} ${rental.reader.lastName}` : 'N/A' }}</td>
              <td>{{ rental.reader?.email || 'N/A' }}</td>
              <td>{{ formatDate(rental.rentalDate) }}</td>
              <td>{{ rental.returnDate ? 'Returned' : 'Active' }}</td>
              <td>
                <button 
                  v-if="!rental.returnDate" 
                  @click="returnBook(rental.id)"
                >
                  Return
                </button>
                <button @click="editRental(rental.id)">Edit</button>
                <button @click="deleteRental(rental.id)">Delete</button>
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
      
      <RentalForm 
        v-if="showRentModal" 
        @close="showRentModal = false" 
        @rent="handleRentBook"
      />
      <RentalForm 
        v-if="showEditModal" 
        :rental="currentRental"
        @close="showEditModal = false" 
        @save="handleUpdateRental"
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
        currentRental,
        isLoading,
        error,
        pagination,
        fetchRentals,
        fetchRental,
        rentBook,
        updateRental,
        returnBook,
        deleteRental
      } = useRentals()
      
      const showRentModal = ref(false)
      const showEditModal = ref(false)
      
      onMounted(fetchRentals)
      
      const handleRentBook = async (bookId, reader) => {
        await rentBook(bookId, reader)
        showRentModal.value = false
      }

      const editRental = async (id) => {
        await fetchRental(id)
        showEditModal.value = true
      }

      const handleUpdateRental = async (rentalData) => {
        await updateRental(currentRental.value.id, rentalData)
        showEditModal.value = false
      }

      const deleteRentalHandler = async (id) => {
        if (confirm('Are you sure you want to delete this rental record?')) {
          await deleteRental(id)
        }
      }

      const changePage = (page) => {
        if (page >= 1 && page <= Math.ceil(pagination.value.total / pagination.value.perPage)) {
          pagination.value.page = page
          fetchRentals()
        }
      } 

      const formatDate = (dateString) => {
        if (!dateString) return 'N/A'
        const date = new Date(dateString)
        return date.toLocaleDateString()
      }
      
      return {
        rentals,
        currentRental,
        isLoading,
        error,
        pagination,
        showRentModal,
        showEditModal,
        handleRentBook,
        editRental,
        handleUpdateRental,
        returnBook,
        deleteRental: deleteRentalHandler,
        changePage,
        formatDate
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