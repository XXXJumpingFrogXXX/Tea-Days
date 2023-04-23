<template>
  <div class="admin-background">
    <div class="container">
      <div class="game-card mt-5 mb-3">
        <div class="game-card-content top-right-align-container">
          <button type="button" class="btn-close top-right-align" aria-label="Close" @click="this.returnGame()"></button>
          <h1 class="text-center">采茶记</h1>
          <h4 class="text-center">管理菜单</h4>
        </div>
      </div>
      <div class="game-card mb-3">
        <div class="game-card-content">
          <div v-if="this.status === 401" class="text-center m-5">
            <h3>您没有权限访问该页面。</h3>
          </div>
          <div v-else>
            <h4>请选择管理项目：</h4>
            <div class="d-flex flex-row justify-content-between flex-wrap">
              <admin-portal-button class="m-2" text="管理用户" description="包括管理用户金币、昵称、权限。" icon="bi bi-people-fill" href="/admin/user"></admin-portal-button>
              <admin-portal-button class="m-2" text="管理手册" description="管理茶叶类别介绍、茶叶种类介绍、工具、物品、节气，增加新的工具、物品。" icon="bi bi-book-fill" href="/admin/handbook"></admin-portal-button>
              <admin-portal-button class="m-2" text="管理教程" description="管理游戏教程文案。" icon="bi bi-map-fill" href="/admin/tutorial"></admin-portal-button>
              <admin-portal-button class="m-2" text="管理公告" description="管理游戏文章（公告）。" icon="bi bi-megaphone-fill" href="/admin/bulletin"></admin-portal-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import AdminPortalButton from "@/components/buttons/admin-portal-button.vue";

export default {
  name: "admin-page",
  components: {AdminPortalButton},
  data: function () {
    return {
      status: 200,
      error: false,
    }
  },
  methods: {
    validateSession: function () {
      axios.get('/api/user/validate_session').then(response => {
        this.$store.commit('setUserId', response.data.uid);
        this.$store.commit('setRole', response.data.role);
        if (response.data.role !== 2) {
          this.status = 401;
        }
      }).catch(() => {
        window.location.replace('/login');
        this.$store.commit('setUserId', null);
        this.$store.commit('setRole', null);
      })
    },
    returnGame: function () {
      window.location.replace('/game');
    }
  },
  mounted() {
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