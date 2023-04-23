<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="infoModal"
                   classes="d-flex justify-content-center align-items-center-safe overflow-auto">
    <div class="game-card">
      <div class="game-card-content content-max-width">
        <h4 class="card-title text-center">选择您想查看的图鉴</h4>
        <hr>
        <div class="d-flex justify-content-center" v-for="i in Array(3).keys()" :key="i">
          <styled-button class="m-1" @click="this.showSubModal(i)" :text="this.choice[i]"></styled-button>
        </div>
        <hr>
        <div class="d-flex justify-content-center">
          <cancel-button class="m-1" @click="this.hide()"></cancel-button>
        </div>
      </div>
    </div>
  </vue-final-modal>
  <tea-modal ref="tea_modal" class="cursor"></tea-modal>
  <tool-modal ref="tool_modal" class="cursor"></tool-modal>
  <goods-modal ref="goods_modal" class="cursor"></goods-modal>

</template>
<script>
import {VueFinalModal} from 'vue-final-modal'
import CancelButton from "@/components/buttons/cancel-button";
import StyledButton from "@/components/buttons/styled-button";
import TeaModal from "@/components/modal/tea-modal";
import ToolModal from "@/components/modal/tool-modal";
import GoodsModal from "@/components/modal/goods-modal";

export default {
  name: "info-modal",
  data: function () {
    return {
      showModal: false,
      hideCallback: null,
      modalShowing: false,
      choice: ["茶叶图谱", "工具图谱", "物品图谱"]
    }
  },
  components: {
    CancelButton,
    VueFinalModal,
    StyledButton,
    TeaModal,
    ToolModal,
    GoodsModal
  },
  methods: {
    show: function (hideCallback) {
      this.hideCallback = hideCallback;
      this.showModal = true;
    },
    hide: function () {
      this.showModal = false;
      if (this.hideCallback !== null)
        this.hideCallback();
    },
    showSubModal: function (choose) {
      this.modalShowing = true;
      if (choose === 0) {
        this.$refs.tea_modal.show(() => {
          this.modalShowing = false;
        })
      }
      if (choose === 1) {
        this.modalShowing = true;
        this.$refs.tool_modal.show(() => {
          this.modalShowing = false;
        })
      }
      if (choose === 2) {
        this.modalShowing = true;
        this.$refs.goods_modal.show(() => {
          this.modalShowing = false;
        })
      }
    },
  }
}
</script>

<style scoped>

</style>