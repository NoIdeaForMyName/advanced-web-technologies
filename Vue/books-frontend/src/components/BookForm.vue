<template>
    <div class="modal-overlay" @click.self="close">
      <div class="modal">
        <h2>{{ book ? 'Edit Book' : 'Add New Book' }}</h2>
        
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label for="title">Title:</label>
            <input id="title" v-model="formData.title" required>
          </div>
          
          <div class="form-group">
            <label for="author">Author:</label>
            <select id="author" v-model="formData.authorId" required>
              <option value="">Select Author</option>
              <option v-for="author in authors" :key="author.id" :value="author.id">
                {{ author.name }} {{ author.lastName }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label for="pages">Pages:</label>
            <input id="pages" type="number" v-model="formData.pages" required>
          </div>
          
          <div class="form-actions">
            <button type="button" @click="close">Cancel</button>
            <button type="submit">Save</button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue'
  import useAuthors from '@/composables/useAuthors'
  
  export default {
    props: {
      book: {
        type: Object,
        default: null
      }
    },
    emits: ['close', 'save'],
    setup(props, { emit }) {
      const { authors, fetchAuthors } = useAuthors()
      const formData = ref({
        title: '',
        authorId: '',
        pages: 0
      })
      
      onMounted(async () => {
        await fetchAuthors()
        if (props.book) {
          formData.value = {
            title: props.book.title,
            authorId: props.book.author?.id || '',
            pages: props.book.pages
          }
        }
      })
      
      const submitForm = () => {
        emit('save', formData.value)
      }
      
      const close = () => {
        emit('close')
      }
      
      return {
        formData,
        authors,
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