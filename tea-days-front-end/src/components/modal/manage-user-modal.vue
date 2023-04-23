<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="manageUserModal" classes="d-flex justify-content-center align-items-center">
    <div class="game-card">
      <div class="game-card-content"  v-if="this.userInfo">
        <h4 class="text-center">管理用户 #{{this.userInfo.user_id}}</h4>
        <label for="emailInput" class="form-label"><small>用户名（邮箱）：</small></label>
        <div class="input-group mb-3">
          <span class="input-group-text" id="basic-addon1">@</span>
          <input type="email" class="form-control" id="emailInput" placeholder="name@example.com"
                 v-model="this.mailbox" disabled readonly>
        </div>
        <label for="nicknameInput" class="form-label"><small>昵称：</small></label>
        <div class="input-group mb-3">
          <span class="input-group-text" id="basic-addon1"><i class="bi bi-emoji-smile"></i></span>
          <input type="text" class="form-control" id="nicknameInput"
                 v-model="this.nickname">
        </div>
        <label for="coinInput" class="form-label"><small>金币：</small></label>
        <div class="input-group mb-3">
          <span class="input-group-text" id="basic-addon1"><i class="bi bi-cash-coin"></i></span>
          <input type="number" class="form-control" id="coinInput"
                 v-model="this.coin">
        </div>
        <label class="form-label"><small>用户组：</small></label>
        <select class="form-select" aria-label="Default select" v-model="this.role">
          <option selected value="-1">请选择...</option>
          <option value="0">封禁用户</option>
          <option value="1">普通用户</option>
          <option value="2">管理员</option>
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
import { VueFinalModal } from 'vue-final-modal'
import CancelButton from "@/components/buttons/cancel-button";
import OkButton from "@/components/buttons/ok-button";
import MessageBoxModal from "@/components/modal/message-box-modal";
import axios from "axios";
export default {
  name: "manage-user-modal",
  components: {MessageBoxModal, OkButton, CancelButton, VueFinalModal},
  data: function () {
    return {
      showModal: false,
      userInfo: null,
      mailbox: '',
      nickname: '',
      coin: 0,
      role: "1",
      callback: null,
    }
  },
  methods: {
    show: function (userInfo, callback = null) {
      this.userInfo = userInfo;
      this.mailbox = userInfo.mailbox;
      this.nickname = userInfo.nickname;
      this.role = `${userInfo.role}`;
      this.coin = userInfo.money;
      this.callback = callback;
      this.showModal = true;
    },
    hide: function () {
      this.showModal = false;
    },
    confirm: function () {
      try {
        const coin = parseInt(this.coin, 10);
        const role = parseInt(this.role, 10);
        if (isNaN(coin) || coin == null) {
          this.$refs.message_box_modal.show('更新出错', `数字格式有误`);
          return;
        } else if (coin < 0) {
          this.$refs.message_box_modal.show('更新出错', `金币数量不得低于0`);
          return;
        } else if (role < 0) {
          this.$refs.message_box_modal.show('更新出错', `请选择用户组`);
          return;
        }
        axios.post("/api/admin_user/update", {
          user_id: this.userInfo.user_id,
          nickname: this.nickname,
          role: role,
          money: coin,
        }).then(res => {
          if (this.callback !== null)
            this.callback(res.data);
          this.showModal = false;
        }).catch(err => {
          console.log(err);
          if (err.response.status === 417) {
            this.$refs.message_box_modal.show('更新出错', `表单有误`);
          } else {
            this.$refs.message_box_modal.show('更新出错（未知错误）', `${err.code} [${err.response.status}]`);
          }
        });
      } catch (err) {
        this.$refs.message_box_modal.show('更新出错', `数字格式有误`);
      }
    }
  }
}
</script>

<style scoped>

</style>