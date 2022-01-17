<template>
  <h1>Bienvenue sur PolyFrontier</h1>
  <button @click="$router.push('request')">Demander une ATFB</button>
  <br /><br />
  <button @click="$router.push('searchFBCAByPassportNumber')">
    Chercher une ATFB par numéro de passeport
  </button>
  <br /><br />
  <button @click="$router.push('searchFBCAByName')">
    Chercher une ATFB par nom et/ou prénom
  </button>
  <br /><br />
  <button @click="$router.push('consultFBCAByPassportNumber')">
    Consulter l'existence d'une ATFB valide
  </button>
  <br /><br />

  <br /><br />
  <hr />
  <br /><br />
  <h3>
    Connexion utilisateur : (Vous êtes connecté en tant que: {{ currentRole }})
  </h3>
  <button @click="removeToken()">USAGER</button>
  <button @click="setToken('67bae9af-ba32-4fa3-9eb3-b2fc122abf57')">
    DOUANIER
  </button>
  <button @click="setToken('2dbb2aab-8d79-4eed-863a-abcb7a5270a7')">
    POLICE
  </button>
</template>

<script lang="ts">
import { Vue } from "vue-class-component";

export default class Home extends Vue {
  private currentRole: string = "USAGER";

  created(): void {
    this.defineRole();
  }

  setToken(token: string): void {
    localStorage.setItem("token", token);
    this.defineRole();
  }
  removeToken(): void {
    localStorage.removeItem("token");
    this.defineRole();
  }

  defineRole(): void {
    const token = localStorage.getItem("token");
    if (token == "67bae9af-ba32-4fa3-9eb3-b2fc122abf57") {
      this.currentRole = "DOUANIER";
    } else if (token == "2dbb2aab-8d79-4eed-863a-abcb7a5270a7") {
      this.currentRole = "POLICIER";
    } else {
      this.currentRole = "USAGER";
    }
  }
}
</script>

<style scoped>

</style>
