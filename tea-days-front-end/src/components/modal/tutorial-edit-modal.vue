<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="tutorialEditModal" classes="d-flex justify-content-center align-items-center">
    <div class="game-card">
      <div class="game-card-content edit-modal-width">
        <h4 class="text-center">编辑文案</h4>
        <div>
          <textarea class="form-control m-2" id="exampleFormControlTextarea" rows="5" v-model="content"></textarea>
          <div class="d-flex justify-content-center">
            <ok-button class="m-1" @click="this.hide()"></ok-button>
          </div>
        </div>
      </div>
    </div>
  </vue-final-modal>
  <message-box-modal ref="message_box_modal"></message-box-modal>
</template>

<script>
import OkButton from "@/components/buttons/ok-button";
import { VueFinalModal } from 'vue-final-modal'
import MessageBoxModal from "@/components/modal/message-box-modal";

export default {
  name: "tutorial-edit-modal",
  components: {MessageBoxModal, OkButton, VueFinalModal},
  data: function () {
    return {
      showModal: false,
      content: '',
      callback: null
    }
  },
  methods: {
    show: function (content, callback = null) {
      this.showModal = true;
      this.content = content;
      this.callback = callback;
    },
    hide: function () {
      this.showModal = false;
      this.callback(this.content);
    }
  }
}
</script>

<style scoped>
.edit-modal-width{
  width: 32rem;
}
</style>