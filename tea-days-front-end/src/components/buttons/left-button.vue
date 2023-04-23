<template>
  <button class="left-button"></button>
</template>

<script>
export default {
  name: "left-button"
}
</script>

<style scoped>
.left-button {
  background: url('../../assets/left_button_idle.png') no-repeat;
  background-size: cover;
  border: none;
  width: 60px;
  height: 66px;
  image-rendering: pixelated;
}
.left-button:active {
  background: url('../../assets/left_button_pressed.png') no-repeat;
  background-size: cover;
  border: none;
  width: 60px;
  height: 66px;
  image-rendering: pixelated;
}
</style>