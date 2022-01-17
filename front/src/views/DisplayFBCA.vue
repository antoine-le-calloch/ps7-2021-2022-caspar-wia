<template>
  <div class="ATFB_card">
    <h1>ATFB</h1>
    <div>
      Prénom : <span>{{ this.fbca.firstName }}</span>
    </div>
    <div>
      Nom : <span>{{ this.fbca.lastName }}</span>
    </div>
    <div>
      Mail : <span>{{ this.fbca.email }}</span>
    </div>
    <div>
      Téléphone : <span>{{ this.fbca.phone }}</span>
    </div>
    <div>
      Passeport : <span>{{ this.fbca.passportNumber }}</span>
    </div>
    <div>
      Date d'expiration : <span>{{ this.fbca.expirationDate }}</span>
    </div>
    <div>
      Raison du passage : <span>{{ this.fbca.crossingReason }}</span>
    </div>
    <div>
      Type de transport : <span>{{ this.fbca.transportType }}</span>
    </div>
    <div>
      Passport valide : <span>{{ this.fbca.valid }}</span>
    </div>
    <div>
      État de l'ATFB : <span>{{ this.fbca.state }}</span>
    </div>
    <div v-if="this.fbca.declinedReason">
      Raison du refus de passage : <span>{{ this.fbca.declinedReason }}</span>
    </div>
    <div class="freights_card" v-if="this.fbca.freights.length > 0">
      <h3>Déclaration de marchandises</h3>
      <table id="declaration">
        <tr>
          <th>Nom</th>
          <th>Type</th>
          <th>Poids</th>
        </tr>
        <tr v-for="freight of this.fbca.freights" :key="freight">
          <td>{{ freight.name }}</td>
          <td>{{ freight.type }}</td>
          <td>{{ freight.weight }}</td>
        </tr>
      </table>
    </div>
    <div v-else>Pas de marchandise.</div>
  </div>
  <button v-if="!hasBeenValidated()" @click="updateState(true)">
    Accepter la demande
  </button>
  <button v-if="!hasBeenDeclined()" @click="declinedHasBeenClick = true">
    Refuser la demande
  </button>
  <div v-if="declinedHasBeenClick">
    <label for="declinedReason"
      >Raison du refus<span class="required">*</span> :
    </label>
    <button @click="declinedHasBeenClick = false">Annuler</button>
    <input
      v-model="declinedReason"
      id="declinedReason"
      name="declinedReason"
      minlength="4"
      placeholder="ex: ATFB invalide"
      required
    />
    <button
      :disabled="declinedReason.length < 4"
      @click="
        updateState(false);
        declinedHasBeenClick = false;
      "
    >
      Valider
    </button>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import FBCA from "@/model/FBCA";
import RouteButton from "@/components/RouteButton.vue";
import { Prop } from "vue-property-decorator";
import FBCAService from "@/service/FBCA.service";

/**
 * The FBCA's view
 */
@Options({
  components: {
    RouteButton,
  },
})
export default class DisplayFBCA extends Vue {
  @Prop() fbca: FBCA = {
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
    freights: [],
  };

  private declinedReason: string = "";
  private declinedHasBeenClick = false;

  hasBeenValidated(): boolean {
    return this.fbca.state != null && this.fbca.state == "ACCEPTED";
  }

  hasBeenDeclined(): boolean {
    return this.fbca.state != null && this.fbca.state == "DECLINED";
  }

  updateState(isValid: boolean): void {
    this.fbca.declinedReason = isValid ? undefined : this.declinedReason;
    this.fbca.state = isValid ? "ACCEPTED" : "DECLINED";
    FBCAService.updateFBCA(this.fbca.id!, this.fbca)
      .then(() => {
        alert("L'ATFB a bien été " + (isValid ? "acceptée" : "refusée") + " !");
      })
      .catch((error) => {
        alert("Une erreur est survenue lors de l'enregistrement");
        console.log(error);
      });
  }
}
</script>

<style scoped>
.ATFB_card {
  background-color: powderblue;
  padding: 1vw;
  color: #2c3e50;
  width: 40%;
  height: 25%;
}

.freights_card {
  background-color: #eedc95;
  margin: 1vw;
  padding: 1vw;
  width: 80%;
  text-align: center;
}

table {
  width: 100%;
  height: 100%;
  border-collapse: collapse;
}

table,
th,
tr {
  border: 1px solid black;
}

th {
  background-color: #ecc757;
}

div {
  font: bold 1.7vw sans-serif;
  text-align: left;
  color: black;
}
span {
  color: #2c3e50;
  font-size: 1.5vw;
}
h1 {
  text-align: center;
  font-size: 4vw;
  margin-top: 1vw;
}
</style>
