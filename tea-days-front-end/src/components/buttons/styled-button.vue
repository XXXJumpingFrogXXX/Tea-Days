<template>
  <button :class="this.pressed ? 'styled-button-active' : 'styled-button'">{{ this.text }}</button>
</template>

<script>
export default {
  name: "styled-button",
  props: {
    pressed: Boolean,
    text: String
  }
}
</script>

<style scoped>
.styled-button {
  border-image: url('../../assets/styled_button_idle.png');
  border-width: 18px;
  border-style: solid;
  border-image-repeat: repeat;
  border-image-slice: 6;
  image-rendering: pixelated;
  background: #e8cfa6 no-repeat;
}
.styled-button-active {
  border-image: url('../../assets/styled_button_pressed.png');
  border-width: 18px;
  border-style: solid;
  border-image-repeat: repeat;
  border-image-slice: 6;
  image-rendering: pixelated;
  background: #dcb98a no-repeat;
}
.styled-button:active {
  border-image: url('../../assets/styled_button_pressed.png');
  border-width: 18px;
  border-style: solid;
  border-image-repeat: repeat;
  border-image-slice: 6;
  image-rendering: pixelated;
  background: #dcb98a no-repeat;
}
</style>