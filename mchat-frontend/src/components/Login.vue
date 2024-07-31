<template>
  <div>
    <h2>Login</h2>
    <input v-model="username" placeholder="Enter your username" />
    <button @click="login">Login</button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: ''
    };
  },
  methods: {
    login() {
      axios.post('http://localhost:8080/login', {username: this.username})
          .then(response => {
            this.$router.push('/chat');
          })
          .catch(error => {
            console.error(error);
          });
    }
  }
};
</script>