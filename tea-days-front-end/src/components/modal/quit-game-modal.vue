<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="quitModal" classes="d-flex justify-content-center align-items-center">
    <div class="game-card">
      <div class="game-card-content">
        <h5 class="card-title text-center">真的要退出游戏吗</h5>
        <h6 class="card-subtitle mb-2 text-muted text-center">您将会退出登录</h6>
        <div class="d-flex justify-content-center">
          <cancel-button class="m-1" @click="this.hide()"></cancel-button>
          <ok-button class="m-1" @click="this.confirm()"></ok-button>
        </div>
      </div>
    </div>
    <message-box-modal ref="message_box_modal"></message-box-modal>
  </vue-final-modal>
</template>

<script>
import { $vfm, VueFinalModal } from 'vue-final-modal'
import OkButton from "@/components/buttons/ok-button";
import CancelButton from "@/components/buttons/cancel-button";
import axios from "axios";
import MessageBoxModal from "@/components/modal/message-box-modal";

export default {
  name: "quit-game-modal",
  data: function () {
    return {
      showModal: false,
      hideCallback: null
    }
  },
  components: {
    MessageBoxModal,
    CancelButton,
    OkButton,
    VueFinalModal
  },
  methods: {
    show: function (hideCallback) {
      this.hideCallback = hideCallback
      $vfm.show('quitModal')
    },
    hide: function () {
      let ret = $vfm.hide('quitModal');
      if (this.hideCallback !== null)
          ret.then(this.hideCallback)
    },
    confirm: function () {
      axios.post('/api/user/quit').then(() => {
          this.$store.commit('setUserId', null);
          window.location.replace('/');
      }).catch(err => {
        this.$refs.message_box_modal.show('退出登录失败', `网络错误 [${err.code}]`)
      })
    }
  }
}
</script>

<style scoped>

</style>