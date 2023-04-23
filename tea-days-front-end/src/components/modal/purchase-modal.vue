<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="purchase" classes="d-flex justify-content-center align-items-center">
    <div class="game-card" v-if="this.item">
      <div class="game-card-content">
        <h5 class="card-title text-center">购买 {{item.name}}</h5>
        <div v-if="loading" class="d-flex w-100 justify-content-center"><div class="spinner-border m-5" role="status"></div></div>
        <div v-else>
          <div class="d-flex justify-content-between align-items-center">
            <img :src="item.img" alt="Item image" class="item-img">
            <div>
              <h6 class="m-2">购买数量：{{count}}</h6>
              <h6 class="m-2">预计需要：{{count * item.price}}G</h6>
            </div>
          </div>
          <div class="d-flex justify-content-center">
            <cancel-button @click="this.hide()" class="m-1"></cancel-button>
            <left-button @click="this.count > 0 ? this.count -= 1 : null" class="m-1"></left-button>
            <right-button @click="this.count += 1" class="m-1"></right-button>
            <ok-button @click="this.confirm()" class="m-1"></ok-button>
          </div>
        </div>
      </div>
    </div>
  </vue-final-modal>
  <message-box-modal ref="message_box_purchase"></message-box-modal>
</template>

<script>
import {VueFinalModal} from "vue-final-modal";
import MessageBoxModal from "@/components/modal/message-box-modal";
import OkButton from "@/components/buttons/ok-button";
import CancelButton from "@/components/buttons/cancel-button";
import LeftButton from "@/components/buttons/left-button";
import RightButton from "@/components/buttons/right-button";
import axios from "axios";

export default {
  name: "purchase-modal",
  components: {RightButton, LeftButton, MessageBoxModal, OkButton, CancelButton, VueFinalModal},
  data: function () {
    return {
      showModal: false,
      item: null,
      count: 0,
      loading: false,
      callback: null,
    }
  },
  methods: {
    show: function (item, callback) {
      this.showModal = true;
      this.item = item;
      this.count = 0;
      this.loading = false;
      this.callback = callback;
    },
    hide: function () {
      this.showModal = false;
    },
    confirm: function () {
      this.loading = true;
      axios.post(`/api/store/purchase?itemId=${this.item.id}&count=${this.count}`).then(() => {
        this.hide();
        this.loading = false;
        this.$refs.message_box_purchase.show('成功', `您已获得 ${this.item.name} x${this.count}`);
        if (this.callback !== null)
          this.callback();
        this.$emit('reloadPlayerData');
      }).catch(err => {
        if (err.response.status === 401)
          this.$refs.message_box_purchase.show('错误', '未授权（登录失效）');
        else if (err.response.status === 406)
          this.$refs.message_box_purchase.show('错误', '金币数量不足');
        else if (err.response.status === 501)
          this.$refs.message_box_purchase.show('错误', '无效的物品ID');
        else
          this.$refs.message_box_purchase.show('错误', `未知错误（${err.response.status}）`);
        this.loading = false;
      })
    }
  }
}
</script>

<style scoped>
.item-img {
  width: 80px;
  height: 80px;
  image-rendering: pixelated;
}
</style>