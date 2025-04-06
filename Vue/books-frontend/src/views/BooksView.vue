<template>
    <div class="books-view">
      <BookList />
    </div>
  </template>
  
  <script>
  import BookList from '@/components/BookList.vue'
  
  export default {
    components: { BookList }
  }
  </script>