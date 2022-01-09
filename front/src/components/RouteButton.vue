<template>
<button @click="goTo()">{{msg}}</button>
</template>

<script lang="ts">

import { goTo } from '@/utils/utils';
import { Vue } from 'vue-class-component';
import { Prop } from 'vue-property-decorator'

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
