<template>
  <div class="article-background">
    <div class="container">
      <div class="game-card mt-5 mb-3">
        <div class="game-card-content top-right-align-container">
          <button type="button" class="btn-close top-right-align" aria-label="Close" @click="this.returnGame()"></button>
          <h1 class="text-center">{{ this.title }}</h1>
        </div>
      </div>
      <div class="game-card mb-3">
        <div class="game-card-content">
          <p class="card-text placeholder-glow" v-if="this.loading">
            <span :class="`placeholder m-1 col-${Math.floor(Math.random() * 8) + 3}`" v-for="i in Array(42).keys()" :key="i"></span>
          </p>
          <div v-else-if="this.error" class="text-center m-5">
            <h3>未知错误（{{this.status}}）</h3>
          </div>
          <vue-markdown :source="this.content"></vue-markdown>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { useRoute } from 'vue-router'
import VueMarkdown from 'vue-markdown-render'

export default {
  name: "article-page",
  data: function () {
    return {
      loading: true,
      error: false,
      status: 200,
      title: '加载中...',
      content: '',
    }
  },
  components: {
    VueMarkdown
  },
  methods: {
    returnGame: function () {
      window.location.replace('/game');
    },
    loadArticleContent: function () {
      this.loading = true;
      this.error = false;
      axios.get(`/api/bulletin/get?id=${useRoute().params.articleId}`).then(res => {
        this.title = res.data.title;
        this.content = res.data.content;
        this.status = 200;
        this.loading = false;
      }).catch(err => {
        this.title = '未知错误';
        this.status = err.response.status;
        this.error = true;
        this.loading = false;
      })
    }
  },
  mounted() {
    this.loadArticleContent();
  }
}
</script>

<style scoped>
.article-background {
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