<template>
  <div id="app" class="small-container">
    <h1>Znajomi</h1>

    <person-form @add:person="addPerson"/>
    <persons-table :personsSource="persons"/>
  </div>
</template>

<script>
  import PersonsTable from '@/components/PersonsTable.vue'
  import PersonForm from '@/components/PersonForm.vue'

  export default {
    name: 'app',
    components: {
      PersonsTable,
      PersonForm,
    },
    data() {
      return {
        // Stare
        /*
        persons: [
          {
            id: 1,
            name: 'Adam Słodowy',
            email: 'adam.slodowy@zrobtosam.pl',
            phone: '+48 787 774 664'
          },
          {
            id: 2,
            name: 'Michał Studencki',
            email: 'ms@student.pwr.edu.pl',
            phone: '+48 600 565 454'
          },
          { 
            id: 3,
            name: 'Kamila Napokaz',
            email: 'kami2003@h2.pl',
            phone: '+48 609 554 987'
          },
        ]
        */
        // Nowe
        persons: []
      }
    },

    methods: {
      addPerson(person) {
        this.persons = [...this.persons, person]
      },

      // Nowe
      async getPersons() {
        try {
          const response = await fetch('https://jsonplaceholder.typicode.com/users')
          const data = await response.json()
          this.persons = data
        } catch (error) {
          console.error(error)
        }
      }, 
    },

    // Nowe
    mounted() {
      this.getPersons()
    }, 
  }
</script>

<style>
  button {
    background: #009435;
    border: 1px solid #009435;
  }
  .small-container {
    max-width: 680px;
  }
</style>
