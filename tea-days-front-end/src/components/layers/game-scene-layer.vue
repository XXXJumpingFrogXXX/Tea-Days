<template>
  <canvas id="viewport" width="600" height="300">
    <h1 class="text-muted">对不起，您的浏览器不支持 HTML5 Canvas。您无法进行该游戏。</h1>
  </canvas>
</template>

<script>
import $game from "@/data/game-data";
export default {
  name: "game-scene-layer",
  data: function () {
    return {
      images: {
        mainScene: null,
        player: null,
        bushes: [],
      }
    }
  },
  methods: {
    initializeImages: function () {
      this.images.mainScene = new Image();
      this.images.mainScene.src = require('../../assets/main_scene.png');
      this.images.player = new Image();
      this.images.player.src = require('../../assets/player.png');
      this.images.tea_bush_dead = new Image();
      this.images.tea_bush_dead.src = require('../../assets/dead_bush.png');
      this.images.bushes.push([this.images.tea_bush_dead, this.images.tea_bush_dead, this.images.tea_bush_dead]);
      for (let i = 0; i < 7; ++i) {
        let temp = [];
        for (let j = 0; j < 3; ++j) {
          let tmpImage = new Image();
          tmpImage.src = require(`../../assets/bush_${i}_${j}.png`);
          temp.push(tmpImage);
        }
        this.images.bushes.push(temp);
      }
    },
    onPaint: function (timestamp) {
      const canvas = document.getElementById('viewport');
      const ctx = canvas.getContext('2d');
      ctx.imageSmoothingEnabled = false;
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      const canvasW = canvas.getBoundingClientRect().width;
      const canvasH = canvas.getBoundingClientRect().height;
      const worldAreaToDisplay = 30000;
      const worldWidth = 400;
      const worldHeight = 400;
      const aspectRatio = canvasW / canvasH;
      const viewportWidth = Math.sqrt(worldAreaToDisplay * aspectRatio);
      const viewportHeight = Math.sqrt(worldAreaToDisplay / aspectRatio);
      let viewportX = $game.playerPositionX - viewportWidth / 2;
      let viewportY = $game.playerPositionY - viewportHeight / 2;
      if (viewportX < 0) { viewportX = 0; }
      if (viewportY < 0) { viewportY = 0; }
      if (viewportX + viewportWidth > worldWidth) { viewportX = worldWidth - viewportWidth; }
      if (viewportY + viewportHeight > worldHeight) { viewportY = worldHeight - viewportHeight; }

      // Main scene
      {
        ctx.save();
        ctx.drawImage(this.images.mainScene,
            viewportX, viewportY, viewportWidth, viewportHeight,
            0, 0, canvas.width, canvas.height);
        ctx.restore();
      }

      // Plants and Player
      {
        for (let step = 0; step < 3; step++) {
          if (step !== 1 && $game.plants !== null) {
            // Plant
            for (let i = 0; i < $game.plants.length; i++) {
              ctx.save();
              if ($game.plants[i] === null)
                continue;
              if ($game.plants[i].species !== 0 &&
                  (($game.soilPosition[i][1] > $game.playerPositionY && step === 2) ||
                      ($game.soilPosition[i][1] <= $game.playerPositionY && step === 0))) {
                const species = $game.plants[i].species === -1 ? 0 : $game.plants[i].species;
                const image = this.images.bushes[species][$game.plants[i].stage];
                const ratio = image.width / image.height;
                let size = 20 * [.5, .75, 1][$game.plants[i].stage];
                if (species === 0) size = 20;
                ctx.drawImage(image,
                    0, 0, image.width, image.height,
                    canvas.width / viewportWidth * ($game.soilPosition[i][0] - viewportX) - canvas.width / viewportWidth * ratio * size / 2,
                    canvas.height / viewportHeight * ($game.soilPosition[i][1] - viewportY) - canvas.height / viewportHeight * size / 3 * 2,
                    canvas.width / viewportWidth * ratio * size, canvas.height / viewportHeight * size);
              }
              ctx.restore();
            }
          } else if (step === 1) {
            // Player
            ctx.save();
            const imageYs = [2, 0, 3, 1];
            let frameIdentifier = Math.floor(timestamp / 100) % 4;
            if (!$game.playerIsWalking) frameIdentifier = 0;
            ctx.drawImage(this.images.player,
                24 * frameIdentifier, 40 * imageYs[$game.playerOrientation], 24, 40,
                canvas.width / viewportWidth * ($game.playerPositionX - viewportX),
                canvas.height / viewportHeight * ($game.playerPositionY - viewportY) - canvas.height / viewportHeight * 30,
                canvas.width / viewportWidth * 24, canvas.height / viewportHeight * 40);
            ctx.restore();
          }
        }
      }
    },
  },

  mounted() {
    this.initializeImages();
  }
}
</script>

<style scoped>
#viewport {
  width: 100%;
  height: 100%;
  position: absolute;
  image-rendering: pixelated;
}
</style>