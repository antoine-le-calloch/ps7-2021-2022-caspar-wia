<template>
  <h1>Rechercher une ATFB</h1>
  <div>
    <label>Passeport : </label>
    <input
      @keypress="numbersOnly($event)"
      v-model="passport2FirstNumbers"
      maxlength="2"
      placeholder="00"
      id="passport2FirstNumbers"
      name="passport2FirstNumbers"
      size="1"
    />
    <input
      @keypress="lettersOnly($event)"
      v-model="passport2letters"
      maxlength="2"
      placeholder="XX"
      id="passport2letters"
      name="passport2letters"
      size="1"
    />
    <input
      @keypress="numbersOnly($event)"
      v-model="passport5LastNumbers"
      maxlength="5"
      placeholder="00000"
      id="passport5LastNumbers"
      name="passport5LastNumbers"
      size="4"
    />
    <button type="button" @click="generatePassportId()">
      Scanner un passeport
    </button>
    <div class="error" v-if="!isPassportNumberValid">
      Numero de passeport invalide! Veuillez saisir un numero de passeport valide.
    </div>
    <div v-else><br></div>
  </div>
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
export default class SearchFBCAByPassportNumber extends Vue {
  private passport2FirstNumbers: string = "";
  private passport2letters: string = "";
  private passport5LastNumbers: string = "";
  private isPassportNumberValid: boolean = true;

  numbersOnly(evt: KeyboardEvent): void {
    const charCode: number = evt.key.charCodeAt(0);
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      evt.preventDefault();
    }
  }

  lettersOnly(evt: KeyboardEvent): void {
    const charCode: number = evt.key.charCodeAt(0);
    if (charCode > 31 && (charCode < 65 || charCode > 90)) {
      evt.preventDefault();
    }
  }

  private generatePassportId(): void {
    this.passport2FirstNumbers = "01";
    this.passport2letters = "AB";
    this.passport5LastNumbers = "12345";
  }

  private resetInput(): void {
    this.passport2FirstNumbers = "";
    this.passport2letters = "";
    this.passport5LastNumbers = "";
  }

  private searchFBCA(): void {
    if (
      this.passport2FirstNumbers.length != 2 ||
      this.passport2letters.length != 2 ||
      this.passport5LastNumbers.length != 5
    ) {
      this.isPassportNumberValid = false;
    } else {
      const passportNumber: string =
        this.passport2FirstNumbers +
        this.passport2letters +
        this.passport5LastNumbers;
      this.$router.push({ path: `/searchFBCAByPassportNumber/FBCA/${passportNumber}` });
    }
    this.resetInput();
  }
}
</script>

<style scoped>
.error{
  color: darkgrey;
  font-size: small;
}
</style>
