<template>
  <div class="container d-flex justify-content-center align-items-center min-vh-100 bg-light">
    <div class="card shadow-lg p-5" style="width: 28rem;">
      <h2 class="text-center mb-4">Login</h2>
      <form @submit.prevent="login">
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
        <div class="form-group mb-4">
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
        <button type="submit" class="btn btn-primary btn-block">
          <i class="fas fa-sign-in-alt"></i> Login
        </button>
        <div v-if="error" class="alert alert-danger mt-3">{{ error }}</div>
      </form>
      <div class="text-center mt-3">
        <a href="#" @click.prevent="forgotPassword">Forgot Password?</a>
      </div>
      <hr />
      <div class="text-center">
        <button class="btn btn-danger btn-block mb-2" @click="loginWithGoogle">
          <i class="fab fa-google"></i> Login with Google
        </button>
        <button class="btn btn-primary btn-block" @click="loginWithFacebook">
          <i class="fab fa-facebook-f"></i> Login with Facebook
        </button>
      </div>
      <hr />
      <div class="text-center">
        <p>Don't have an account? <a href="#" @click.prevent="register">Sign up</a></p>
      </div>
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
        this.$router.push('/chat'); // Chuyển hướng đến trang chat sau khi đăng nhập thành công
      } catch (error) {
        this.error = 'Invalid username or password';
      }
    },
    loginWithGoogle() {
      // Xử lý logic đăng nhập với Google
    },
    loginWithFacebook() {
      // Xử lý logic đăng nhập với Facebook
    },
    forgotPassword() {
      // Xử lý logic quên mật khẩu
    },
    register() {
      // Chuyển hướng đến trang đăng ký
      this.$router.push('/register');
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

.fas, .fab {
  margin-right: 10px;
}
</style>
