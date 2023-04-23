<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="toolModal"
                   classes="d-flex justify-content-center align-items-center-safe overflow-auto">
    <div class="game-card">
      <div class="game-card-content content-max-width">
        <h3 class="card-title text-center">工具图谱</h3>
        <hr>
        <p class="text-wrap">

          {{this.toolData[this.tool]}}
        </p>

        <hr>
        <div class="d-flex justify-content-center">
          <left-button @click="tool > 0 ? tool -= 1 : null" class="m-1"></left-button>
          <cancel-button class="m-1" @click="this.hide()"></cancel-button>
          <right-button @click="tool + 1 < toolData.length ? tool += 1 : null" class="m-1"></right-button>

        </div>
      </div>
    </div>
  </vue-final-modal>
</template>

<script>
import {$vfm, VueFinalModal} from 'vue-final-modal'
import CancelButton from "@/components/buttons/cancel-button";
import RightButton from "@/components/buttons/right-button";
import LeftButton from "@/components/buttons/left-button";
import axios from "axios";

export default {
  name: "tool-modal",
  data: function () {
    return {
      showModal: false,
      hideCallback: null,
      toolData: [],
      tool: 0,
    }
  },
  components: {
    CancelButton,
    VueFinalModal,
    LeftButton,
    RightButton
  },
  methods: {
    show: function (hideCallback) {
      this.hideCallback = hideCallback;
      $vfm.show('toolModal');
    },
    hide: function () {
      let ret = $vfm.hide('toolModal');
      if (this.hideCallback !== null)
        ret.then(this.hideCallback)
    },
    getdata:function (){
      axios.get(`/api/things/get?type=${1}`).then(response=>{
        this.toolData=response.data;
      })
    }
  },
  mounted() {
    this.getdata();
  }
}
</script>

<style scoped>
.content-max-width {
  max-width: 36rem;
}
</style>