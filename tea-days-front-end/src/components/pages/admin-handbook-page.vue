<template>
  <div class="admin-background">
    <div class="container">
      <div class="game-card mt-5 mb-3">
        <div class="game-card-content top-right-align-container">
          <button type="button" class="btn-close top-right-align" aria-label="Close"
                  @click="this.returnPrev()"></button>
          <h1 class="text-center">采茶记</h1>
          <h4 class="text-center">{{ this.pageTitle }}管理页面</h4>
          <button class="headbtn lsbtn" @click="this.setSelectedButton('1');this.initialize();">茶叶管理</button>
          <button class="headbtn lsbtn" @click="this.setSelectedButton('2');this.initialize();">工具管理</button>
          <button class="headbtn lsbtn" @click="this.setSelectedButton('3');this.initialize();">物品管理</button>
          <button class="headbtn lsbtn" @click="this.setSelectedButton('4');this.initialize();">节气管理</button>
          <button class="headbtn addbtn" @click="this.add()">+添加</button>
        </div>
      </div>
      <div class="game-card mb-3">
        <div class="game-card-content">
          <p class="card-text placeholder-glow" v-if="this.loading">
            <span :class="`placeholder m-1 col-${Math.floor(Math.random() * 8) + 3}`" v-for="i in Array(42).keys()"
                  :key="i"></span>
          </p>
          <div v-else-if="this.error" class="text-center m-5">
            <h3 v-if="this.status === 401">您没有权限访问该页面。</h3>
            <h3 v-else>未知错误（{{ this.status }}）</h3>
          </div>
          <div v-else>
            <h4>{{ this.pageTitle }}列表</h4>
            <table class="table">
              <thead>
              <tr>
                <th scope="col">名称</th>
                <th scope="col">介绍</th>
                <th scope="col">操作</th>
              </tr>
              </thead>
              <tbody>

              <tr v-for="item in this.items" :key="item.name">
                <td>{{ item.name }}</td>
                <td>{{ item.info }}</td>
                <td style="width: 10%">
                  <button type="button" class="btn btn-outline-primary btn-sm me-1 " @click="this.manageHandbook(item)">
                    管理
                  </button>
                  <button type="button" class="btn btn-outline-danger btn-sm " v-if="isShow"
                          @click="this.removeThing(item)">删除
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
  <manage-handbook-modal ref="manage_handbook_modal"></manage-handbook-modal>
  <manage-add-modal ref="manage_add_modal"></manage-add-modal>
  <message-box-modal ref="message_box_modal_1"></message-box-modal>
  <message-box-modal ref="message_box_modal_2"></message-box-modal>
</template>

<script>
import axios from "axios";
import ManageHandbookModal from "@/components/modal/manage-handbook-modal";
import ManageAddModal from "@/components/modal/manage-add-modal";
import MessageBoxModal from "@/components/modal/message-box-modal";

export default {
  name: "admin-handbook-page",
  components: {MessageBoxModal, ManageHandbookModal, ManageAddModal},
  data: function () {
    return {
      loading: true,
      items: [],
      error: false,
      status: 200,
      groupName: ['封禁用户', '普通用户', '管理员'],
      selectedButton: null,
      pageTitle: null,
      index: 0
    }
  },
  methods: {
    initialize: function () {
      if (this.selectedButton === '1' || this.selectedButton === null) {
        axios.get('/api/handbook/list_tea').then(res => {
          this.pageTitle = '茶叶';
          this.items = res.data;
          this.loading = false;
          this.isShow = false;

        }).catch(err => {
          this.error = true;
          this.loading = false;
          this.status = err.response.status;
        });
      } else if (this.selectedButton === '2') {
        axios.get('/api/handbook/list_tool').then(res => {
          this.pageTitle = '工具';
          this.items = res.data;
          this.loading = false;
          this.isShow = true;
        }).catch(err => {
          this.error = true;
          this.loading = false;
          this.status = err.response.status;
        });
      } else if (this.selectedButton === '3') {
        axios.get('/api/handbook/list_item').then(res => {
          this.pageTitle = '物品';
          this.items = res.data;
          this.loading = false;
          this.isShow = true;
        }).catch(err => {
          this.error = true;
          this.loading = false;
          this.status = err.response.status;
        });
      } else if (this.selectedButton === '4') {
        axios.get('/api/handbook/list_term').then(res => {
          this.pageTitle = '节气';
          this.items = res.data;
          this.loading = false;
          this.isShow = false;
        }).catch(err => {
          this.error = true;
          this.loading = false;
          this.status = err.response.status;
        });
      }
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
    manageHandbook: function (item) {
      this.$refs.manage_handbook_modal.show(item, newItem => {
        for (let i = 0; i < this.items.length; ++i) {
          if (newItem.name === this.items[i].name) {
            this.items[i] = newItem;
            break;
          }
        }
        this.initialize();
      });
    },
    removeThing: function (item) {
      this.$refs.message_box_modal_1.show('警告', `真的要删除该物品 #${item.name}吗？`, () => {
        if (item.type === 1) {
          axios.post(`/api/handbook/remove_tool?name=${item.name}`).then(() => {
            this.$refs.message_box_modal_1.show('提示', '删除成功！');
            this.initialize();
          }).catch(err => {
            this.$refs.message_box_modal_1.show('提示', `删除失败：${err.code} [${err.response.status}]`);
          });
        } else if (item.type === 2) {
          axios.post(`/api/handbook/remove_item?name=${item.name}`).then(() => {
            this.$refs.message_box_modal_1.show('提示', '删除成功！');
            this.initialize();
          }).catch(err => {
            this.$refs.message_box_modal_1.show('提示', `删除失败：${err.code} [${err.response.status}]`);
          });
        }
      })
    },
    add: function () {
      this.$refs.manage_add_modal.show(() => {
            this.initialize();
          }
      );
    },
    returnPrev: function () {
      window.location.replace('/admin');
    },
    setSelectedButton: function (num) {
      this.selectedButton = num;
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

.headbtn {

  font-size: larger;

  /* margin-left: 7%;
  margin-right: 7%;
  width: 19%; */
  /* margin-left: 1%; */
  /* margin-right: 7%; */
  width: 10%;
  border-style: ridge;
  border-width: 2px;
  border-radius: 3px;
  color: rgb(72, 72, 72);
  border-color: rgb(193, 166, 142);
  background-color: unset;
  padding: 2px;
}

.headbtn:hover {
  color: rgb(255, 255, 255);
  background-color: rgb(153, 137, 119);
}

.lsbtn:focus {
  color: rgb(255, 255, 255);
  background-color: rgb(125, 113, 98);
}

.addbtn:hover {
  color: rgb(255, 255, 255);
  background-color: rgb(0, 135, 22);
}

/* .addbtn:focus{
  color:rgb(0, 135, 22);
  border-color:rgb(0, 154, 26);
  background-color:unset;
} */
.addbtn {
  float: right;
  color: rgb(0, 135, 22);
  border-color: rgb(0, 154, 26);
}

.btn {
  margin: 2px;
}
</style>