<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="shopModal" classes="d-flex justify-content-center align-items-center-safe overflow-auto">
    <div class="game-card">
      <div class="game-card-content">
        <h5 class="card-title text-center">选购商品</h5>
        <h6 class="text-center text-muted">点击以购买</h6>
        <hr class="my-hr">
        <div v-for="item in this.items" :key="JSON.stringify(item)">
          <div class="d-flex" @click="purchaseSampling(item)" v-if="item.category === 0 || item.category === 2">
            <img :src="item.img" class="item-img" alt="Item image">
            <div class="d-flex align-items-center justify-content-between w-100 ms-3">
              <h5>{{ item.name }}</h5>
              <h5>{{ item.price }}G</h5>
            </div>
          </div>
          <hr class="my-hr" v-if="item.category === 0">
        </div>
        <div class="d-flex justify-content-center">
          <ok-button class="m-1" @click="this.hide()"></ok-button>
        </div>
      </div>
    </div>
    <message-box-modal ref="message_box_modal"></message-box-modal>
    <purchase-modal ref="purchase_modal" @reloadPlayerData="this.$emit('reloadPlayerData')"></purchase-modal>
  </vue-final-modal>
</template>

<script>
import {$vfm, VueFinalModal} from 'vue-final-modal'
import MessageBoxModal from "@/components/modal/message-box-modal";
import OkButton from "@/components/buttons/ok-button";
import $item from '../../data/item-data';
import PurchaseModal from "@/components/modal/purchase-modal";

export default {
  name: "shop-modal",
  data: function () {
    return {
      showModal: false,
      hideCallback: null,
      items: [],
    }
  },
  components: {
    PurchaseModal,
    VueFinalModal,
    MessageBoxModal,
    OkButton,
  },
  methods: {
        show: function (hideCallback) {
          this.hideCallback = hideCallback;
          $vfm.show('shopModal');
          this.items = $item.items;
        },
        hide: function () {
          let ret = $vfm.hide('shopModal');
          if (this.hideCallback !== null)
            ret.then(this.hideCallback);
    },
    purchaseSampling: function (item) {
      this.$refs.purchase_modal.show(item, null)
    }
  }
}
</script>

<style scoped>
.my-hr {
  min-width: 300px;
  width: 100%;
}
.item-img {
  width: 30px;
  height: 30px;
  image-rendering: pixelated;
}
</style>