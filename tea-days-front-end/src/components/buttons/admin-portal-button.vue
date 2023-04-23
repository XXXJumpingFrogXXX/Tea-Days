<template>
  <div :class="this.pressed ? 'portal-button-active' : 'portal-button'" @click="redirect()">
    <div class="text-start">
      <h5><i :class="icon + ' m-1'"></i>{{this.text}}</h5>
      <hr>
      <span class="text-muted">{{this.description}}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "admin-portal-button",
  props: {
    pressed: Boolean,
    text: String,
    href: String,
    description: String,
    icon: String,
  },
  methods: {
    redirect: function () {
      if (this.href)
        window.location.replace(this.href);
    }
  }
}
</script>

<style scoped>
.portal-button {
  border-image: url('../../assets/styled_button_idle.png');
  border-width: 18px;
  border-style: solid;
  border-image-repeat: repeat;
  border-image-slice: 6;
  image-rendering: pixelated;
  background: #e8cfa6 no-repeat;
  width: 20rem;
  height: 12rem;
  cursor: pointer;
}
.portal-button-active {
  border-image: url('../../assets/styled_button_pressed.png');
  border-width: 18px;
  border-style: solid;
  border-image-repeat: repeat;
  border-image-slice: 6;
  image-rendering: pixelated;
  background: #dcb98a no-repeat;
  width: 20rem;
  height: 12rem;
  cursor: pointer;
}
.portal-button:active {
  border-image: url('../../assets/styled_button_pressed.png');
  border-width: 18px;
  border-style: solid;
  border-image-repeat: repeat;
  border-image-slice: 6;
  image-rendering: pixelated;
  background: #dcb98a no-repeat;
  width: 20rem;
  height: 12rem;
  cursor: pointer;
}
</style>