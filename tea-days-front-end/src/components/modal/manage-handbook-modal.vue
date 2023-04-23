<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="ManageHandbookModal"
                   classes="d-flex justify-content-center align-items-center">
    <div class="game-card">
      <div class="game-card-content" v-if="this.name">
        <h4 class="text-center">管理图鉴 #{{ this.name }}</h4>
        <label for="nameInput" class="form-label"><small>名称：</small></label>
        <div class="input-group mb-3">
          <input type="text" class="form-control" id="nameInput" placeholder=""
                 v-model="this.name" disabled readonly>
        </div>
        <label for="infoInput" class="form-label"><small>介绍：</small></label>
        <div class="input-group mb-3">
          <textarea type="text" class="form-control" id="infonameInput" v-model="this.info"></textarea>
        </div>
        <label class="form-label"><small>图鉴分组：</small></label>
        <select class="form-select" aria-label="Default select" v-model="this.role" disabled>
          <option value="0">茶类</option>
          <option value="1">工具</option>
          <option value="2">物品</option>
          <option value="3">节气</option>
          <option value="10">成品茶</option>
        </select>
        <div class="d-flex justify-content-center mt-3">
          <cancel-button class="m-1" @click="hide()"></cancel-button>
          <ok-button class="m-1" @click="confirm()"></ok-button>
        </div>
      </div>
    </div>
    <message-box-modal ref="message_box_modal"></message-box-modal>
  </vue-final-modal>
</template>

<script>
import {VueFinalModal} from 'vue-final-modal'
import CancelButton from "@/components/buttons/cancel-button";
import OkButton from "@/components/buttons/ok-button";
import MessageBoxModal from "@/components/modal/message-box-modal";
import axios from "axios";

export default {
  name: "manage-handbook-modal",
  components: {MessageBoxModal, OkButton, CancelButton, VueFinalModal},
  data: function () {
    return {
      showModal: false,
      info: null,
      name: '',
      role: -1,
      callback: null,
    }
  },
  methods: {
    show: function (item, callback = null) {
      this.name = item.name;
      this.type = item.type;
      this.info = item.info;
      if (this.type === 0) {
        this.role = 0;
      } else if (this.type === 1) {
        this.role = 1;
      } else if (this.type === 2) {
        this.role = 2;
      } else if (this.type === 3) {
        this.role = 3;
      } else if (this.type > 10) {
        this.role = 10;
      } else {
        this.role = -1;
      }
      this.callback = callback;
      this.showModal = true;
    },
    hide: function () {
      this.showModal = false;
    },
    confirm: function () {
      try {
        if (this.name == null) {
          this.$refs.message_box_modal.show('更新出错', `数字格式有误`);
          return;
        }
        axios.post("/api/handbook/update", {
          name: this.name,
          info: this.info,
          type: this.type,
        }).then(res => {
          if (this.callback !== null)
            this.callback(res.data);
          this.showModal = false;
        }).catch(err => {
          if (err.response.status === 417) {
            this.$refs.message_box_modal.show('更新出错', `表单有误`);
          } else {
            this.$refs.message_box_modal.show('更新出错（未知错误）', `${err.code} [${err.response.status}]`);
          }
        });
      } catch (err) {
        this.$refs.message_box_modal.show('更新出错', `错误！`);
      }
    }
  }
}
</script>

<style scoped>

</style>