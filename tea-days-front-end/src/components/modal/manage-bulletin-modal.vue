<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="manageBulletinModal" classes="d-flex justify-content-center align-items-center">
    <div class="game-card manage-modal-width">
      <div class="game-card-content" v-if="this.bulletinInfo">
        <h4 class="text-center">管理公告 #{{this.bulletinInfo.bulletin_id}}</h4>
        <label for="titleInput" class="form-label"><small>标题：</small></label>
        <input type="text" class="form-control" id="titleInput" placeholder="标题" v-model="this.title">
        <label for="urlInput" class="form-label"><small>图片路径：</small></label>
        <input type="text" class="form-control" id="urlInput" v-model="this.img_url">
        <label for="contentInput" class="form-label"><small>内容：</small></label>
        <textarea class="form-control" id="contentInputTextArea" rows="10" v-model="this.content" placeholder="在此输入内容（Markdown）"></textarea>
        <div class="d-flex justify-content-center mt-3">
          <cancel-button class="m-1" @click="hide()"></cancel-button>
          <ok-button class="m-1" @click="confirm()"></ok-button>
        </div>
      </div>
    </div>
  </vue-final-modal>
  <message-box-modal ref="message_box_modal_in_modal"></message-box-modal>
</template>

<script>
import { VueFinalModal } from 'vue-final-modal'
import CancelButton from "@/components/buttons/cancel-button";
import OkButton from "@/components/buttons/ok-button";
import MessageBoxModal from "@/components/modal/message-box-modal";
import axios from "axios";
export default {
  name: "manage-bulletin-modal",
  components: {MessageBoxModal, OkButton, CancelButton, VueFinalModal},
  data: function () {
    return {
      showModal: false,
      bulletinInfo: null,
      callback: null,
      title: '',
      content: '',
      img_url: '',
    }
  },
  methods: {
    show: function (bulletinInfo, callback = null) {
      this.bulletinInfo = bulletinInfo;
      this.title = bulletinInfo.title;
      this.img_url = bulletinInfo.img_url;
      this.callback = callback;
      this.showModal = true;
      axios.get(`/api/bulletin/get?id=${this.bulletinInfo.bulletin_id}`).then(res => {
        this.content = res.data.content;
      }).catch(() => {
        this.content = '内容加载失败';
      });
    },
    hide: function () {
      this.showModal = false;
    },
    confirm: function () {
      try {
        if (this.title === null || this.title.length === 0) {
          this.$refs.message_box_modal_in_modal.show('更新出错', `未填写标题`);
          return;
        } else if (this.content === null || this.content.length === 0) {
          this.$refs.message_box_modal_in_modal.show('更新出错', `未填写内容`);
          return;
        } else if (this.img_url === null || this.img_url.length === 0) {
          this.$refs.message_box_modal_in_modal.show('更新出错', `未填写图片地址`);
          return;
        }
        axios.post("/api/admin_bulletin/update", {
          bulletin_id: this.bulletinInfo.bulletin_id,
          title: this.title,
          content: this.content,
          img_url: this.img_url,
        }).then(res => {
          if (this.callback !== null)
            this.callback(res.data);
          this.$refs.message_box_modal_in_modal.show('更新成功', `公告内容已经更新。`);
          this.showModal = false;
        }).catch(err => {
          if (err.response.status === 417) {
            this.$refs.message_box_modal_in_modal.show('更新出错', `表单有误`);
          } else {
            this.$refs.message_box_modal_in_modal.show('更新出错（未知错误）', `${err.code} [${err.response.status}]`);
          }
        });
      } catch (err) {
        this.$refs.message_box_modal_in_modal.show('更新出错', `格式有误`);
      }
    }
  }
}
</script>

<style scoped>
.manage-modal-width {
  width: 32rem;
}
</style>