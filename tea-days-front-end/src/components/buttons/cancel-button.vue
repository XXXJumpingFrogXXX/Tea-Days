<template>
  <button class="cancel-button"></button>
</template>

<script>
export default {
  name: "cancel-button"
}
</script>

<style scoped>
.cancel-button {
  background: url('../../assets/cancel_button_idle.png') no-repeat;
  background-size: cover;
  border: none;
  width: 60px;
  height: 66px;
}
.cancel-button:active {
  background: url('../../assets/cancel_button_pressed.png') no-repeat;
  background-size: cover;
  border: none;
  width: 60px;
  height: 66px;
}
</style>