<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" :esc-to-close="false" name="messageBoxModal" classes="d-flex justify-content-center align-items-center">
    <div class="game-card">
      <div class="game-card-content">
        <h4 class="text-center">{{ title }}</h4>
        <p class="text-muted text-center">{{ message }}</p>
        <div class="d-flex justify-content-center">
          <cancel-button class="m-1" @click="this.cancel()" v-if="this.cancelCallback !== null || this.okCallback !== null"></cancel-button>
          <ok-button class="m-1" @click="this.ok()"></ok-button>
        </div>
      </div>
    </div>
  </vue-final-modal>
</template>

<script>
import OkButton from "@/components/buttons/ok-button";
import CancelButton from "@/components/buttons/cancel-button";
import {VueFinalModal} from "vue-final-modal";

export default {
  name: "message-box-modal",
  components: {OkButton, CancelButton, VueFinalModal},
  data: function () {
    return {
      title: '',
      message: '',
      showModal: false,
      cancelCallback: null,
      okCallback: null,
    }
  },
  methods: {
    show: function (title, msg, okCallback = null, cancelCallback = null) {
      this.title = title;
      this.message = msg;
      this.cancelCallback = cancelCallback;
      this.okCallback = okCallback;
      this.showModal = true;
    },
    cancel: function () {
      this.showModal = false;
      if (this.cancelCallback !== null)
        this.cancelCallback();
    },
    ok: function () {
      this.showModal = false;
      if (this.okCallback !== null)
        this.okCallback();
    }
  }
}
</script>

<style scoped>

</style>