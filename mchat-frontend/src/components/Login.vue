<template>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow-sm p-4" style="width: 25rem;">
      <h2 class="text-center mb-4">Login</h2>
      <form @submit.prevent="login">
        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" v-model="username" class="form-control" id="username" required />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" v-model="password" class="form-control" id="password" required />
        </div>
        <button type="submit" class="btn btn-primary btn-block">Login</button>
      </form>
      <div v-if="error" class="alert alert-danger mt-3">{{ error }}</div>
    </div>
  </div>
</template>

<script>
import api from "../api/api.js";

export default {
  data() {
    return {
      username: '',
      password: '',
      error: ''
    };
  },
  methods: {
    async login() {
      try {
        const response = await api.post('/auth/login', {
          username: this.username,
          password: this.password
        });
        const token = response.data.accessToken;
        console.log(token);
        localStorage.setItem('token', token);
        localStorage.setItem('username', response.data.username);
        this.$router.push('/chat'); // Chuyển hướng đến trang chủ sau khi đăng nhập thành công
      } catch (error) {
        this.error = 'Invalid username or password';
      }
    }
  }
};
</script>

<style scoped>
.min-vh-100 {
  min-height: 100vh;
}
</style>
