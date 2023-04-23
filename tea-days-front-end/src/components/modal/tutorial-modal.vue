<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="tutorialModal"
                   classes="d-flex justify-content-center align-items-center-safe">
    <div class="tutorial-background position-relative">
      <div class="header-div d-flex flex-column justify-content-between">
        <h5 class="text-center">采茶记 游戏手册</h5>
        <h6 class="text-center">{{ page + 1 }} / {{ contents.length }} 页</h6>
        <div class="d-flex justify-content-center">
          <left-button @click="page > 0 ? page -= 1 : null" class="m-1"></left-button>
          <cancel-button @click="hide()" class="m-1"></cancel-button>
          <right-button @click="page + 1 < contents.length ? page += 1 : null" class="m-1"></right-button>
        </div>
      </div>
      <div class="content-div" v-if="contents.length > 0">
        <h6 class="text-wrap">{{ contents[page] }}</h6>
      </div>
      <div v-else class="content-div">
        <p class="placeholder-glow">
          <span :class="`placeholder m-1 col-${Math.floor(Math.random() * 4) + 3}`" v-for="i in Array(10).keys()"
                :key="i"></span>
        </p>
      </div>
    </div>
  </vue-final-modal>
</template>

<script>
import {VueFinalModal} from 'vue-final-modal'
import LeftButton from "@/components/buttons/left-button";
import CancelButton from "@/components/buttons/cancel-button";
import RightButton from "@/components/buttons/right-button";
import axios from "axios";

export default {
  name: "tutorial-modal",
  data: function () {
    return {
      showModal: false,
      hideCallback: null,
      page: 0,
      contents: []
    }
  },
  components: {
    RightButton,
    CancelButton,
    LeftButton,
    VueFinalModal,
  },
  methods: {
    show: function (hideCallback) {
      this.initialize();
      this.hideCallback = hideCallback;
      this.showModal = true;
    },
    hide: function () {
      this.showModal = false;
      if (this.hideCallback !== null)
        this.hideCallback();
    },
    initialize: function () {
      axios.get(`/api/things/get_data?type=${4}`).then(response => {
        this.contents = JSON.parse(response.data[0].info);
      })
    }
  }
}
</script>

<style scoped>
.tutorial-background {
  background: url('../../assets/tutorial.png') no-repeat;
  background-size: cover;
  border: none;
  width: 612px;
  height: 454px;
  image-rendering: pixelated;
}

.header-div {
  position: absolute;
  left: 202px;
  top: 25px;
  width: 378px;
  height: 158px;
}

.content-div {
  position: absolute;
  left: 54px;
  top: 234px;
  width: 500px;
  height: 155px;
}
</style>