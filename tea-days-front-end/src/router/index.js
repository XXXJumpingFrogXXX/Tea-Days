import { createRouter, createWebHistory } from "vue-router";

import homePage from "@/components/pages/home-page";
import gamePage from "@/components/pages/game-page";
import adminPage from "@/components/pages/admin-page";
import adminUserPage from "@/components/pages/admin-user-page";
import articlePage from "@/components/pages/article-page";
import adminHandbookPage from "@/components/pages/admin-handbook-page";
import adminTutorialPage from "@/components/pages/admin-tutorial-page";
import adminBulletinPage from "@/components/pages/admin-bulletin-page";

const routes = [
    { path: '/', component: homePage, meta: { isLoginPage: true } },
    { path: '/login', redirect: '/' },
    { path: '/game', component: gamePage, meta: { requiresAuth: true, isLoginPage: false } },
    { path: '/admin', component: adminPage, meta: { requiresAuth: true, isLoginPage: false } },
    { path: '/admin', component: adminPage, meta: { requiresAuth: true, isLoginPage: false } },
    { path: '/article/:articleId', component: articlePage, meta: { requiresAuth: false, isLoginPage: false } },
    { path: '/admin/handbook', component: adminHandbookPage, meta: { requiresAuth: true, isLoginPage: false } },
    { path: '/admin/user', component: adminUserPage, meta: { requiresAuth: true, isLoginPage: false } },
    { path: '/admin/tutorial', component: adminTutorialPage, meta: { requiresAuth: true, isLoginPage: false } },
    { path: '/admin/bulletin', component: adminBulletinPage, meta: { requiresAuth: true, isLoginPage: false } },
]


const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth) && !router.$store.getters.isUserLogin) {
        next('/login')
    } else if (router.$store.getters.isUserLogin && to.matched.some(record => record.meta.isLoginPage)) {
        next('/game')
    } else {
        next()
    }
})


export default router
