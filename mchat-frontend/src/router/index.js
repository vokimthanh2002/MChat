import { createRouter, createWebHistory } from 'vue-router';
import Login from '../components/Login.vue';
import ChatRoom from '../components/ChatRoom.vue';

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/chat',
        name: 'ChatRoom',
        component: ChatRoom
    }
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
});

export default router;
