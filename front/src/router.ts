import { createWebHistory, createRouter } from "vue-router";

import SearchATFBByPassport from "@/views/SearchATFB.vue";
import DisplayATFB from "@/views/DisplayATFB.vue";
import Request from "@/views/Request.vue";
import Home from "@/views/Home.vue"

const routes = [
    { path: '/request', component: Request},
    { path: '/searchATFB', component: SearchATFBByPassport},
    { path: '/ATFB/:passportNumber', component: DisplayATFB},
    { path: '/', component: Home},
]

const router = createRouter({
    history: createWebHistory(),
    routes,
});


export default router;
