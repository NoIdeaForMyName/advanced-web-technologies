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
              v-for="book in books.filter(b => b.available)" 
              :key="book.id" 
              :value="book.id"
            >
              {{ book.title }} ({{ book.author ? `${book.author.name} ${book.author.lastName}` : 'N/A' }})
            </option>
          </select>
        </div>
        
        <div class="form-group">
          <label for="name">Your Name:</label>
          <input id="name" v-model="formData.reader.name" required>
        </div>
        
        <div class="form-group">
          <label for="email">Your Email:</label>
          <input id="email" type="email" v-model="formData.reader.email" required>
        </div>
        
        <div class="form-actions">
          <button type="button" @click="close">Cancel</button>
          <button type="submit">Rent Book</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import useBooks from '@/composables/useBooks'

export default {
  emits: ['close', 'rent'],
  setup(props, { emit }) {
    const { books, fetchBooks } = useBooks()
    const formData = ref({
      bookId: '',
      reader: {
        name: '',
        email: ''
      }
    })
    
    onMounted(async () => {
      await fetchBooks()
    })
    
    const submitForm = () => {
      emit('rent', formData.value.bookId, formData.value.reader)
    }
    
    const close = () => {
      emit('close')
    }
    
    return {
      formData,
      books,
      submitForm,
      close
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