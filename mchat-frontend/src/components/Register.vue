<template>
  <div class="container d-flex justify-content-center align-items-center min-vh-100 bg-light">
    <div class="card shadow-lg p-5" style="width: 28rem;">
      <h2 class="text-center mb-4">Sign Up</h2>
      <form @submit.prevent="signUp">
        <div class="form-group mb-3">
          <label for="email">
            <i class="fas fa-envelope"></i> Email
          </label>
          <input
              type="email"
              v-model="email"
              class="form-control"
              id="email"
              placeholder="Enter your email"
              required
          />
        </div>
        <div class="form-group mb-3">
          <label for="username">
            <i class="fas fa-user"></i> Username
          </label>
          <input
              type="text"
              v-model="username"
              class="form-control"
              id="username"
              placeholder="Enter your username"
              required
          />
        </div>
        <div class="form-group mb-3">
          <label for="password">
            <i class="fas fa-lock"></i> Password
          </label>
          <input
              type="password"
              v-model="password"
              class="form-control"
              id="password"
              placeholder="Enter your password"
              required
          />
        </div>
        <div class="form-group mb-4">
          <label for="confirmPassword">
            <i class="fas fa-lock"></i> Confirm Password
          </label>
          <input
              type="password"
              v-model="confirmPassword"
              class="form-control"
              id="confirmPassword"
              placeholder="Confirm your password"
              required
          />
        </div>
        <button type="submit" class="btn btn-success btn-block">
          <i class="fas fa-user-plus"></i> Sign Up
        </button>
        <div v-if="error" class="alert alert-danger mt-3">{{ error }}</div>
      </form>
      <hr />
      <div class="text-center">
        <p>Already have an account? <a href="#" @click.prevent="goToLogin">Login</a></p>
      </div>
    </div>
  </div>
</template>

<script>
import api from "../api/api.js";
import Swal from 'sweetalert2'

export default {
  data() {
    return {
      email: '',
      username: '',
      password: '',
      confirmPassword: '',
      error: ''
    };
  },
  methods: {
    async signUp() {
      if (this.password !== this.confirmPassword) {
        this.error = "Passwords do not match!";
        Swal.fire({
          position: "center",
          icon: "error",
          title: "Mật khẩu không khớp!",
          showConfirmButton: false,
          timer: 1500
        });
        return;
      }

      try {
        const response = await api.post('/auth/register', {
          email: this.email,
          username: this.username,
          password: this.password
        });
        console.log(response.data);
        this.$router.push('/'); // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
            Swal.fire({
              position: "center",
              icon: "success",
              title: "Thành công",
              showConfirmButton: false,
              timer: 1500
            });

      } catch (error) {
            Swal.fire({
              position: "center",
              icon: "error",
              title: "Thất bại",
              showConfirmButton: false,
              timer: 1500
            });
        this.error = 'Registration failed. Please try again.';
      }
    },
    goToLogin() {
      // Chuyển hướng đến trang đăng nhập
      this.$router.push('/');
    }
  }
};
</script>

<style scoped>
.min-vh-100 {
  min-height: 100vh;
}

.card {
  border-radius: 1rem;
}

.btn-block {
  border-radius: 2rem;
}

.bg-light {
  background-color: #f8f9fa !important;
}

.fas {
  margin-right: 10px;
}
</style>
