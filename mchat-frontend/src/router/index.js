import { createRouter, createWebHistory } from 'vue-router';
import Login from '../components/Login.vue';
import ChatRoom from '../components/ChatRoom.vue';

const routes = [
    {
        path: '',
        name: 'Login',
        component: Login
    },
    {
        path: '/chat',
        component: ChatRoom,
        meta: { requiresAuth: true }
    }
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
});

router.beforeEach((to, from, next) => {
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
    const token = localStorage.getItem('token');

    if (requiresAuth && !token) {
        next('/login');
    } else {
        next();
    }
});

export default router;
