<template>
  <canvas id="weather-layer" width="600" height="300" class="cursor">
  </canvas>
</template>
<script>
import $game from '../../data/game-data'
import axios from "axios";

export default {
  name: "weather-layer",
  data: function () {
    return {
      images: {},
      solarTerm: 0
    }
  },
  methods: {
    initializeImages: function () {
      this.images.clouds = new Image();
      this.images.clouds.src = require('../../assets/clouds.png');
      this.images.rain = new Image();
      this.images.rain.src = require('../../assets/particle_rain.png');
      this.images.snow = new Image();
      this.images.snow.src = require('../../assets/particle_snow.png');
      this.images.leaf = new Image();
      this.images.leaf.src = require('../../assets/particle_leaf.png');
      this.images.leafPink = new Image();
      this.images.leafPink.src = require('../../assets/particle_leaf_pink.png');
      this.images.leafYellow = new Image();
      this.images.leafYellow.src = require('../../assets/particle_leaf_yellow.png');
      this.images.rainFloor = new Image();
      this.images.rainFloor.src = require('../../assets/particle_rain_on_floor.png');
    },
    onPaint: function (timestamp) {
      let seedRandom = require('seedrandom');
      const canvas = document.getElementById('weather-layer');
      const ctx = canvas.getContext('2d');
      ctx.imageSmoothingEnabled = false;
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      const canvasW = canvas.getBoundingClientRect().width;
      const canvasH = canvas.getBoundingClientRect().height;
      const ratio = canvasW / canvasH;
      const unitWidth = canvas.width / Math.sqrt(ratio) * 0.005;
      const unitHeight = canvas.height / Math.sqrt(1 / ratio) * 0.005;
      if (this.cloudy) {
        const primes = [641, 941, 1433, 809, 1163];
        for (const prime in primes) {
          let rng = seedRandom(Math.floor(timestamp / 10000 / prime));
          if (rng() < .5) {
            ctx.save();
            rng = seedRandom(prime + Math.floor(timestamp / 10000 / prime));
            const startX = (rng() * (canvas.width * 0.6));
            const startY = (rng() * (canvas.height * 0.6));
            const endX = (startX + canvas.width * 0.5);
            const endY = (startY + canvas.height * 0.5);
            const progress = ((timestamp / 10000) % prime) / prime;
            const x = startX + ((endX - startX) * progress);
            const y = startY + ((endY - startY) * progress);
            let alpha = 200;
            if (progress < 0.1) {
              alpha = Math.floor(255 - progress * 10 * (255 - alpha));
            } else if (progress > 0.9) {
              alpha = Math.floor(255 - (1 - progress) * 10 * (255 - alpha));
            }
            ctx.globalAlpha = 1.0 - alpha / 255;
            ctx.drawImage(this.images.clouds, 0, 0, this.images.clouds.width, this.images.clouds.height,
                x, y, this.images.clouds.width * unitWidth, this.images.clouds.height * unitHeight);
            ctx.restore();
          }
        }
      }
      if (this.leafy) {
        const primes = [113, 127, 131, 137, 139, 149];
        for (const prime in primes) {
          let rng = seedRandom(Math.floor(timestamp / 3000 / prime));
          for (let i = 0; i < 3; i++) {
            ctx.save();
            rng = seedRandom(`${prime}${Math.floor(timestamp / 3000 / prime)}${i}`);
            const startX = (rng() * (canvas.width * 1.5));
            const startY = (rng() * (canvas.height * 0.8)) - canvas.height * 0.2;
            const endX = (startX + canvas.width * 0.2);
            const endY = (startY + canvas.height * 0.6);
            const progress = ((timestamp / 3000) % prime) / prime;
            const x = startX + ((endX - startX) * progress);
            const y = startY + ((endY - startY) * progress);
            let alpha = 0;
            if (progress < 0.05) {
              alpha = Math.floor(255 - progress * 20 * (255 - 127));
            } else if (progress > 0.95) {
              alpha = Math.floor(255 - (1 - progress) * 20 * (255 - 127));
            }
            ctx.globalAlpha = 1.0 - alpha / 255;
            let imageIndex = Math.floor(progress * 7);
            if (imageIndex >= 6)
              imageIndex = 5;
            ctx.drawImage(this.images.leaf, imageIndex * 12, 0, 12, this.images.leaf.height,
                x, y, 12 * unitWidth, 7 * unitHeight);
            ctx.restore();
          }
        }
      }
      if (this.pink_leafy) {
        const primes = [113, 127, 131, 137, 139, 149];
        for (const prime in primes) {
          let rng = seedRandom(Math.floor(timestamp / 3000 / prime));
          for (let i = 0; i < 3; i++) {
            ctx.save();
            rng = seedRandom(`P${prime}${Math.floor(timestamp / 3000 / prime)}${i}`);
            const startX = (rng() * (canvas.width * 1.5));
            const startY = (rng() * (canvas.height * 0.8)) - canvas.height * 0.2;
            const endX = (startX + canvas.width * 0.2);
            const endY = (startY + canvas.height * 0.6);
            const progress = ((timestamp / 3000) % prime) / prime;
            const x = startX + ((endX - startX) * progress);
            const y = startY + ((endY - startY) * progress);
            let alpha = 0;
            if (progress < 0.05) {
              alpha = Math.floor(255 - progress * 20 * (255 - 127));
            } else if (progress > 0.95) {
              alpha = Math.floor(255 - (1 - progress) * 20 * (255 - 127));
            }
            ctx.globalAlpha = 1.0 - alpha / 255;
            let imageIndex = Math.floor(progress * 8);
            if (imageIndex >= 6)
              imageIndex = 5;
            ctx.drawImage(this.images.leafPink, imageIndex * 12, 0, 12, this.images.leafPink.height,
                x, y, 12 * unitWidth, 7 * unitHeight);
            ctx.restore();
          }
        }
      }
      if (this.yellow_leafy) {
        const primes = [113, 127, 131, 137, 139, 149];
        for (const prime in primes) {
          let rng = seedRandom(Math.floor(timestamp / 3000 / prime));
          for (let i = 0; i < 3; i++) {
            ctx.save();
            rng = seedRandom(`Y${prime}${Math.floor(timestamp / 3000 / prime)}${i}`);
            const startX = (rng() * (canvas.width * 1.5));
            const startY = (rng() * (canvas.height * 0.8)) - canvas.height * 0.2;
            const endX = (startX + canvas.width * 0.2);
            const endY = (startY + canvas.height * 0.6);
            const progress = ((timestamp / 3000) % prime) / prime;
            const x = startX + ((endX - startX) * progress);
            const y = startY + ((endY - startY) * progress);
            let alpha = 0;
            if (progress < 0.05) {
              alpha = Math.floor(255 - progress * 20 * (255 - 127));
            } else if (progress > 0.95) {
              alpha = Math.floor(255 - (1 - progress) * 20 * (255 - 127));
            }
            ctx.globalAlpha = 1.0 - alpha / 255;
            let imageIndex = Math.floor(progress * 8);
            if (imageIndex >= 6)
              imageIndex = 5;
            ctx.drawImage(this.images.leafYellow, imageIndex * 12, 0, 12, this.images.leafYellow.height,
                x, y, 12 * unitWidth, 7 * unitHeight);
            ctx.restore();
          }
        }
      }
      if (this.snowy) {
        const primes = [61, 67, 71, 73, 79, 83, 89];
        for (const prime in primes) {
          let rng = seedRandom(Math.floor(timestamp / 5000 / prime));
          for (let i = 0; i < 20; i++) {
            ctx.save();
            rng = seedRandom(`${prime}${Math.floor(timestamp / 5000 / prime)}${i}`);
            const startX = (rng() * (canvas.width * 1.5));
            const startY = (rng() * (canvas.height * 0.8)) - canvas.height * 0.2;
            const endX = (startX + canvas.width * 0.2);
            const endY = (startY + canvas.height * 0.6);
            const progress = ((timestamp / 5000) % prime) / prime;
            const x = startX + ((endX - startX) * progress);
            const y = startY + ((endY - startY) * progress);
            let alpha = 32;
            if (progress < 0.05) {
              alpha = Math.floor(255 - progress * 20 * (255 - 127));
            } else if (progress > 0.95) {
              alpha = Math.floor(255 - (1 - progress) * 20 * (255 - 127));
            }
            ctx.globalAlpha = 1.0 - alpha / 255;
            let imageIndex = Math.floor(progress * 8);
            if (imageIndex >= 7)
              imageIndex = 6;
            ctx.drawImage(this.images.snow, imageIndex * 8, 0, 8, this.images.snow.height,
                x, y, 8 * unitWidth, 8 * unitHeight);
            ctx.restore();
          }
        }
      }
      if (this.rainy) {
        const primes = [61, 67, 71, 73, 79, 83, 89];
        for (const prime in primes) {
          let rng = seedRandom(Math.floor(timestamp / 800 / prime));
          for (let i = 0; i < 5; i++) {
            ctx.save();
            rng = seedRandom(`D${prime}${Math.floor(timestamp / 800 / prime)}${i}`);
            const x = (rng() * (canvas.width * 1.5));
            const y = (rng() * (canvas.height * 0.7)) + canvas.height * 0.3;
            const progress = ((timestamp / 800) % prime) / prime;
            let alpha = 32;
            if (progress < 0.05) {
              alpha = Math.floor(255 - progress * 20 * (255 - 127));
            } else if (progress > 0.95) {
              alpha = Math.floor(255 - (1 - progress) * 20 * (255 - 127));
            }
            ctx.globalAlpha = 1.0 - alpha / 255;
            let imageIndex = Math.floor(progress * 4);
            if (imageIndex >= 3)
              imageIndex = 2;
            ctx.drawImage(this.images.rainFloor, imageIndex * 8, 0, 8, this.images.rainFloor.height,
                x, y, 8 * unitWidth, 8 * unitHeight);
            ctx.restore();
          }
          for (let i = 0; i < 20; i++) {
            ctx.save();
            rng = seedRandom(`R${prime}${Math.floor(timestamp / 800 / prime)}${i}`);
            const startX = (rng() * (canvas.width * 1.5));
            const startY = (rng() * (canvas.height * 0.8)) - canvas.height * 0.2;
            const endX = (startX - canvas.width * 0.2);
            const endY = (startY + canvas.height * 0.6);
            const progress = ((timestamp / 800) % prime) / prime;
            const x = startX + ((endX - startX) * progress);
            const y = startY + ((endY - startY) * progress);
            let alpha = 32;
            if (progress < 0.05) {
              alpha = Math.floor(255 - progress * 20 * (255 - 127));
            } else if (progress > 0.95) {
              alpha = Math.floor(255 - (1 - progress) * 20 * (255 - 127));
            }
            ctx.globalAlpha = 1.0 - alpha / 255;
            let imageIndex = Math.floor(progress * 4);
            if (imageIndex >= 3)
              imageIndex = 2;
            ctx.drawImage(this.images.rain, imageIndex * 8, 0, 8, this.images.rain.height,
                x, y, 8 * unitWidth, 8 * unitHeight);
            ctx.restore();
          }
        }
      }
    },
    loadSolarTerm: function () {
      axios.post('/api/calendar/date').then(response => {
        this.solarTerm = response.data;
      }).catch(() => {
        this.solarTerm = 0;
      });
    }
  },
  computed: {
    rainy() {
      return $game.weatherData[this.solarTerm].search('R') !== -1
    },
    cloudy() {
      return $game.weatherData[this.solarTerm].search('C') !== -1
    },
    snowy() {
      return $game.weatherData[this.solarTerm].search('S') !== -1
    },
    leafy() {
      return $game.weatherData[this.solarTerm].search('L') !== -1
    },
    pink_leafy() {
      return $game.weatherData[this.solarTerm].search('P') !== -1
    },
    yellow_leafy() {
      return $game.weatherData[this.solarTerm].search('Y') !== -1
    },
  },
  mounted() {
    this.initializeImages();
    this.loadSolarTerm();
  }
}
</script>

<style scoped>
#weather-layer {
  width: 100%;
  height: 100%;
  position: absolute;
  image-rendering: pixelated;
}
</style>