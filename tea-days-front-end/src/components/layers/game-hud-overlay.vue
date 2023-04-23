<template>
  <canvas id="hud" width="600" height="300" class="cursor">
  </canvas>
  <quit-game-modal ref="quit_game_modal" class="cursor"></quit-game-modal>
  <shop-modal ref="shop_modal" class="cursor" @reloadPlayerData="this.$emit('reloadPlayerData')"></shop-modal>
  <inventory-modal ref="inv_modal" class="cursor" @reloadPlayerData="this.$emit('reloadPlayerData')"></inventory-modal>
  <check-solar-term-modal ref="check_solar_term_modal" class="cursor"></check-solar-term-modal>
  <tutorial-modal ref="tutorial_modal" class="cursor"></tutorial-modal>
  <info-choose-modal ref="info_modal" class="cursor"></info-choose-modal>
  <bulletin-modal ref="bulletin_modal" class="cursor"></bulletin-modal>
</template>

<script>
import axios from "axios";
import QuitGameModal from "@/components/modal/quit-game-modal";
import ShopModal from "@/components/modal/shop-modal";
import InventoryModal from "@/components/modal/inventory-modal";
import CheckSolarTermModal from "@/components/modal/check-solar-term-modal";
import $game from '../../data/game-data'
import $item from '../../data/item-data'
import TutorialModal from "@/components/modal/tutorial-modal.vue";
import InfoChooseModal from "@/components/modal/info-choose-modal";
import BulletinModal from "@/components/modal/bulletin-modal";
export default {
  name: "hud-overlay",
  components: {
    BulletinModal,
    TutorialModal, QuitGameModal, ShopModal, InventoryModal, CheckSolarTermModal,InfoChooseModal},
  data: function () {
    return {
      images: {
        buttons: null,
        panel: null,
        buttonIcons: null,
        bannedIcon: null,
        selector: null,
        buttonsUpper: null,
        itemImages: [],
        calendar: null,
      },
      cursorDown: false,
      buttons: [0, 1, 2, 3, 4],
      selectedButtonLower: -1,
      selectedButtonUpper: -1,
      selectedEntityPos: null,
      selectedEntityIndex: -1,
      toolBoxDescriptions: [
        ['镰刀：\n点击地块铲除作物。', '再次点击以确认铲除。'],
        ['水壶：\n点击地块浇水。', '再次点击以确认浇水。'],
        ['手套：\n点击地块收获作物。', '再次点击以确认收获。'],
        ['问号：\n点击地块查询信息。', '再次点击以确认查询。'],
        ['幼苗：\n请先在背包里调整作物。', '再次点击以确认种植。'],
      ],
      messageBoxTimer: 120,
      messageBoxMessages: [],
      modalShowing: false,
      keyPressing: -1,
      buttonsUpperInfo: [2, 5, 6, 7, 9, 10, 4],
    }
  },
  methods: {
    initializeImages: function () {
      this.images.buttons = new Image();
      this.images.buttons.src = require('../../assets/buttons_square.png');
      this.images.panel = new Image();
      this.images.panel.src = require('../../assets/panel.png');
      this.images.buttonIcons = new Image();
      this.images.buttonIcons.src = require('../../assets/icons.png');
      this.images.selector = new Image();
      this.images.selector.src = require('../../assets/selector.png');
      this.images.messageBox = new Image();
      this.images.messageBox.src = require('../../assets/message_box.png');
      this.images.buttonsUpper = new Image();
      this.images.buttonsUpper.src = require('../../assets/buttons_upper.png');
      this.images.bannedIcon = new Image();
      this.images.bannedIcon.src = require('../../assets/banned_icon.png');
      for (let item of $item.items) {
        let newImage = new Image();
        newImage.src = item.img;
        this.images.itemImages.push(newImage);
      }
    },
    getItemImage: function (itemId) {
      for (let [ind, item] of $item.items.entries()) {
        if (item.id === itemId) {
          return this.images.itemImages[ind];
        }
      }
    },
    onPaint: function (timestamp) {
      const canvas = document.getElementById('hud');
      const ctx = canvas.getContext('2d');
      ctx.imageSmoothingEnabled = false;
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      const canvasW = canvas.getBoundingClientRect().width;
      const canvasH = canvas.getBoundingClientRect().height;
      const ratio = canvasW / canvasH;
      const unitWidth = canvas.width / Math.sqrt(ratio) * 0.005;
      const unitHeight = canvas.height / Math.sqrt(1 / ratio) * 0.005;

      // selector
      if (this.selectedEntityIndex !== -1 && this.selectedButtonLower < 5) {
        const worldAreaToDisplay = 30000;
        const aspectRatio = canvasW / canvasH;
        const viewportWidth = Math.sqrt(worldAreaToDisplay * aspectRatio);
        const viewportHeight = Math.sqrt(worldAreaToDisplay / aspectRatio);
        let viewportX = $game.playerPositionX - viewportWidth / 2;
        let viewportY = $game.playerPositionY - viewportHeight / 2;
        if (viewportX < 0) { viewportX = 0; }
        if (viewportY < 0) { viewportY = 0; }
        if (viewportX + viewportWidth > $game.worldWidth) { viewportX = $game.worldWidth - viewportWidth; }
        if (viewportY + viewportHeight > $game.worldHeight) { viewportY = $game.worldHeight - viewportHeight; }

        ctx.save();
        const selectorWidth = unitWidth * 24 * (1 + 0.2 * Math.sin((timestamp % (2 * 314)) / 200));
        const selectorHeight = unitHeight * 25 * (1 + 0.2 * Math.sin((timestamp % (2 * 314)) / 200));
        ctx.translate(canvas.width / viewportWidth * (this.selectedEntityPos[0] - viewportX) - selectorWidth / 2, canvas.height / viewportHeight * (this.selectedEntityPos[1] - viewportY) - selectorHeight / 2);
        ctx.drawImage(this.images.selector, 0, 0, 24, 25, 0, 0, selectorWidth, selectorHeight);
        ctx.restore();
      }
      // panel
      {
        const panelWidth = unitWidth * 70 * 0.8;
        const panelHeight = unitHeight * 60 * 0.8;
        ctx.save();
        ctx.drawImage(this.images.panel,
            0, 0, 70, 60,
            canvas.width - panelWidth - unitWidth, unitHeight,
            panelWidth, panelHeight);
        ctx.fillStyle = "#FAFAD2";
        ctx.fillText(String($game.coin) + ' G', canvas.width - unitWidth * 55 * 0.8, unitHeight * 54 * 0.8);
        ctx.fillStyle = "#000000";
        ctx.fillText($game.nickname + '的茶园\n', canvas.width - unitWidth * 65 * 0.8, unitHeight * 12 * 0.8);
        ctx.fillText('当前节气：' + $game.solarTermNames[$game.solarTerm], canvas.width - unitWidth * 65 * 0.8, unitHeight * 26 * 0.8);
        ctx.restore();
      }
      // buttons
      {
        const buttonsCount = this.buttons.length;
        const buttonWidth = unitWidth * 15;
        const buttonHeight = unitHeight * 17;
        const iconWidth = unitWidth * 10;
        const iconHeight = unitHeight * 10;
        const uiWidth = buttonWidth * buttonsCount + unitWidth * (buttonsCount - 1);
        ctx.save();
        for (const [index, iconId] of this.buttons.entries()) {
          const buttonCenterX = canvas.width / 2 - uiWidth / 2 + index * (buttonWidth + unitWidth) + buttonWidth / 2;
          ctx.drawImage(this.images.buttons,
              11 + (this.selectedButtonLower === iconId ? 48 : 0), 11 + (this.selectedButtonLower === iconId ? -2 : 0), 26, 28,
              buttonCenterX - buttonWidth / 2, canvas.height - buttonHeight - unitHeight,
              buttonWidth, buttonHeight);
          if (iconId < 4 || $game.selectedSampling === 0 || $game.selectedSampling === null) {
            ctx.drawImage(this.images.buttonIcons,
                10 * iconId, 0, 10, 10,
                buttonCenterX - iconWidth / 2,
                canvas.height - buttonHeight / 2 - unitHeight - iconHeight / 2 + (this.selectedButtonLower === iconId ? unitHeight : 0),
                iconWidth, iconHeight);
          } else {
            ctx.drawImage(this.getItemImage($game.selectedSampling),
                0, 0, 20, 20,
                buttonCenterX - iconWidth / 2,
                canvas.height - buttonHeight / 2 - unitHeight - iconHeight / 2 + (this.selectedButtonLower === iconId ? unitHeight : 0),
                iconWidth, iconHeight);
          }
          if (this.$store.getters.getRole === 0) {
            ctx.drawImage(this.images.bannedIcon,
                0, 0, 10, 10,
                buttonCenterX - iconWidth / 2,
                canvas.height - buttonHeight / 2 - unitHeight - iconHeight / 2,
                iconWidth, iconHeight);
          }
        }
        ctx.restore();
      }
      // buttons upper
      {
        const buttonWidth = unitWidth * 11;
        const buttonHeight = unitHeight * 12;
        ctx.save();
        for (const [index, iconId] of this.buttonsUpperInfo.entries()) {
          ctx.drawImage(this.images.buttonsUpper,
              (this.selectedButtonUpper === iconId ? 22 : 0), 24 * iconId, 22, 24,
              canvas.width - unitWidth * 70 * 0.8 - unitWidth * (index + 2) - (index + 1) * buttonWidth,
              unitHeight,
              buttonWidth, buttonHeight);
        }
        ctx.restore();
      }
      // message box
      if (this.messageBoxTimer < 120) {
        ++this.messageBoxTimer;
        ctx.save();
        const panelWidth = unitWidth * 70 * 0.8;
        const panelHeight = unitHeight * 37 * 0.8;
        let deltaY = 0;
        if (this.messageBoxTimer < 20)
          deltaY = +(panelHeight * (1 - this.messageBoxTimer / 20));
        else if (this.messageBoxTimer > 100)
          deltaY = +(panelHeight * ((this.messageBoxTimer - 100) / 20));
        ctx.drawImage(this.images.messageBox,
            0, 0, 70, 37,
            canvas.width - panelWidth - unitWidth, deltaY + canvas.height - panelHeight - unitHeight,
            panelWidth, panelHeight);
        ctx.fillStyle = "#000000";
        for (const [index, t] of this.messageBoxMessages.entries()) {
          ctx.fillText(t, canvas.width - unitWidth * 65 * 0.8, deltaY + canvas.height - unitHeight * (28 - index * 10) * 0.8);
        }
        ctx.restore();
      }
    },
    buttonLowerClicked: function (buttonIndex) {
      if (buttonIndex <= 4) {
        if (this.selectedButtonLower !== buttonIndex) {
          this.selectedButtonLower = buttonIndex;
          if (buttonIndex === 4 && $game.selectedSampling !== 0 && $game.selectedSampling !== null) {
            this.postMessage($item.getItemInfo($game.selectedSampling).name + '：\n点击地块进行种植；\n在背包里调整作物。');
          } else {
            this.postMessage(this.toolBoxDescriptions[buttonIndex][this.selectedEntityIndex === -1 ? 0 : 1]);
          }
        } else {
          this.selectedButtonLower = -1;
          this.selectedEntityIndex = -1;
        }
      }
    },
    buttonUpperClicked: function (buttonIndex, down) {
      if (down) {
        this.selectedButtonUpper = buttonIndex;
      } else {
        if (this.selectedButtonUpper === 4) {
          this.modalShowing = true;
          this.$refs.quit_game_modal.show(() => {
            this.modalShowing = false;
            this.$emit('reloadPlayerData');
          })
        } else if (this.selectedButtonUpper === 2) {
          this.modalShowing = true;
          this.$refs.shop_modal.show(() => {
            this.modalShowing = false;
            this.$emit('reloadPlayerData');
          })
        } else if (this.selectedButtonUpper === 7) {
          this.modalShowing = true;
          this.$refs.inv_modal.show(() => {
            this.modalShowing = false;
            this.$emit('reloadPlayerData');
          })
        } else if(this.selectedButtonUpper === 5) {
          this.modalShowing = true;
          this.$refs.check_solar_term_modal.show($game.solarTerm, () => {
            this.modalShowing = false;
          })
        } else if(this.selectedButtonUpper === 6) {
          this.showTutorialModal(true);
        } else if (this.selectedButtonUpper === 8) {
          window.location.replace('/admin');
        } else if(this.selectedButtonUpper === 10){
          this.modalShowing = true;
          this.$refs.info_modal.show(() => {
            this.modalShowing = false;
          })
        } else if(this.selectedButtonUpper === 9) {
          this.modalShowing = true;
          this.$refs.bulletin_modal.show(() => {
            this.modalShowing = false;
          })
        }
        this.selectedButtonUpper = -1;
      }
    },
    postMessage: function (message) {
      if (this.messageBoxTimer >= 120)
        this.messageBoxTimer = 0;
      else if (this.messageBoxTimer > 20 && this.messageBoxTimer < 100)
        this.messageBoxTimer = 20;
      else if (this.messageBoxTimer >= 100)
        this.messageBoxTimer = 120 - this.messageBoxTimer;
      this.messageBoxMessages = message.split('\n');
    },
    confirmToolUsage: function (toolType, entityIndex) {
      if (toolType === 0) {
        axios.post(`/api/game/remove?soilId=${entityIndex + 1}`).then(() => {
          this.postMessage('铲除成功。');
          this.$emit('reloadOnePlant', entityIndex);
        }).catch(error => {
          if (error.response.status === 404)
            this.postMessage(`铲除失败：\n无效的土地。`);
          else if (error.response.status === 401)
            this.postMessage(`铲除失败：\n没有权限。`);
        });
      } else if (toolType === 1) {
        axios.post(`/api/game/water?soilId=${entityIndex + 1}`).then(() => {
          this.postMessage('浇水成功\n作物生长已加速。');
          this.$emit('reloadOnePlant', entityIndex);
        }).catch(error => {
          if (error.response.status === 404)
            this.postMessage(`浇水失败：\n无效的土地。`);
          else if (error.response.status === 401)
            this.postMessage(`浇水失败：\n没有权限。`);
          else if (error.response.status === 418)
            this.postMessage(`浇水失败：\n无法浇水。`);
          else if (error.response.status === 405)
            this.postMessage(`浇水失败：\n已经成熟。`);
          else if (error.response.status === 402)
            this.postMessage(`浇水失败：\n没有水滴。`);
        });
      } else if (toolType === 2) {
        axios.post(`/api/game/harvest?soilId=${entityIndex + 1}`).then(res => {
          this.postMessage(`采收成功\n获得${$item.getItemInfo(res.data.itemId).name}x${res.data.count}。`);
          this.$emit('reloadOnePlant', entityIndex);
        }).catch(error => {
          if (error.response.status === 404)
            this.postMessage(`采收失败：\n无效的土地。`);
          else if (error.response.status === 401)
            this.postMessage(`采收失败：\n没有权限。`);
          else if (error.response.status === 418)
            this.postMessage(`采收失败：\n无法采收。`);
          else if (error.response.status === 405)
            this.postMessage(`采收失败：\n没有成熟。`);
        });
      } else if (toolType === 3) {
        axios.post(`/api/game/check?soilId=${entityIndex + 1}`).then(res => {
          if (res.data.species === -1) {
            this.postMessage('枯萎的灌木');
          } else if (res.data.species === 0) {
            this.postMessage('空土地');
          } else {
            this.postMessage(`${$game.speciesNames[res.data.species - 1]}：\n状态：${$game.stageNames[res.data.stage]}\n` + (res.data.stage === 2 ? '' : `预计还需${res.data.maxProgress - res.data.progress}小时`));
          }
          this.$emit('reloadOnePlant', entityIndex);
        }).catch(error => {
          if (error.response.status === 404)
            this.postMessage(`检查失败：\n无效的土地。`);
          else if (error.response.status === 401)
            this.postMessage(`检查失败：\n没有权限。`);
        });
      } else if (toolType === 4) {
        if ($game.selectedSampling === null || $game.selectedSampling === 0) {
          this.postMessage(`种植失败：\n未选择幼苗。`);
          return;
        }
        axios.post(`/api/game/plant?soilId=${entityIndex + 1}&itemId=${$game.selectedSampling}`).then(res => {
          if (res.status === 226)
            this.postMessage(`种植失败：\n已经种植。`);
          else
            this.postMessage(`种植成功。`);
          this.$emit('reloadOnePlant', entityIndex);
        }).catch(error => {
          if (error.response.status === 404)
            this.postMessage(`种植失败：\n无效的土地。`);
          else if (error.response.status === 401)
            this.postMessage(`种植失败：\n没有权限。`);
          else if (error.response.status === 406)
            this.postMessage(`种植失败：\n没有此幼苗。`);
          else if (error.response.status === 402)
            this.postMessage(`种植失败：\n幼苗已用尽。`);
        });
      }
    },
    mouseDown: function (x, y) {
      const canvas = document.getElementById('hud');
      const canvasW = canvas.getBoundingClientRect().width;
      const canvasH = canvas.getBoundingClientRect().height;
      const onCanvasX = x / canvasW * canvas.width;
      const onCanvasY = y / canvasH * canvas.height;

      // Check buttons lower
      if (this.$store.getters.getRole !== 0) {
        const buttonsCount = this.buttons.length;
        const ratio = canvasW / canvasH;
        const unitWidth = canvas.width / Math.sqrt(ratio) * 0.005;
        const unitHeight = canvas.height / Math.sqrt(1 / ratio) * 0.005;
        const buttonWidth = unitWidth * 15;
        const buttonHeight = unitHeight * 17;
        const uiWidth = buttonWidth * buttonsCount + unitWidth * (buttonsCount - 1);
        let radius = 13;
        let ind = -1;
        for (const [index, iconId] of this.buttons.entries()) {
          const buttonCenterX = canvas.width / 2 - uiWidth / 2 + index * (buttonWidth + unitWidth) + buttonWidth / 2;
          const buttonCenterY = canvas.height - buttonHeight / 2 - unitHeight;
          const distance = Math.sqrt(Math.pow(buttonCenterX - onCanvasX, 2)
              + Math.pow(buttonCenterY - onCanvasY, 2));
          if (distance < radius) {
            radius = distance;
            ind = iconId;
          }
        }
        if (ind !== -1) {
          this.buttonLowerClicked(ind);
          return
        }
      }
      // check buttons upper
      {
        const ratio = canvasW / canvasH;
        const unitWidth = canvas.width / Math.sqrt(ratio) * 0.005;
        const unitHeight = canvas.height / Math.sqrt(1 / ratio) * 0.005;
        const buttonWidth = unitWidth * 11;
        const buttonHeight = unitHeight * 12;
        let radius = 13;
        let ind = -1;
        for (const [index, iconId] of this.buttonsUpperInfo.entries()) {
          const buttonCenterX = canvas.width - unitWidth * 70 * 0.8 - unitWidth * (index + 2) - (index + 0.5) * buttonWidth;
          const buttonCenterY = unitHeight + buttonHeight / 2;
          const distance = Math.sqrt(Math.pow(buttonCenterX - onCanvasX, 2)
              + Math.pow(buttonCenterY - onCanvasY, 2));
          if (distance < radius) {
            radius = distance;
            ind = iconId;
          }
        }
        if (ind !== -1) {
          this.buttonUpperClicked(ind, true);
          return
        }
      }
      // Check entities
      if (this.selectedButtonLower !== -1 && this.selectedButtonLower < 5 && this.$store.getters.getRole !== 0) {
        const worldAreaToDisplay = 30000;
        const aspectRatio = canvasW / canvasH;
        const viewportWidth = Math.sqrt(worldAreaToDisplay * aspectRatio);
        const viewportHeight = Math.sqrt(worldAreaToDisplay / aspectRatio);
        let viewportX = $game.playerPositionX - viewportWidth / 2;
        let viewportY = $game.playerPositionY - viewportHeight / 2;
        if (viewportX < 0) {
          viewportX = 0;
        }
        if (viewportY < 0) {
          viewportY = 0;
        }
        if (viewportX + viewportWidth > $game.worldWidth) {
          viewportX = $game.worldWidth - viewportWidth;
        }
        if (viewportY + viewportHeight > $game.worldHeight) {
          viewportY = $game.worldHeight - viewportHeight;
        }
        for (const [index, val] of $game.soilPosition.entries()) {
          const radius = 18;
          const distance = Math.sqrt(Math.pow(canvas.width / viewportWidth * (val[0] - viewportX) - onCanvasX, 2)
              + Math.pow(canvas.height / viewportHeight * (val[1] - viewportY) - onCanvasY, 2));
          if (distance < radius) {
            if (this.selectedEntityIndex === -1 || this.selectedEntityIndex !== index) {
              this.selectedEntityPos = val;
              this.selectedEntityIndex = index;
              this.postMessage(this.toolBoxDescriptions[this.selectedButtonLower][1]);
            } else {
              this.confirmToolUsage(this.selectedButtonLower, this.selectedEntityIndex);
              this.selectedEntityIndex = -1;
            }
            return
          }
        }
      }
    },
    mouseUp: function () {
      this.buttonUpperClicked(-1, false);
    },
    onKeyEvent: function (key, down) {
      if (down) {
        if (key === 'a' || key === 'A' || key === 'ArrowLeft') {
          this.keyPressing = 1;
        } else if (key === 'w' || key === 'W' || key === 'ArrowUp') {
          this.keyPressing = 0;
        } else if (key === 's' || key === 'S' || key === 'ArrowDown') {
          this.keyPressing = 2;
        } else if (key === 'd' || key === 'D' || key === 'ArrowRight') {
          this.keyPressing = 3;
        }
      } else {
        this.keyPressing = -1;
      }
    },
    walkPlayer: function (dx, dy) {
      const newPositionX = $game.playerPositionX + dx;
      const newPositionY = $game.playerPositionY + dy;
      for (const area of $game.walkFreeAreas) {
        if (newPositionX >= area[0] && newPositionY >= area[1] && newPositionX <= area[2] && newPositionY <= area[3]) {
          $game.playerPositionX = newPositionX;
          $game.playerPositionY = newPositionY;
          $game.playerIsWalking = 1;
          if (dx > 0) $game.playerOrientation = 3;
          if (dx < 0) $game.playerOrientation = 1;
          if (dy > 0) $game.playerOrientation = 2;
          if (dy < 0) $game.playerOrientation = 0;
          return
        }
      }
    },
    playerWalkerTimer: function () {
      const speed = 3;
      if (this.keyPressing !== -1) {
        if (this.keyPressing === 1) {
          this.walkPlayer(-speed, 0);
        } else if (this.keyPressing === 0) {
          this.walkPlayer(0, -speed);
        } else if (this.keyPressing === 2) {
          this.walkPlayer(0, +speed);
        } else if (this.keyPressing === 3) {
          this.walkPlayer(+speed, 0);
        }
        $game.playerIsWalking = true;
      } else {
        $game.playerIsWalking = false;
      }
      setTimeout(this.playerWalkerTimer, 50);
    },
    showTutorialModal: function (force = false) {
      if (force || !this.$store.getters.getHelpShown) {
        this.modalShowing = true;
        this.$refs.tutorial_modal.show(() => {
          this.modalShowing = false;
          this.$store.commit('setHelpShown');
        });
      }
    }
  },
  mounted() {
    this.initializeImages();
    if (this.$store.getters.getRole === 2) {
      this.buttonsUpperInfo.push(8);
    }
    if (this.$store.getters.getRole === 0) {
      this.buttonsUpperInfo = [6, 4];
    }
    setTimeout(this.playerWalkerTimer, 50);
    setTimeout(this.showTutorialModal, 300);
    document.addEventListener('mousedown', (event) => {
      if (event.button === 0 && !this.modalShowing)
        this.mouseDown(event.x, event.y)
    });
    document.addEventListener('mouseup', (event) => {
      if (event.button === 0 && !this.modalShowing)
        this.mouseUp(event.x, event.y)
    });
    document.addEventListener('keydown', (event) => {
      if (!this.modalShowing) {
        this.onKeyEvent(event.key, true);
      } else {
        this.onKeyEvent(event.key, false);
      }
    }, false);
    document.addEventListener('keyup', (event) => {
      this.onKeyEvent(event.key, false);
    }, false);
  }
}
</script>

<style scoped>
#hud {
  width: 100%;
  height: 100%;
  position: absolute;
  image-rendering: pixelated;
}
</style>