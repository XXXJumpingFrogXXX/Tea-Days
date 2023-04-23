<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="goodsModal"
                   classes="d-flex justify-content-center align-items-center-safe overflow-auto">
    <div class="game-card">
      <div class="game-card-content content-max-width">
        <h3 class="card-title text-center">物品图谱</h3>
        <div class="d-flex justify-content-between" v-for="i in Array(4).keys()" :key="i">
        </div>
        <hr>
        <p class="text-wrap">
          {{this.goodsData[this.goods]}}
        </p>

        <hr>
        <div class="d-flex justify-content-center">
          <left-button @click="goods > 0 ? goods -= 1 : null" class="m-1"></left-button>
          <cancel-button class="m-1" @click="this.hide()"></cancel-button>
          <right-button @click="goods + 1 < goodsData.length ? goods += 1 : null" class="m-1"></right-button>

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
  name: "goods-modal",
  data: function () {
    return {
      showModal: false,
      hideCallback: null,
      goodsData: [],
      goods: 0,
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
      $vfm.show('goodsModal');
    },
    hide: function () {
      let ret = $vfm.hide('goodsModal');
      if (this.hideCallback !== null)
        ret.then(this.hideCallback)
    },
    getdata:function (){
      axios.get(`/api/things/get?type=${2}`).then(response=>{
        this.goodsData=response.data;
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