<template>
    <header class="app-header">
      <h1>Book Management System</h1>
      <nav>
        <router-link to="/">Dashboard</router-link>
        <router-link to="/books">Books</router-link>
        <router-link to="/authors">Authors</router-link>
        <router-link to="/rentals">Rentals</router-link>
      </nav>
    </header>
  </template>
  
  <script>
  export default {
    name: 'AppHeader'
  }
  </script>
  
  <style scoped>
  .app-header {
    background-color: #2c3e50;
    color: white;
    padding: 1rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .app-header h1 {
    margin: 0;
    font-size: 1.5rem;
  }
  
  .app-header nav {
    display: flex;
    gap: 1rem;
  }
  
  .app-header a {
    color: white;
    text-decoration: none;
  }
  
  .app-header a.router-link-exact-active {
    font-weight: bold;
    text-decoration: underline;
  }
  </style>