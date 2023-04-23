<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="registerModal" classes="d-flex justify-content-center align-items-center">
    <div class="game-card">
      <div class="game-card-content">
        <h4 class="text-center">注册</h4>
        <div v-if="loading" class="d-flex w-100 justify-content-center"><div class="spinner-border m-5" role="status"></div></div>
        <div v-else>
          <label for="emailInput" class="form-label"><small>用户名：</small></label>
          <div class="input-group mb-2">
            <span class="input-group-text" id="basic-addon1">@</span>
            <input type="email" class="form-control" id="emailInput" placeholder="name@example.com"
                   v-model="registerUsername">
          </div>
          <label for="nicknameInput" class="form-label"><small>昵称：</small></label>
          <div class="input-group mb-2">
            <span class="input-group-text" id="basic-addon1"><i class="bi bi-person-circle"></i></span>
            <input type="text" class="form-control" id="nicknameInput"
                   v-model="registerNickname">
          </div>
          <label for="passwordInput1" class="form-label"><small>密码：</small></label>
          <div class="input-group mb-2">
            <span class="input-group-text" id="basic-addon1"><i class="bi bi-key"></i></span>
            <input type="password" class="form-control" id="passwordInput1" v-model="registerPassword1">
          </div>
          <label for="passwordInput2" class="form-label"><small>重复密码：</small></label>
          <div class="input-group mb-2">
            <span class="input-group-text" id="basic-addon1"><i class="bi bi-key"></i></span>
            <input type="password" class="form-control" id="passwordInput2" v-model="registerPassword2">
          </div>
          <div class="d-flex justify-content-center">
            <cancel-button class="m-1" @click="this.hide()"></cancel-button>
            <ok-button class="m-1" @click="this.register()"></ok-button>
          </div>
        </div>
      </div>
    </div>
  </vue-final-modal>
  <message-box-modal ref="message_box_modal"></message-box-modal>
</template>

<script>
import CancelButton from "@/components/buttons/cancel-button";
import OkButton from "@/components/buttons/ok-button";
import axios from "axios";
import { VueFinalModal } from 'vue-final-modal'
import MessageBoxModal from "@/components/modal/message-box-modal";

export default {
  name: "register-modal",
  components: {MessageBoxModal, OkButton, CancelButton, VueFinalModal},
  data: function () {
    return {
      showModal: false,
      registerUsername: '',
      registerNickname: '',
      registerPassword1: '',
      registerPassword2: '',
      loading: false,
    }
  },
  methods: {
    show: function () {
      this.showModal = true;
      this.loading = false;
    },
    hide: function () {
      this.showModal = false;
    },
    register: function () {
      if (this.registerUsername && this.registerNickname && this.registerPassword1 && this.registerPassword2) {
        const verify = /^\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;
        if (!verify.test(this.registerUsername)) {
          this.$refs.message_box_modal.show('注册失败，请重新填写', '邮箱格式错误。');
        } else if (this.registerPassword1.length < 8) {
          this.$refs.message_box_modal.show('注册失败，请重新填写', '密码过短。');
        } else if (this.registerPassword1 !== this.registerPassword2) {
          this.$refs.message_box_modal.show('注册失败，请重新填写', '两次输入的密码不一致。');
        } else {
          this.loading = true;
          axios.post(`/api/user/register`, {
            'mailbox': this.registerUsername,
            'nickname': this.registerNickname,
            'password': this.registerPassword1
          }).then(() => {
            this.$refs.message_box_modal.show('注册成功', '');
            this.loading = false;
            this.hide();
          }).catch(() => {
            this.$refs.message_box_modal.show('注册失败，请重新填写', '用户名已存在。');
            this.loading = false;
          })
        }
      } else {
        if (!this.registerUsername || !this.registerNickname || !this.registerPassword1 || !this.registerPassword2) {
          this.$refs.message_box_modal.show('注册失败，请重新填写', '信息不能为空。');
        }
      }
    }
  }
}
</script>

<style scoped>

</style>