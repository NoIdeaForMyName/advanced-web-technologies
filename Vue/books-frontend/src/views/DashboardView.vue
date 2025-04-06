<template>
  <div class="dashboard">
    <h1>Book Management System</h1>
    
    <div class="stats">
      <div class="stat-card">
        <h3>Total Books</h3>
        <p>{{ bookCount }}</p>
      </div>
      <div class="stat-card">
        <h3>Total Authors</h3>
        <p>{{ authorCount }}</p>
      </div>
      <div class="stat-card">
        <h3>Active Rentals</h3>
        <p>{{ activeRentals }}</p>
      </div>
    </div>
    
    <div class="recent-activity">
      <h2>Recent Activity</h2>
      <div v-if="isActivitiesLoading" class="loading">
        Loading activity...
      </div>
      <div v-else-if="activitiesError" class="error">
        {{ activitiesError }}
      </div>
      <ul v-else-if="activityList.length">
        <li v-for="(activity, index) in activityList" :key="index">
          <span class="timestamp">{{ formatDate(activity.timestamp) }}</span>
          {{ activity.message }}
        </li>
      </ul>
      <div v-else class="no-activities">
        No recent activity found
      </div>
    </div>
  </div>
</template>
  
<script>
  import { ref, onMounted } from 'vue'
  import useBooks from '../composables/useBooks'
  import useAuthors from '../composables/useAuthors'
  import useRentals from '../composables/useRentals'
  import useActivities from '../composables/useActivities'
  
  export default {
    setup() {
      const bookCount = ref(0)
      const authorCount = ref(0)
      const activeRentals = ref(0)
      
      const { books, fetchBooks } = useBooks()
      const { authors, fetchAuthors } = useAuthors()
      const { rentals, fetchRentals } = useRentals()
      const { 
        activities: activityList, 
        isLoading: isActivitiesLoading, 
        error: activitiesError,
        fetchRecentActivities 
      } = useActivities()

      const formatDate = (timestamp) => {
        return new Date(timestamp).toLocaleString()
      }
      
      onMounted(async () => {
        await fetchBooks()
        await fetchAuthors()
        await fetchRentals()
        await fetchRecentActivities(5)
        
        bookCount.value = books.value.length
        authorCount.value = authors.value.length
        activeRentals.value = rentals.value.filter(r => !r.returned).length
      })
      
      return {
        bookCount,
        authorCount,
        activeRentals,
        activityList,
        isActivitiesLoading,
        activitiesError,
        formatDate
      }
    }
  }
</script>
  
<style scoped>
  .dashboard {
    padding: 20px;
  }
  
  .stats {
    display: flex;
    gap: 20px;
    margin: 20px 0;
  }
  
  .stat-card {
    border: 1px solid #ddd;
    border-radius: 5px;
    padding: 15px;
    flex: 1;
    text-align: center;
  }
  
  .stat-card h3 {
    margin-top: 0;
  }
  
  .recent-activity {
    margin-top: 30px;
  }
  
  .recent-activity ul {
    list-style-type: none;
    padding: 0;
  }
  
  .recent-activity li {
    padding: 8px 0;
    border-bottom: 1px solid #eee;
    display: flex;
    gap: 10px;
    align-items: center;
  }

  .timestamp {
    color: #666;
    font-size: 0.8em;
    min-width: 160px;
  }

  .loading, .error, .no-activities {
    padding: 10px;
    border-radius: 4px;
  }

  .loading {
    background-color: #f0f0f0;
    color: #555;
  }

  .error {
    background-color: #ffebee;
    color: #d32f2f;
  }

  .no-activities {
    color: #666;
    font-style: italic;
  }
</style>