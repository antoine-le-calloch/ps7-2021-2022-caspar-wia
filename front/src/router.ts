import { createWebHistory, createRouter } from "vue-router";

import SearchFBCAByPassportNumber from "@/views/SearchFBCAByPassportNumber.vue";
import SearchFBCAByName from "@/views/SearchFBCAByName.vue";
import GetFBCAWithPassportNumber from "@/views/GetFBCAWithPassportNumber.vue";
import GetFBCAWithName from "@/views/GetFBCAWithName.vue";
import Request from "@/views/Request.vue";
import Home from "@/views/Home.vue"
import ConsultFBCAByPassportNumber from "@/views/ConsultFBCAByPassportNumber.vue";

const routes = [
    { path: '/request', component: Request},
    { path: '/consultFBCAByPassportNumber', component: ConsultFBCAByPassportNumber},
    { path: '/searchFBCAByPassportNumber', component: SearchFBCAByPassportNumber},
    { path: '/searchFBCAByName', component: SearchFBCAByName},
    { path: '/searchFBCAByPassportNumber/FBCA/:passportNumber', component: GetFBCAWithPassportNumber},
    { path: '/searchFBCAByName/FBCA/:firstName/:lastName', component: GetFBCAWithName},
    { path: '/searchFBCAByName/FBCA/:lastName', component: GetFBCAWithName},
    { path: '/', component: Home},
    { path: "/:catchAll(.*)", redirect: '/',}
]

const router = createRouter({
    history: createWebHistory(),
    routes,
});


export default router;
