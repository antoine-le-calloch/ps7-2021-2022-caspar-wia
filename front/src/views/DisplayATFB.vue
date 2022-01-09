<template>
  <div class="DisplayATFB" v-if="isFbcaFind">
    <div class="ATFB_card">
      <h1>ATFB</h1>
      <div>firstName : <span>{{this.fbca.firstName}}</span></div>
      <div>lastName : <span>{{this.fbca.lastName}}</span></div>
      <div>email : <span>{{this.fbca.email}}</span></div>
      <div>phone : <span>{{this.fbca.phone}}</span></div>
      <div>passportNumber : <span>{{this.fbca.passportNumber}}</span></div>
      <div>startDate : <span>{{this.fbca.startDate}}</span></div>
      <div>endDate : <span>{{this.fbca.endDate}}</span></div>
      <div>crossingReason : <span>{{this.fbca.crossingReason}}</span></div>
      <div>transportType : <span>{{this.fbca.transportType}}</span></div>
      <div>valid : <span>{{this.fbca.valid}}</span></div>
    </div>
  <RouteButton v-bind:msg="'Retour'" v-bind:route="'/searchATFB'" ></RouteButton>
  </div>
</template>

<script lang="ts">

import { Options, Vue } from 'vue-class-component';
import FBCAService from "@/service/FBCA.service";
import FBCA from "@/model/FBCA";
import RouteButton from '@/components/RouteButton.vue';

@Options({
  components: {
    RouteButton
  },
})
export default class DisplayATFB extends Vue {

  private fbca: FBCA = {firstName:'',lastName:'',email:'',phone:'',passportNumber:'',
    startDate:'',endDate:'',crossingReason:'',transportType:'',valid:false};
  private isFbcaFind = false;

  created(){
    const passportNumber: string = this.$route.params.passportNumber as string;
    FBCAService.getFBCAByPassportNumber(passportNumber).then( (valeurReturn) => {
      this.fbca = valeurReturn.data;
      if(!this.fbca)
        alert("Cette FBCA n'est pas valide");
      this.isFbcaFind = true;
    }).catch(error => {
      alert("Cette FBCA n'est pas valide");
      console.log(error);
      this.$router.push({ path: `/searchATFB` });
    });
  }
}
</script>

<style scoped>

.DisplayATFB{
  display: flex;
  justify-content: center;
  align-items: center;
  flex-flow: column nowrap;
}

.ATFB_card{
  background-color: powderblue;
  padding: 1vw;
  color: #2c3e50;
  width: 40%;
  height: 25%;
}

div{
  font: bold 1.7vw sans-serif;
  text-align: left;
  color: black;
}
span{
  color: #2c3e50;
  font-size: 1.5vw;
}
h1{
  text-align: center;
  font-size: 4vw;
  margin-top: 1vw;
}

</style>
