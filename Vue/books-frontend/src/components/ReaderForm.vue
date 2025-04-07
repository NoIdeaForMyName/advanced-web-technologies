<template>
    <div class="modal-overlay" @click.self="close">
      <div class="modal">
        <h2>{{ reader ? 'Edit Reader' : 'Add New Reader' }}</h2>
        
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label for="name">Name:</label>
            <input id="name" v-model="formData.name" required>
          </div>
          
          <div class="form-group">
            <label for="lastName">Last Name:</label>
            <input id="lastName" v-model="formData.lastName" required>
          </div>
          
          <div class="form-group">
            <label for="email">Email:</label>
            <input id="email" type="email" v-model="formData.email" required>
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
  import { ref } from 'vue'
  
  export default {
    props: {
      reader: {
        type: Object,
        default: null
      }
    },
    emits: ['close', 'save'],
    setup(props, { emit }) {
      const formData = ref({
        name: '',
        lastName: '',
        email: ''
      })
      
      if (props.reader) {
        formData.value = {
          name: props.reader.name,
          lastName: props.reader.lastName,
          email: props.reader.email
        }
      }
      
      const submitForm = () => {
        emit('save', formData.value)
      }
      
      const close = () => {
        emit('close')
      }
      
      return {
        formData,
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
  
  input {
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