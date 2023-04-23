<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="bulletinModal"
                   classes="d-flex justify-content-center align-items-center-safe overflow-auto">
    <div class="game-card">
      <div class="game-card-content content-max-width">
        <h3 class="card-title text-center">游戏公告 / 文章</h3>
        <hr>
        <div class="d-flex justify-content-between">
          <div class="card m-2" style="width: 18rem;" v-for="item in contents[page]" :key="item.bulletin_id">
            <img :src="item.img_url" style="height: 10rem;" class="card-img-top" :alt="item.title">
            <div class="card-body">
              <a :href="`/article/${item.bulletin_id}`" class="card-text text-decoration-none text-dark stretched-link">{{ item.title }}</a>
            </div>
          </div>
        </div>
        <hr>
        <h6 class="text-center text-muted">{{page + 1}} / {{contents.length}} 页</h6>
        <div class="d-flex justify-content-center">
          <left-button @click="page > 0 ? page -= 1 : null" class="m-1"></left-button>
          <cancel-button @click="hide()" class="m-1"></cancel-button>
          <right-button @click="page + 1 < contents.length ? page += 1 : null" class="m-1"></right-button>
        </div>
      </div>
    </div>
  </vue-final-modal>
</template>

<script>
// import StyledButton from "@/components/buttons/styled-button";
import LeftButton from "@/components/buttons/left-button";
import RightButton from "@/components/buttons/right-button";
import CancelButton from "@/components/buttons/cancel-button";
import {$vfm, VueFinalModal} from "vue-final-modal";
import axios from "axios";

export default {
  name: "bulletin-modal",
  data: function () {
    return {
      showModal: false,
      hideCallback: null,
      page: 0,
      contents: [],
      loading: false,
      err: false,
    }
  },
  components: {
    // StyledButton,
    CancelButton,
    LeftButton,
    RightButton,
    VueFinalModal
  },
  methods: {
    show: function (hideCallback) {
      this.hideCallback = hideCallback;
      $vfm.show('bulletinModal');
      this.loadBulletinData();
    },
    hide: function () {
      let ret = $vfm.hide('bulletinModal');
      if (this.hideCallback !== null)
        ret.then(this.hideCallback)
    },
    loadBulletinData: function () {
      this.loading = true;
      this.contents = [];
      axios.get('/api/bulletin/all').then(res => {
        const items = res.data;
        let temp = [];
        let contents = [];
        for (let i = 0; i <= items.length; ++i) {
          if (i % 2 === 0 && i !== 0 || i === items.length) {
            contents.push(temp);
            temp = [];
            if (i === items.length)
              break;
          }
          temp.push(items[i]);
        }
        this.contents = contents;
        this.loading = false;
        console.log(items.length);
      }).catch(() => {
        this.loading = false;
        this.err = true;
      })
    }
  },
  mounted() {
    this.loadBulletinData();
  }
}
</script>

<style scoped>

</style>