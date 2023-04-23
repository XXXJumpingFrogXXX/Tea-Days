<template>
  <button class="play-button"></button>
</template>

<script>
export default {
  name: "play-button"
}
</script>

<style scoped>
.play-button {
  background: url('../../assets/play_button_idle.png') no-repeat;
  background-size: cover;
  border: none;
  width: 360px;
  height: 108px;
  max-width: 100%;
}
.play-button:active {
  background: url('../../assets/play_button_pressed.png') no-repeat;
  background-size: cover;
  border: none;
  width: 360px;
  height: 108px;
  max-width: 100%;
}
</style>