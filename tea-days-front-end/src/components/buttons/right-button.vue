<template>
  <button class="right-button"></button>
</template>

<script>
export default {
  name: "right-button"
}
</script>

<style scoped>
.right-button {
  background: url('../../assets/right_button_idle.png') no-repeat;
  background-size: cover;
  border: none;
  width: 60px;
  height: 66px;
  image-rendering: pixelated;
}
.right-button:active {
  background: url('../../assets/right_button_pressed.png') no-repeat;
  background-size: cover;
  border: none;
  width: 60px;
  height: 66px;
  image-rendering: pixelated;
}
</style>