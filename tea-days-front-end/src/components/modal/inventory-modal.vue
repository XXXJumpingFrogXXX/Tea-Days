<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="bagModal"
                   classes="d-flex justify-content-center align-items-center-safe overflow-auto">
    <div class="game-card">
      <div class="game-card-content">
        <h4 class="text-center">背包</h4>
        <h5 class="text-center text-muted m-1" v-if="this.err">网络通信失败，请刷新页面</h5>
        <div v-else>
          <h6 v-if="this.toolsItems.length === 0 && this.samplingItems.length === 0 && this.productItems.length === 0"
              class="text-center text-muted m-1">背包中还没有物品</h6>
          <div v-else class="row">
            <div v-if="this.samplingItems.length > 0" class="m-3 col">
              <h5>幼苗<span class="text-muted">（点击可选择）</span></h5>
              <div v-for="item in this.samplingItems" :key="JSON.stringify(item)">
                <hr class="my-hr">
                <div class="d-flex" @click="selectSampling(item[0].id)">
                  <img :src="item[0].img" class="item-img">
                  <div class="d-flex align-items-center justify-content-between w-100 ms-3">
                    <h5>{{ item[0].name }} <span v-if="this.selectedSampling === item[0].id"
                                                 class="text-success">[已选择]</span></h5>
                    <h5>x{{ item[1] }}</h5>
                  </div>
                </div>
              </div>
            </div>
            <div v-if="this.productItems.length > 0" class="m-3 col">
              <h5>茶叶<span class="text-muted">（点击可卖出）</span></h5>
              <div v-for="item in this.productItems" :key="JSON.stringify(item)">
                <hr class="my-hr">
                <div class="d-flex" @click="sellItem(item[0], item[1])">
                  <img :src="item[0].img" class="item-img">
                  <div class="d-flex align-items-center justify-content-between w-100 ms-3">
                    <h5>{{ item[0].name }}</h5>
                    <h5>x{{ item[1] }}</h5>
                  </div>
                </div>
              </div>
            </div>
            <div v-if="this.toolsItems.length > 0" class="m-3 col">
              <h5>道具<span class="text-muted"></span></h5>
              <div v-for="item in this.toolsItems" :key="JSON.stringify(item)">
                <hr class="my-hr">
                <div class="d-flex">
                  <img :src="item[0].img" class="item-img">
                  <div class="d-flex align-items-center justify-content-between w-100 ms-3">
                    <h5>{{ item[0].name }}</h5>
                    <h5>x{{ item[1] }}</h5>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="d-flex justify-content-center m-1">
          <ok-button @click="this.hide()"></ok-button>
        </div>
      </div>
    </div>
    <message-box-modal ref="message_box_modal"></message-box-modal>
    <sell-modal ref="sell_modal" @reloadPlayerData="this.$emit('reloadPlayerData')"></sell-modal>
  </vue-final-modal>

</template>

<script>
import OkButton from "@/components/buttons/ok-button";
import {$vfm, VueFinalModal} from 'vue-final-modal'
import MessageBoxModal from "@/components/modal/message-box-modal";
import axios from "axios";
import $item from '../../data/item-data'
import SellModal from "@/components/modal/sell-modal";

export default {
  name: "inventory-modal",
  data: function () {
    return {
      showModal: false,
      hideCallback: null,
      err: false,
      samplingItems: [],
      productItems: [],
      toolsItems: [],
      selectedSampling: 0
    }
  },
  components: {
    SellModal,
    VueFinalModal,
    MessageBoxModal,
    OkButton,
  },
  methods: {
    show: function (hideCallback) {
      this.hideCallback = hideCallback;
      this.updateInventory();
      this.querySelectedSampling();
      $vfm.show('bagModal')
    },
    hide: function () {
      let ret = $vfm.hide('bagModal');
      if (this.hideCallback !== null)
        ret.then(this.hideCallback)
    },
    updateInventory: function () {
      this.samplingItems = [];
      this.productItems = [];
      this.toolsItems = [];
      axios.post('/api/inventory/list').then(res => {
        const items = res.data;
        for (const item of items) {
          const info = $item.getItemInfo(item.item_id);
          if (info.category === 0) {
            this.samplingItems.push([info, item.count]);
          } else if (info.category === 1) {
            this.productItems.push([info, item.count]);
          } else if (info.category === 2) {
            this.toolsItems.push([info, item.count]);
          }
        }
      }).catch(() => {
        this.err = true;
      })
    },
    selectSampling: function (id) {
      axios.post(`/api/inventory/select_sampling?sampling=${id}`).then(() => {
        this.selectedSampling = id;
        this.$emit('reloadPlayerData');
      }).catch(err => {
        if (err.response.status === 401)
          this.$refs.message_box_modal.show('错误', '未授权（登录失效）');
        else if (err.response.status === 406)
          this.$refs.message_box_modal.show('错误', '无效的物品ID');
        else
          this.$refs.message_box_modal.show('错误', `未知错误（${err.response.status}）`);
      })
    },
    sellItem: function (item, c) {
      this.$refs.sell_modal.show(item, c, () => {
        this.updateInventory();
        this.querySelectedSampling();
      });
    },
    querySelectedSampling: function () {
      axios.post(`/api/inventory/query_sampling`).then(res => {
        this.selectedSampling = res.data;
      }).catch(() => {
        this.err = true;
      })
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
