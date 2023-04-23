<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="ManageAddModal"
    classes="d-flex justify-content-center align-items-center">
    <div class="game-card">
      <div class="game-card-content">
        <h4 class="text-center">添加</h4>
        <label for="nameInput" class="form-label"><small>名称:</small></label>
        <div class="input-group mb-3">
          <!-- <span class="input-group-text" id="basic-addon1">@</span> -->
          <input type="text" class="form-control" id="nameInput" v-model="this.name">
        </div>
        <label for="nicknameInput" class="form-label"><small>介绍：</small></label>
        <div class="input-group mb-3">
          <!-- <span class="input-group-text" id="basic-addon1"><i class="bi bi-emoji-smile"></i></span> -->
          <textarea type="text" class="form-control" id="infoInput" v-model="this.info"
            placeholder="【名称】介绍......"></textarea>
        </div>
        <label class="form-label"><small>图鉴分组：</small></label>
        <select class="form-select" aria-label="Default select" v-model="this.role">
          <!-- <option selected value="-1">请选择...</option> -->
          <option value="1">工具</option>
          <option value="2">物品</option>
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
  name: "manage--modal",
  components: { MessageBoxModal, OkButton, CancelButton, VueFinalModal },
  data: function () {
    return {
      showModal: false,
      info: '',
      name: null,
      type: '',
      role: 1,
      callback: null,
    }
  },
  methods: {
    show: function (callback = null) {
      this.callback = callback;
      console.log('addshow!')
      this.showModal = true;
    },
    hide: function () {
      this.showModal = false;
    },
    confirm: function () {
      try {
        if (this.name == null) {
          this.$refs.message_box_modal.show('添加出错', `名称未填写`);
          return;
        } else if (this.role < 0) {
          this.$refs.message_box_modal.show('添加出错', `请选择物品组`);
        }
       
          axios.post("/api/handbook/insert", {
            name: this.name,
            info: this.info,
            type: this.role,
          }).then(res => {
            if (this.callback !== null)
              this.callback(res.data);
              console.log(this.callback);
              this.showModal = false;
              this.info = '',
              this.name = null,
              this.type = '',
              this.role = 1,
              this.callback = null
          }).catch(err => {
            //console.log(err);
            if (err.response.status === 417) {
              this.$refs.message_box_modal.show('添加出错', `表单有误`);
            } 
            else if(err.response.status === 420){
              console.log('进入')
              this.$refs.message_box_modal.show('添加出错', `名称重复`);
            }
            else {
              // this.$refs.message_box_modal.show('添加出错', `此名称已存在`)
              this.$refs.message_box_modal.show('添加出错（未知错误）', `${err.code} [${err.response.status}]`)
            }
              this.showModal = true;
              this.info = '',
              this.name = null,
              this.type = '',
              this.role = 1,
              this.callback = null
          });
      } catch (err) {
        this.$refs.message_box_modal.show('添加出错', `数字格式有误`);
      }
    }
  }
}
</script>
  
<style scoped>

</style>