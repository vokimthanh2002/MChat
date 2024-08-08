<template>
  <div class="login">
    <h2>Login</h2>
    <div class="form-group">
      <label for="username">Username</label>
    <input v-model="username" placeholder="Enter your username" />
    </div>
    <div class="form-group">
      <label for="password">Password</label>
      <input v-model="Password" placeholder="Enter your password"/>
    </div>
    <button @click="login">Login</button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
