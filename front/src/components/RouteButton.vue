<template>
  <button @click="goTo()">{{ msg }}</button>
</template>

<script lang="ts">
import { goTo } from "@/utils/utils";
import { Vue } from "vue-class-component";
import { Prop } from "vue-property-decorator";

/**
 * A button which permits to go to the given URL
 * The given message is displayed in the button
 */
export default class RouteButton extends Vue {
  @Prop() private msg!: string;
  @Prop() private route!: string;

  goTo(): void {
    goTo(this.$router, this.route);
  }
}
</script>

<style scoped>
</style>
