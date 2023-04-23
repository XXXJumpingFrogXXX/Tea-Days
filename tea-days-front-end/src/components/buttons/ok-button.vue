<template>
<button class="ok-button"></button>
</template>

<script>
export default {
  name: "ok-button"
}
</script>

<style scoped>
.ok-button {
  background: url('../../assets/ok_button_idle.png') no-repeat;
  background-size: cover;
  border: none;
  width: 60px;
  height: 66px;
}
.ok-button:active {
  background: url('../../assets/ok_button_pressed.png') no-repeat;
  background-size: cover;
  border: none;
  width: 60px;
  height: 66px;
}
</style>