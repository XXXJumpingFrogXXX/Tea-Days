<template>
  <div class="admin-background">
    <div class="container">
      <div class="game-card mt-5 mb-3">
        <div class="game-card-content top-right-align-container">
          <button type="button" class="btn-close top-right-align" aria-label="Close" @click="this.returnPrev()"></button>
          <h1 class="text-center">采茶记</h1>
          <h4 class="text-center">用户管理页面</h4>
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
            <h4>用户列表</h4>
            <table class="table">
              <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">邮箱</th>
                <th scope="col">昵称</th>
                <th scope="col">金币</th>
                <th scope="col">用户组</th>
                <th scope="col">操作</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="item in this.users" :key="item.mailbox">
                <th scope="row">{{ item.user_id }}</th>
                <td>{{ item.mailbox }}</td>
                <td>{{ item.nickname }}</td>
                <td>{{ item.money }}</td>
                <td>{{ this.groupName[item.role] }}</td>
                <td> <button type="button" class="btn btn-outline-primary btn-sm me-1" @click="this.manageUser(item)">管理</button><button type="button" class="btn btn-outline-danger btn-sm" @click="this.removeUser(item)">删除</button> </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
  <manage-user-modal ref="manage_user_modal"></manage-user-modal>
  <message-box-modal ref="message_box_modal_1"></message-box-modal>
  <message-box-modal ref="message_box_modal_2"></message-box-modal>
</template>

<script>
import axios from "axios";
import ManageUserModal from "@/components/modal/manage-user-modal";
import MessageBoxModal from "@/components/modal/message-box-modal";

export default {
  name: "admin-user-page",
  components: {MessageBoxModal, ManageUserModal},
  data: function () {
    return {
      loading: true,
      users: [],
      error: false,
      status: 200,
      groupName: ['封禁用户', '普通用户', '管理员']
    }
  },
  methods: {
    initialize: function () {
      axios.get('/api/admin_user/list_all').then(res => {
        this.users = res.data;
        this.loading = false;
      }).catch(err => {
        this.error = true;
        this.loading = false;
        this.status = err.response.status;
      });
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
    manageUser: function (item) {
      this.$refs.manage_user_modal.show(item, newItem => {
        for (let i = 0; i < this.users.length; ++i) {
          if (newItem.user_id === this.users[i].user_id) {
            this.users[i] = newItem;
            break;
          }
        }
      });
    },
    removeUser: function (item) {
      this.$refs.message_box_modal_1.show('警告', `真的要删除用户 #${item.user_id} ${item.mailbox} 吗？`, () => {
        axios.post(`/api/admin_user/remove_user?userId=${item.user_id}`).then(() => {
          this.$refs.message_box_modal_1.show('提示', '删除成功！');
          this.initialize();
        }).catch(err => {
          this.$refs.message_box_modal_1.show('提示', `删除失败：${err.code} [${err.response.status}]`);
        });
      })
    },
    returnPrev: function () {
      window.location.replace('/admin');
    }
  },
  mounted() {
    this.initialize();
    this.validateSession();
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