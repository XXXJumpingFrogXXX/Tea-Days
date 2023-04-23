<template>
  <div class="admin-background">
    <div class="container">
      <div class="game-card mt-5 mb-3">
        <div class="game-card-content top-right-align-container">
          <button type="button" class="btn-close top-right-align" aria-label="Close" @click="this.returnPrev()"></button>
          <h1 class="text-center">采茶记</h1>
          <h4 class="text-center">教程管理</h4>
        </div>
      </div>
      <div class="game-card mb-3">
        <div class="game-card-content">
          <p class="card-text placeholder-glow" v-if="this.loading">
            <span :class="`placeholder m-1 col-${Math.floor(Math.random() * 8) + 3}`" v-for="i in Array(42).keys()" :key="i"></span>
          </p>
          <div v-else-if="this.error" class="text-center m-5">
            <h3 v-if="this.status === 401">您没有权限访问该页面。</h3>
            <h3 v-else>未知错误（{{this.status}}）</h3>
          </div>
          <div v-else>
            <h4>教程文案列表：</h4>
            <h6 class="text-muted">请注意，编辑之后请点击“保存”按钮，更改才会生效。</h6>
            <table class="table">
              <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">文案</th>
                <th scope="col" style="width: 15%">操作</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in this.contents" :key="item">
                <th scope="row">{{ index + 1 }}</th>
                <td><span class="text-wrap">{{ item }}</span></td>
                <td style="width: 15%">
                  <button type="button" class="m-1 btn btn-outline-primary btn-sm me-1" @click="this.edit(index)">编辑</button>
                  <button type="button" class="m-1 btn btn-outline-danger btn-sm" @click="this.remove(index)">删除</button>
                  <button type="button" class="m-1 btn btn-outline-success btn-sm" @click="this.insert(index)">插入</button>
                </td>
              </tr>
              <tr>
                <th scope="row"></th>
                <td><span class="text-wrap text-muted"></span></td>
                <td style="width: 15%">
                  <button type="button" class="m-1 btn btn-outline-success btn-sm" @click="this.insert(this.contents.length)">插入</button>
                </td>
              </tr>
              </tbody>
            </table>
            <div class="d-flex justify-content-center">
              <button class="m-2 btn btn-lg btn-success" @click="this.save()" :disabled="saving">
                <span v-if="saving" class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                <span v-if="saving">保存中</span>
                <span v-else>保存更改</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <tutorial-edit-modal ref="tutorial_edit_modal"></tutorial-edit-modal>
  <message-box-modal ref="message_box_modal"></message-box-modal>
</template>

<script>
import axios from "axios";
import TutorialEditModal from "@/components/modal/tutorial-edit-modal.vue";
import MessageBoxModal from "@/components/modal/message-box-modal.vue";

export default {
  name: "admin-tutorial-page",
  components: {MessageBoxModal, TutorialEditModal},
  data: function () {
    return {
      status: 200,
      loading: true,
      error: false,
      contents: [],
      saving: false,
    }
  },
  methods: {
    returnPrev: function () {
      window.location.replace('/admin');
    },
    validateSession: function () {
      axios.get('/api/user/validate_session').then(response => {
        this.$store.commit('setUserId', response.data.uid);
        this.$store.commit('setRole', response.data.role);
      }).catch(() => {
        window.location.replace('/login');
        this.$store.commit('setUserId', null);
        this.$store.commit('setRole', null);
      })
    },
    initialize: function () {
      axios.get(`/api/things/get_data?type=${4}`).then(res => {
        this.contents = JSON.parse(res.data[0].info);
        this.loading = false;
      }).catch(err => {
        this.error = true;
        this.loading = false;
        this.status = err.response.status;
      });
    },
    edit: function (index) {
      this.$refs.tutorial_edit_modal.show(this.contents[index], newContent => {
        this.contents[index] = newContent;
      });
    },
    remove: function (index) {
      let temp = [];
      for (let i = 0; i < this.contents.length; ++i) {
        if (i !== index)
          temp.push(this.contents[i]);
      }
      this.contents = temp;
    },
    insert: function (index) {
      let temp = [];
      for (let i = 0; i < this.contents.length + 1; ++i) {
        if (i === index)
          temp.push('新建文案');
        if (i !== this.contents.length)
          temp.push(this.contents[i]);
      }
      this.contents = temp;
    },
    save: function () {
      this.saving = true;
      axios.post("/api/handbook/update", {
        name: '教程',
        info: JSON.stringify(this.contents),
        type: 4,
      }).then(() => {
        this.saving = false;
        this.$refs.message_box_modal.show('成功', `已经更新教程文案。`);
      }).catch(err => {
        this.saving = false;
        if (err.response.status === 417) {
          this.$refs.message_box_modal.show('更新出错', `表单有误`);
        } else {
          this.$refs.message_box_modal.show('更新出错（未知错误）', `${err.code} [${err.response.status}]`);
        }
      });
    }
  },
  mounted() {
    this.validateSession();
    this.initialize();
  }
}
</script>

<style scoped>
.admin-background {
  background: url("../../assets/brick_wall.png") repeat;
  width: 100%;
  min-height: 100%;
  position: absolute;
}
.top-right-align-container {
  position: relative;
}
.top-right-align {
  position: absolute;
  right: 20px;
  top: 20px;
}
</style>