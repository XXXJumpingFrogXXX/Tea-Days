<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="loginModal" classes="d-flex justify-content-center align-items-center">
    <div class="game-card">
      <div class="game-card-content">
        <h4 class="text-center">登录</h4>
        <div v-if="loading" class="d-flex w-100 justify-content-center"><div class="spinner-border m-5" role="status"></div></div>
        <div v-else>
          <label for="emailInput" class="form-label"><small>用户名：</small></label>
          <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addon1">@</span>
            <input type="email" class="form-control" id="emailInput" placeholder="name@example.com"
                   v-model="username">
          </div>
          <label for="passwordInput" class="form-label"><small>密码：</small></label>
          <div class="input-group">
            <span class="input-group-text" id="basic-addon1"><i class="bi bi-key"></i></span>
            <input type="password" class="form-control" id="passwordInput" v-model="password">
          </div>
          <div class="d-flex justify-content-center mt-3">
            <cancel-button class="m-1" @click="hide()"></cancel-button>
            <ok-button class="m-1" @click="login()"></ok-button>
          </div>
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
import axios from "axios";
import MessageBoxModal from "@/components/modal/message-box-modal";
export default {
  name: "login-modal",
  components: {MessageBoxModal, OkButton, CancelButton, VueFinalModal},
  data: function () {
    return {
      showModal: false,
      username: '',
      password: '',
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
    login: function () {
      this.loading = true;
      axios.post(`/api/user/login`, {
        'mailbox': this.username,
        'password': this.password
      }).then(response => {
        this.loading = false;
        this.hide();
        if (response.status === 200) {
          this.$store.commit('setUserId', response.data.uid);
          this.$store.commit('setRole', response.data.role);
          window.location.replace('/game');
        }
      }).catch(err => {
        this.loading = false;
        this.$refs.message_box_modal.show(`登录失败 [${err.code}]`, '用户名或密码错误')
      })
    }
  }
}
</script>

<style scoped>

</style>