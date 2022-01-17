<template>
  <div class="DisplayFBCA" v-if="isFBCAFind">
    <DisplayFBCA v-bind:fbca="this.fbca"></DisplayFBCA>
    <RouteButton
      v-bind:msg="'Retour'"
      v-bind:route="'/searchFBCAByPassportNumber'"
    ></RouteButton>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import FBCAService from "@/service/FBCA.service";
import RouteButton from "@/components/RouteButton.vue";
import FBCA from "@/model/FBCA";
import DisplayFBCA from "@/views/DisplayFBCA.vue";

@Options({
  components: {
    DisplayFBCA,
    RouteButton,
  },
})
export default class GetFBCAWithPassportNumber extends Vue {
  private fbca: FBCA = {
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    passportNumber: "",
    expirationDate: "",
    crossingReason: "",
    transportType: "",
    valid: false,
    state: "",
  };
  private isFBCAFind = false;

  created() {
    const passportNumber: string = this.$route.params.passportNumber as string;
    this.recoverFBCAByPassportNumber(passportNumber);
  }

  recoverFBCAByPassportNumber(passportNumber: string): void {
    FBCAService.getFBCAByPassportNumber(passportNumber)
      .then((returnValue) => {
        this.processReturnValue(returnValue);
      })
      .catch(() => {
        this.processError();
      });
  }

  processReturnValue(returnValue: any): void {
    this.fbca = returnValue.data;
    this.isFBCAFind = true;
  }

  processError(): void {
    alert("Ce numéro de passeport ne correspond à aucune ATFB");
    this.$router.push({ path: `/searchFBCAByPassportNumber` });
  }
}
</script>

<style scoped>
.DisplayFBCA {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-flow: column nowrap;
}
</style>
