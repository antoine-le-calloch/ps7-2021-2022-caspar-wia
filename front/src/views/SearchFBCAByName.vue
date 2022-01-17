<template>
  <h1>Rechercher une ATFB</h1>
  <div>
    <div>
      <label>Nom : </label>
      <input v-model="lastName" name="lastName" size="40" />
    </div>
    <label>Prénom : </label>
    <input v-model="firstName" name="firstName" size="40" />
  </div>
  <div class="error" v-if="!isNameValid">
    Nom ou prénom invalide! Veuillez saisir un nom et prénom valide.
  </div>
  <div v-else><br></div>
  <button type="button" @click="generateName()">Générer nom/prenom existant</button>
  <button type="button" @click="searchFBCA()">Rechercher</button>
  <RouteButton v-bind:msg="'Annuler'" v-bind:route="'/'"></RouteButton>
</template>

<script lang="ts">
import RouteButton from "@/components/RouteButton.vue";
import { Options, Vue } from "vue-class-component";

@Options({
  components: {
    RouteButton
  },
})
export default class SearchFBCAByName extends Vue {
  private firstName: string = "";
  private lastName: string = "";
  private isNameValid: boolean = true;

  private generateName(): void {
    this.firstName = "antoine";
    this.lastName = "le calloch";
  }

  private searchFBCA(): void {
    if (this.lastName == "") {
      this.isNameValid = false;
    }
    else if (this.firstName == "")
      this.$router.push({ path: `/searchFBCAByName/FBCA/${this.lastName}` });
    else
      this.$router.push({path: `/searchFBCAByName/FBCA/${this.firstName}/${this.lastName}` });
  }
}
</script>

<style scoped>

</style>
