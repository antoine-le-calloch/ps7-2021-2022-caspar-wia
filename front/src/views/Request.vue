<template>
  <h1>Formulaire d'autorisation de traverse Franco-Britannique</h1>
  <form ref="fbcaForm" v-on:submit.prevent="sendFBCA()">
    <label for="firstName">First name<span class="required">*</span> : </label>
    <input v-model="firstName" id="firstName" placeholder="First name" name="firstName" required="required"><br>
    <label for="lastName">Last name<span class="required">*</span> : </label>
    <input v-model="lastName" id="lastName" placeholder="Last name" name="lastName" required><br>
    <label for="email">Email<span class="required">*</span> : </label>
    <input type="email" v-model="email" id="email" placeholder="ex: example@email.com" name="email" required><br>
    <label for="phone">Phone number<span class="required">*</span> : </label>
    <input type="tel" pattern="[0-9]{10}" v-model="phone" id="phone" placeholder="ex: 0752567424" name="phone" required><br>
    <label for="passport">Passeport<span class="required">*</span> : </label>
    <input v-model="passportNumber" id="passport" name="passportNumber" pattern="[0-9]{2}[A-Z]{2}[0-9]{5}" required><button type="button" @click="generatePassportId()">Scanner mon passeport</button>
    <p>Quel type de transport allez-vous utiliser?<span class="required">*</span> </p>
    <input type="radio" v-model="transportType" id="car" name="transportType" value="car" required><label for="car">Car</label>
    <input type="radio" v-model="transportType" id="bike" name="transportType" value="bike"><label for="bike">Bike</label>
    <input type="radio" v-model="transportType" id="train" name="transportType" value="train"><label for="train">Train</label>
    <input type="radio" v-model="transportType" id="airplane" name="transportType" value="airplane"><label for="airplane">Airplane</label>
    <input type="radio" v-model="transportType" id="boat" name="transportType" value="boat"><label for="boat">Boat</label>
    <br>
    <label for="crossingReason">Quel est le motif de votre déplacement?<span class="required">*</span> </label>
    <select v-model="crossingReason" name="crossingReason" id="crossingReason" required>
      <option value="FAMILY">Famille</option>
      <option value="TOURISM">Tourisme</option>
      <option value="HOBBIES">Hobby</option>
      <option value="BUSINESS">Business</option>
      <option value="SPORT">Sport</option>
      <option value="EDUCATION">Education</option>
      <option value="HEALTH">Santé</option>
      <option value="ART">Art</option>
      <option value="CULTURE">Culture</option>
      <option value="OTHER">Autre</option>
    </select>
    <br>
    <label for="startDate">Date d'arrivée<span class="required">*</span> : </label>
    <input type="date" v-model="startDate" id="startDate" name="startDate" required>
    <br>
    <label for="endDate">Date de retour<span class="required">*</span> : </label>
    <input type="date" v-model="endDate" id="endDate" name="endDate" required>
    <br>
    <p><span class="required">*</span> Ces champs sont obligatoires</p>
    <input type="submit" value="Envoyer">
  </form>
  <RouteButton v-bind:msg="'Annuler'" v-bind:route="'/'" ></RouteButton>
</template>

<script lang="ts">
import FBCA from '@/model/FBCA';
import { Options, Vue } from 'vue-class-component';
import FBCAService from '@/service/FBCA.service'
import moment from 'moment'
import RouteButton from '@/components/RouteButton.vue';

@Options({
  components: {
    RouteButton
  },
})
export default class Request extends Vue {
  private firstName : string = ''
  private lastName : string = ''
  private email : string = ''
  private phone : string = ''
  private passportNumber : string = ''
  private transportType : string = ''
  private crossingReason : string = ''
  private endDate : string = ''
  private startDate : string = ''

  private generatePassportId() : void {
    this.passportNumber = "01AB12345"
  }

  sendFBCA(): void {
    const fbca: FBCA = { firstName: this.firstName, lastName: this.lastName, email: this.email, phone: this.phone,
                        passportNumber: this.passportNumber, startDate: moment(this.startDate).format('YYYY-MM-DD'), endDate: moment(this.endDate).format('YYYY-MM-DD'),
                        crossingReason: this.crossingReason.toUpperCase(),
                        transportType:  this.transportType.toUpperCase(),
                        valid: true 
                        };
    FBCAService.addFBCA(fbca).then( () => {
      alert("Demande envoyée et acceptée ! ");
      (this.$refs['fbcaForm'] as any).reset();
    })
    .catch(error => {
        console.error('There was an error!', error);
    });
  }
}
</script>

<style scoped>
.required {
  color: red;
}

p {
  font-style: italic;
}
</style>
