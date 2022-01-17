<template>
  <h1>Formulaire d'autorisation de traverse Franco-Britannique</h1>
  <form ref="fbcaForm" v-on:submit.prevent="sendFBCA()">
    <label for="firstName">First name<span class="required">*</span> : </label>
    <input
      v-model="firstName"
      id="firstName"
      placeholder="First name"
      name="firstName"
      required="required"
    /><br />
    <label for="lastName">Last name<span class="required">*</span> : </label>
    <input
      v-model="lastName"
      id="lastName"
      placeholder="Last name"
      name="lastName"
      required
    /><br />
    <label for="email">Email<span class="required">*</span> : </label>
    <input
      type="email"
      v-model="email"
      id="email"
      placeholder="ex: example@email.com"
      name="email"
      required
    /><br />
    <label for="phone">Phone number<span class="required">*</span> : </label>
    <input
      type="tel"
      pattern="[0-9]{10}"
      v-model="phone"
      id="phone"
      placeholder="ex: 0752567424"
      name="phone"
      required
    /><br />
    <label for="passport">Passeport<span class="required">*</span> : </label>
    <input
      v-model="passportNumber"
      id="passport"
      name="passportNumber"
      pattern="[0-9]{2}[A-Z]{2}[0-9]{5}"
      required
    /><button type="button" @click="generatePassportId()">
      Scanner mon passeport
    </button>
    <p>
      Quel type de transport allez-vous utiliser?<span class="required">*</span>
    </p>
    <input
      type="radio"
      v-model="transportType"
      id="car"
      name="transportType"
      value="car"
      required
    /><label for="car">Car</label>
    <input
      type="radio"
      v-model="transportType"
      id="bike"
      name="transportType"
      value="bike"
    /><label for="bike">Bike</label>
    <input
      type="radio"
      v-model="transportType"
      id="train"
      name="transportType"
      value="train"
    /><label for="train">Train</label>
    <input
      type="radio"
      v-model="transportType"
      id="airplane"
      name="transportType"
      value="airplane"
    /><label for="airplane">Airplane</label>
    <input
      type="radio"
      v-model="transportType"
      id="boat"
      name="transportType"
      value="boat"
    /><label for="boat">Boat</label>
    <br />
    <label for="crossingReason"
      >Quel est le motif de votre déplacement?<span class="required">*</span>
    </label>
    <select
      v-model="crossingReason"
      name="crossingReason"
      id="crossingReason"
      required
    >
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
    <br />
    <h2>Déclaration de marchandises</h2>
    <p>
      Pour plus d'information sur l'assujettissement à la déclaration de
      marchandises, <br />
      veuillez consulter
      <a
        href="https://www.douane.gouv.fr/sites/default/files/documentation/pdf/voyagez-tranquille.pdf"
        target="_blank"
        rel="noopener noreferrer"
        >cette page</a
      >
    </p>
    <p>
      Voir
      <a
        href="http://www.wcoomd.org/fr/topics/nomenclature/instrument-and-tools/hs-nomenclature-2022-edition/hs-nomenclature-2022-edition.aspx"
        target="_blank"
        rel="noopener noreferrer"
        >ici</a
      >
      pour les détails sur les types de marchandises
    </p>
    <div v-for="(freight, index) of freights" :key="freight">
      <span>Marchandise n°{{ index + 1 }}</span
      ><br />
      <span>Type<span class="required">*</span> : </span>
      <select
        v-model="freight.type"
        name="freightType"
        id="freightType"
        required
      >
        <option value="S1">ANIMAUX VIVANTS ET PRODUITS DU REGNE ANIMAL</option>
        <option value="S2">PRODUITS DU REGNE VEGETAL</option>
        <option value="S3">
          GRAISSES ET HUILES ANIMALES, VEGETALES OU D’ORIGINE MICROBIENNE
        </option>
        <option value="S4">PRODUITS DES INDUSTRIES ALIMENTAIRES</option>
        <option value="S5">PRODUITS MINERAUX</option>
        <option value="S6">
          PRODUITS DES INDUSTRIES CHIMIQUES OU DES INDUSTRIES CONNEXES
        </option>
        <option value="S7">
          MATIERES PLASTIQUES ET OUVRAGES EN CES MATIERES
        </option>
        <option value="S8">
          PEAUX, CUIRS, PELLETERIES ET OUVRAGES EN CES MATIERES
        </option>
        <option value="S9">BOIS, CHARBON DE BOIS ET OUVRAGES EN BOIS</option>
        <option value="S10">
          PATES DE BOIS OU D\'AUTRES MATIERES FIBREUSES CELLULOSIQUES
        </option>
        <option value="S11">
          MATIERES TEXTILES ET OUVRAGES EN CES MATIERES
        </option>
        <option value="S12">
          CHAUSSURES, COIFFURES, PARAPLUIES, PARASOLS, CANNES, FOUETS, CRAVACHES
          ET LEURS PARTIES
        </option>
        <option value="S13">
          OUVRAGES EN PIERRES, PLATRE, CIMENT, AMIANTE, MICA OU MATIERES
          ANALOGUES
        </option>
        <option value="S14">
          PERLES FINES OU DE CULTURE, PIERRES GEMMES OU SIMILAIRES, METAUX
          PRECIEUX, ET OUVRAGES EN CES MATIERES
        </option>
        <option value="S15">METAUX COMMUNS ET OUVRAGES EN CES METAUX</option>
        <option value="S16">
          MACHINES ET APPAREILS, MATERIEL ELECTRIQUE ET LEURS PARTIES
        </option>
        <option value="S17">MATERIEL DE TRANSPORT</option>
        <option value="S18">
          INSTRUMENTS ET APPAREILS D'OPTIQUE, DE PHOTOGRAPHIE OU DE
          CINEMATOGRAPHIE
        </option>
        <option value="S19">
          ARMES, MUNITIONS ET LEURS PARTIES ET ACCESSOIRES
        </option>
        <option value="S20">MARCHANDISES ET PRODUITS DIVERS</option>
        <option value="S21">OBJETS D'ART, DE COLLECTION OU D'ANTIQUITE</option>
      </select>
      <br />
      <span>Nom<span class="required">*</span> : </span
      ><input v-model="freight.name" type="text" required />
      <span>Poids(en kg)<span class="required">*</span> : </span
      ><input v-model="freight.weight" type="number" required />
    </div>
    <button type="button" @click="addFreight()">Ajouter une marchandise</button>
    <button
      type="button"
      v-if="this.freights.length > 0"
      @click="removeFreight()"
    >
      Supprimer une marchandise
    </button>
    <p><span class="required">*</span> Ces champs sont obligatoires</p>
    <input type="submit" value="Envoyer" />
  </form>
  <RouteButton v-bind:msg="'Annuler'" v-bind:route="'/'"></RouteButton>
</template>

<script lang="ts">
import FBCA from "@/model/FBCA";
import { Options, Vue } from "vue-class-component";
import FBCAService from "@/service/FBCA.service";
import moment from "moment";
import RouteButton from "@/components/RouteButton.vue";
import { Freight } from "@/model/Freight";

@Options({
  components: {
    RouteButton,
  },
})
export default class Request extends Vue {
  private firstName: string = "";
  private lastName: string = "";
  private email: string = "";
  private phone: string = "";
  private passportNumber: string = "";
  private transportType: string = "";
  private crossingReason: string = "";
  private freights: Freight[] = [];

  private generatePassportId(): void {
    this.passportNumber = "01AB12345";
  }

  sendFBCA(): void {
    const currentDate = new Date();
    const fbca: FBCA = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      phone: this.phone,
      passportNumber: this.passportNumber,
      expirationDate: moment(
        currentDate.setDate(currentDate.getDate() + 15)
      ).format("YYYY-MM-DD"),
      crossingReason: this.crossingReason.toUpperCase(),
      transportType: this.transportType.toUpperCase(),
      valid: true,
      state: "WAITING_FOR_APPROVAL",
      freights: this.freights.length > 0 ? this.freights : undefined,
    };
    FBCAService.addFBCA(fbca)
      .then(() => {
        alert("Demande envoyée et acceptée ! ");
        (this.$refs["fbcaForm"] as any).reset();
      })
      .catch((error) => {
        console.error("There was an error!", error);
      });
  }

  addFreight(): void {
    const freight: Freight = {
      type: "",
      name: "",
      weight: 0,
    };
    this.freights.push(freight);
  }

  removeFreight(): void {
    this.freights.pop();
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
