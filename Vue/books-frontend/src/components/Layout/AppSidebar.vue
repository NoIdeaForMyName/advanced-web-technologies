<template>
  <aside class="app-sidebar">
    <div class="sidebar-content">
      <h3>Quick Actions</h3>
      <ul>
        <li><button @click="navigateToAddBook">Add New Book</button></li>
        <li><button @click="navigateToAddAuthor">Add New Author</button></li>
        <li><button @click="navigateToAddReader">Add New Reader</button></li>
        <li><button @click="navigateToRentBook">Rent a Book</button></li>
      </ul>
      
      <h3>Statistics</h3>
      <ul>
        <li>Books: {{ stats.bookCount }}</li>
        <li>Authors: {{ stats.authorCount }}</li>
        <li>Readers: {{ stats.readerCount }}</li>
        <li>Active Rentals: {{ stats.activeRentals }}</li>
      </ul>
    </div>
  </aside>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import useBooks from '@/composables/useBooks'
import useAuthors from '@/composables/useAuthors'
import useReaders from '@/composables/useReaders'
import useRentals from '@/composables/useRentals'

export default {
  setup() {
    const router = useRouter()
    const stats = ref({
      bookCount: 0,
      authorCount: 0,
      readerCount: 0,
      activeRentals: 0
    })
    
    const { books, fetchBooks } = useBooks()
    const { authors, fetchAuthors } = useAuthors()
    const { readers, fetchReaders } = useReaders()
    const { rentals, fetchRentals } = useRentals()
    
    onMounted(async () => {
      await fetchBooks()
      await fetchAuthors()
      await fetchReaders()
      await fetchRentals()
      
      stats.value = {
        bookCount: books.value.length,
        authorCount: authors.value.length,
        readerCount: readers.value.length,
        activeRentals: rentals.value.filter(r => !r.returnDate).length
      }
    })

    const navigateToAddBook = () => {
      router.push('/books').then(() => {
        // Czekamy na załadowanie komponentu, następnie klikamy przycisk
        setTimeout(() => {
          const addButton = document.querySelector('.book-list button');
          if (addButton) {
            addButton.click();
          } else {
            console.warn('Add book button not found');
          }
        }, 100);
      });
    }

    const navigateToAddAuthor = () => {
      router.push('/authors').then(() => {
        setTimeout(() => {
          const addButton = document.querySelector('.author-list button');
          if (addButton) {
            addButton.click();
          }
        }, 100);
      });
    }

    const navigateToAddReader = () => {
      router.push('/readers').then(() => {
        setTimeout(() => {
          const addButton = document.querySelector('.reader-list button');
          if (addButton) {
            addButton.click();
          }
        }, 100);
      });
    }

    const navigateToRentBook = () => {
      router.push('/rentals').then(() => {
        setTimeout(() => {
          const rentButton = document.querySelector('.rental-list button');
          if (rentButton) {
            rentButton.click();
          }
        }, 100);
      });
    }
    
    return {
      stats,
      navigateToAddBook,
      navigateToAddAuthor,
      navigateToAddReader,
      navigateToRentBook
    }
  }
}
</script>
  
<style scoped>
  .app-sidebar {
    width: 250px;
    background-color: #f5f5f5;
    padding: 20px;
    border-right: 1px solid #ddd;
  }
  
  .sidebar-content {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  h3 {
    margin: 0 0 10px 0;
  }
  
  ul {
    list-style-type: none;
    padding: 0;
    margin: 0;
  }
  
  li {
    padding: 5px 0;
  }
  
  button {
    background: none;
    border: none;
    text-align: left;
    cursor: pointer;
    padding: 5px 0;
    width: 100%;
  }
  
  button:hover {
    text-decoration: underline;
  }
</style>