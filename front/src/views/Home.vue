<template>
  <h1>Bienvenue sur PolyFrontier</h1>
  <button @click="$router.push('request')">Demander une ATFB</button>
  <br><br>
  <button @click="$router.push('searchATFB')">Chercher une ATFB par numéro de passeport</button>
</template>

<script lang="ts">

import { Vue } from 'vue-class-component';

export default class Home extends Vue {
}
</script>

<style scoped>

</style>
