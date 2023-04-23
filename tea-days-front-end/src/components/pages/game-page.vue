<template>
  <game-scene-layer ref="game_scene"></game-scene-layer>
  <weather-layer ref="weather_layer"></weather-layer>
  <game-hud-overlay ref="hud_overlay" @reloadAllPlants="loadAllPlants" @reloadOnePlant="loadOnePlant" @reloadPlayerData="loadPlayerData"></game-hud-overlay>
</template>

<script>
import axios from "axios";
import GameHudOverlay from '../layers/game-hud-overlay';
import $game from '../../data/game-data'
import GameSceneLayer from "@/components/layers/game-scene-layer";
import WeatherLayer from "@/components/layers/weather-layer";

export default {
  name: "game-page",
  components: {
    WeatherLayer,
    GameSceneLayer,
    GameHudOverlay
  },
  data: function () {
    return {
    }
  },
  methods: {
    onPaint: function (timestamp) {
      this.$refs.game_scene.onPaint(timestamp);
      this.$refs.weather_layer.onPaint(timestamp);
      this.$refs.hud_overlay.onPaint(timestamp);
      window.requestAnimationFrame(this.onPaint);
    },
    loadAllPlants: function () {
      axios.post(`/api/game/all`, {}).then(response => {
        $game.plants = response.data;
      }).catch()
    },
    loadOnePlant: function (id) {
      axios.post(`/api/game/check?soilId=${id + 1}`).then(res => {
        $game.plants[id] = res.data;
      });
    },
    validateUserSession: function () {
      axios.get('/api/user/validate_session').then(response => {
        this.$store.commit('setUserId', response.data.uid);
        this.$store.commit('setRole', response.data.role);
      }).catch(() => {
        window.location.replace('/login');
        this.$store.commit('setUserId', null);
        this.$store.commit('setRole', null);
      })
    },
    loadPlayerData: function () {
      axios.get('/api/user/userinfo')
          .then(response => {
            $game.nickname = response.data.nickname;
            $game.coin = response.data.money;
          }).catch(() => {
        $game.nickname = '茶小农';
      });
      axios.post(`/api/inventory/query_sampling`).then(res => {
        $game.selectedSampling = res.data;
      }).catch(() => {
        $game.selectedSampling = null;
      });
    },
    getCurrentSolarTerm: function() {
      axios.post('/api/calendar/date').then(response => {
            $game.solarTerm = response.data;
          }).catch(() => {
            $game.solarTerm = 0;
      });
    }
  },
  mounted() {
    this.validateUserSession();
    this.loadAllPlants();
    this.loadPlayerData();
    this.getCurrentSolarTerm();
    window.requestAnimationFrame(this.onPaint);
  }
}
</script>

<style scoped>
</style>