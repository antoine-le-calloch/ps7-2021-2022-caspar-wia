import { Router } from "vue-router";

export function goTo(router: Router, url: string) {
    router.push({ path: url });
}