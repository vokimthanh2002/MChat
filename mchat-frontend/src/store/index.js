// src/store/index.js
import Vue from 'vue';
import Vuex from 'vuex';
import api from '../api/api.js';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        token: localStorage.getItem('token') || '',
        user: null
    },
    mutations: {
        setToken(state, token) {
            state.token = token;
            localStorage.setItem('token', token);
        },
        setUser(state, user) {
            state.user = user;
        },
        logout(state) {
            state.token = '';
            state.user = null;
            localStorage.removeItem('token');
        }
    },
    actions: {
        async login({ commit }, credentials) {
            const response = await api.post('/auth/login', credentials);
            const token = response.data.token;
            commit('setToken', token);
        },
        async fetchUser({ commit }) {
            const response = await api.get('/auth/user');
            commit('setUser', response.data);
        },
        logout({ commit }) {
            commit('logout');
        }
    },
    getters: {
        isAuthenticated: state => !!state.token,
        user: state => state.user
    }
});
