import { Router } from "vue-router";

/**
 * Switch the app to the given url
 * @param router The app's router
 * @param url The path to the new url
 */
export function goTo(router: Router, url: string): void {
    router.push({ path: url });
}