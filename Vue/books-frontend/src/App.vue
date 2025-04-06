<template>
  <div class="app-container">
    <AppHeader />
    <div class="main-content">
      <AppSidebar />
      <router-view />
    </div>
    <AppFooter />
  </div>
</template>

<script>
import AppHeader from '@/components/Layout/AppHeader.vue'
import AppSidebar from '@/components/Layout/AppSidebar.vue'
import AppFooter from '@/components/Layout/AppFooter.vue'

export default {
  components: {
    AppHeader,
    AppSidebar,
    AppFooter
  }
}
</script>

<style>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  display: flex;
  flex: 1;
}
</style>