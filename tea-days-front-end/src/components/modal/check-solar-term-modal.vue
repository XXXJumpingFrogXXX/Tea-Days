<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="solarTermModal"
                   classes="d-flex justify-content-center align-items-center-safe overflow-auto">
    <div class="game-card">
      <div class="game-card-content content-max-width">
        <h3 class="card-title text-center">二十四节气表</h3>
        <div v-if="this.solarTermData.length > 0">
          <div class="d-flex justify-content-between" v-for="i in Array(4).keys()" :key="i">
            <styled-button class="m-1" @click="this.currentSolarTerm = i * 6 + j" v-for="j in Array(6).keys()" :key="i + 6 * j" :pressed="currentSolarTerm === i * 6 + j" :text="this.solarTermData[i * 6 + j].name"></styled-button>
          </div>
          <hr>
          <p class="text-wrap">
            {{this.solarTermData[this.currentSolarTerm].info}}
          </p>
        </div>
        <div v-else>
          <p class="card-text placeholder-glow">
            <span :class="`placeholder m-1 col-${Math.floor(Math.random() * 4) + 3}`" v-for="i in Array(18).keys()" :key="i"></span>
          </p>
        </div>
        <hr>
        <div class="d-flex justify-content-center">
          <cancel-button class="m-1" @click="this.hide()"></cancel-button>
        </div>
      </div>
    </div>
  </vue-final-modal>
</template>

<script>
import {$vfm, VueFinalModal} from 'vue-final-modal'
import CancelButton from "@/components/buttons/cancel-button";
import StyledButton from "@/components/buttons/styled-button";
import axios from "axios";

export default {
  name: "check-solar-term-modal",
  data: function () {
    return {
      showModal: false,
      hideCallback: null,
      currentSolarTerm: 0,
      solarTermData: []
    }
  },
  components: {
    StyledButton,
    CancelButton,
    VueFinalModal
  },
  methods: {
    show: function (solarTerm, hideCallback) {
      this.initialize();
      this.hideCallback = hideCallback;
      $vfm.show('solarTermModal');
      this.currentSolarTerm = solarTerm;
    },
    hide: function () {
      let ret = $vfm.hide('solarTermModal');
      if (this.hideCallback !== null)
        ret.then(this.hideCallback)
    },
    initialize: function () {
      axios.get(`/api/things/get_data?type=${3}`).then(response=>{
        this.solarTermData = response.data;
      })
    }
  }
}
</script>

<style scoped>
.content-max-width {
  max-width: 36rem;
}
</style>