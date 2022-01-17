<template>
  <div class="DisplayFBCA" v-if="isFBCAFind">
    <div class="fbcas" v-bind:key="fbca" v-for="fbca of this.fbcas">
      <DisplayFBCA v-bind:fbca="fbca"></DisplayFBCA>
    </div>
    <RouteButton
      v-bind:msg="'Retour'"
      v-bind:route="'/searchFBCAByName'"
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
    RouteButton,
    DisplayFBCA
  },
})
export default class GetFBCAWithName extends Vue {
  private fbcas: FBCA[] = [];
  private isFBCAFind = false;
  private declinedReason: string = "";
  private hasBeenDeclined = false;

  created() {
    const firstName: string = this.$route.params.firstName as string;
    const lastName: string = this.$route.params.lastName as string;
    this.recoverFBCAByFirstAndLastName(firstName, lastName);
  }

  recoverFBCAByFirstAndLastName(firstName: string, lastName: string): void {
    FBCAService.getFBCAByFirstNameAndLastName(firstName ?? "", lastName)
      .then((returnValue) => {
        this.processReturnValue(returnValue);
      })
      .catch(() => {
        this.processError();
      });
  }

  processReturnValue(returnValue: any): void {
    this.fbcas = returnValue.data;
    if (this.fbcas.length == 0) {
      this.processError();
    }
    this.isFBCAFind = true;
  }

  processError() : void {
    alert("Ce nom et prénom ne correspond à aucune ATFB");
    this.$router.push({path: `/searchFBCAByName`});
  }
}
</script>

<style scoped>

.DisplayFBCA{
  display: flex;
  justify-content: center;
  align-items: center;
  flex-flow: column nowrap;
}

.fbcas {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-flow: column nowrap;
  width: 100%;
}
</style>
