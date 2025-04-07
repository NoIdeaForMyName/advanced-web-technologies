<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal">
      <h2>Rent a Book</h2>
      
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="book">Book:</label>
          <select id="book" v-model="formData.bookId" required>
            <option value="">Select Book</option>
            <option 
              v-for="book in availableBooks" 
              :key="book.id" 
              :value="book.id"
              :selected="rental && book.id === rental.book.id"
            >
              {{ book.title }} ({{ book.author ? `${book.author.name} ${book.author.lastName}` : 'N/A' }})
            </option>
          </select>
        </div>

        <div class="form-group">
          <label for="reader">Reader:</label>
          <select id="reader" v-model="formData.readerId" required>
            <option value="">Select Reader</option>
            <option 
              v-for="reader in readers" 
              :key="reader.id" 
              :value="reader.id"
            >
              {{ reader.name }} {{ reader.lastName }} ({{ reader.email }})
            </option>
          </select>
        </div>

        <div v-if="rental" class="form-group">
          <label>Rental Date:</label>
          <p>{{ formatDate(rental.rentalDate) }}</p>
        </div>
        
        <div class="form-actions">
          <button type="button" @click="close">Cancel</button>
          <button type="submit">{{ rental ? 'Update' : 'Rent' }}</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import useBooks from '@/composables/useBooks'
import useReaders from '@/composables/useReaders'

export default {
  props: {
    rental: {
      type: Object,
      default: null
    }
  },
  emits: ['close', 'rent', 'save'],
  setup(props, { emit }) {
    const { books, fetchBooks } = useBooks()
    const { readers, fetchReaders } = useReaders()
    const formData = ref({
      bookId: '',
      readerId: '',
      returnDate: ''
    })
    
    onMounted(async () => {
      await fetchBooks()
      await fetchReaders()

      if (props.rental) {
        formData.value = {
          bookId: props.rental.book.id,
          readerId: props.rental.reader.id,
          returnDate: props.rental.returnDate || ''
        }
      }
    })

    const availableBooks = computed(() => {
      // Uwzględnij książkę, która jest już wypożyczona (dla edycji)
      return books.value.filter(book => 
        book.available || 
        (props.rental && book.id === props.rental.book.id)
      )
    })
    
    const submitForm = () => {
      if (props.rental) {
        emit('save', formData.value)
      } else {
        const selectedReader = readers.value.find(r => r.id === formData.value.readerId)
        emit('rent', formData.value.bookId, selectedReader)
      }
    }
    
    const close = () => {
      emit('close')
    }

    const formatDate = (dateString) => {
      if (!dateString) return 'N/A'
      const date = new Date(dateString)
      return date.toLocaleDateString()
    }
    
    return {
      formData,
      availableBooks,
      readers,
      submitForm,
      close,
      formatDate
    }
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 20px;
  border-radius: 5px;
  width: 500px;
  max-width: 90%;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input, select {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

button {
  padding: 8px 16px;
  cursor: pointer;
}
</style>