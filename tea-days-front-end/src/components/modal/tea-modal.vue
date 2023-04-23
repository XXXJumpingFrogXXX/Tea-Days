<template>
  <vue-final-modal v-model="showModal" :click-to-close="false" name="teaModal"
                   classes="d-flex justify-content-center align-items-center-safe overflow-auto">
    <div class="game-card">
      <div class="game-card-content content-max-width">
        <h3 class="card-title text-center">茶叶图谱</h3>
        <hr>
        <div class="m-3">{{ this.teaData[this.tea] }}</div>
        <div>
          <div>
            <h5 class="card-title text-center m-2">属于这个类别的茶叶</h5>
          </div>
          <ul class="nav nav-tabs" id="myTab" role="tablist">
            <li class="nav-item" role="presentation">
              <button class="nav-link active" id="home-tab" data-bs-toggle="tab" data-bs-target="#home" type="button"
                      role="tab" aria-controls="home" aria-selected="true">
                <span class="text-dark">{{ this.teaTypeName[5 * this.tea] }}</span></button>
            </li>
            <li class="nav-item" role="presentation">
              <button class="nav-link" id="profile-tab" data-bs-toggle="tab" data-bs-target="#profile" type="button"
                      role="tab" aria-controls="profile" aria-selected="false"><span
                  class="text-dark">{{ this.teaTypeName[5 * this.tea + 1] }}</span></button>

            </li>
            <li class="nav-item" role="presentation">
              <button class="nav-link" id="contact-tab" data-bs-toggle="tab" data-bs-target="#contact" type="button"
                      role="tab" aria-controls="contact" aria-selected="false"><span
                  class="text-dark">{{ this.teaTypeName[5 * this.tea + 2] }}</span></button>

            </li>
            <li class="nav-item" role="presentation">
              <button class="nav-link" id="contact2-tab" data-bs-toggle="tab" data-bs-target="#contact2" type="button"
                      role="tab" aria-controls="contact2" aria-selected="false"><span
                  class="text-dark">{{ this.teaTypeName[5 * this.tea + 3] }}</span></button>

            </li>
            <li id="asa" class="nav-item" role="presentation">
              <button class="nav-link" id="contact3-tab" data-bs-toggle="tab" data-bs-target="#contact3" type="button"
                      role="tab" aria-controls="contact3" aria-selected="false"><span
                  class="text-dark">{{ this.teaTypeName[5 * this.tea + 4] }}</span></button>
            </li>
          </ul>
          <div class="tab-content" id="myTabContent">
            <div class="tab-pane fade show active m-3" id="home" role="tabpanel" aria-labelledby="home-tab">
              {{ this.teaTypeData[5 * this.tea] }}
            </div>
            <div class="tab-pane fade m-3" id="profile" role="tabpanel" aria-labelledby="profile-tab">
              {{ this.teaTypeData[5 * this.tea + 1] }}
            </div>
            <div class="tab-pane fade m-3" id="contact" role="tabpanel" aria-labelledby="contact-tab">
              {{ this.teaTypeData[5 * this.tea + 2] }}
            </div>
            <div class="tab-pane fade m-3" id="contact2" role="tabpanel" aria-labelledby="contact2-tab">
              {{ this.teaTypeData[5 * this.tea + 3] }}
            </div>
            <div class="tab-pane fade m-3" id="contact3" role="tabpanel" aria-labelledby="contact3-tab">
              {{ this.teaTypeData[5 * this.tea + 4] }}
            </div>
          </div>
        </div>
        <div class="d-flex justify-content-center">
          <left-button @click="tea > 0 ? tea -= 1 : null;" class="m-1"></left-button>
          <cancel-button class="m-1" @click="this.hide()"></cancel-button>
          <right-button @click="tea + 1 < teaData.length ? tea += 1 : null;" class="m-1"></right-button>
        </div>
      </div>
    </div>
  </vue-final-modal>
</template>

<script>
import {$vfm, VueFinalModal} from 'vue-final-modal'
import CancelButton from "@/components/buttons/cancel-button";
import RightButton from "@/components/buttons/right-button";
import LeftButton from "@/components/buttons/left-button";
import axios from "axios";

export default {
  name: "tea-modal",
  data: function () {
    return {
      showModal: false,
      hideCallback: null,
      teaData: [],
      teaTypeData: [],
      teaTypeName: [],
      tea: 0,
      currentType: -1,
      lastType: 0,
      typeData: ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'e', 'g', 's', 's'],
    }
  },
  components: {
    CancelButton,
    VueFinalModal,
    LeftButton,
    RightButton,
  },
  methods: {
    show: function (hideCallback) {
      this.hideCallback = hideCallback;
      $vfm.show('teaModal');
      this.getData();
    },
    hide: function () {
      this.currentType = -1;
      let ret = $vfm.hide('teaModal');
      if (this.hideCallback !== null)
        ret.then(this.hideCallback)
    },
    getData: function () {
      axios.get(`/api/things/get?type=${0}`).then(response => {
        this.teaData = response.data;
      })
      axios.get(`/api/things/gettea`).then(response => {
        this.teaTypeData = response.data;
      })
      axios.get(`/api/things/getteaname`).then(response => {
        this.teaTypeName = response.data;
      })
    }
  },
  mounted() {
    this.getData();
  }
}
</script>

<style scoped>
.content-max-width {
  max-width: 36rem;
}
</style>