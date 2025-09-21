<template>
  <v-container>
    <v-row class="mb-10 mt-10">
      <v-col cols="4">
        <RhythmsList :rhythms="rhythmsList" @open-rhythm="openRhythm" @open-selected="openSeveral"/>
      </v-col>
      <v-col cols="8">
        <v-btn
          class="mb-4"
          color="primary"
          @click="startPlayback">
          <v-icon>mdi-play</v-icon>
          <span v-if="isPlaying">Stop</span>
          <span v-else>Play</span>
        </v-btn>
         <v-list density="compact">
           <v-list-item v-for="(rhythm, index) in selectedRhythms" :key="index">
            <v-row>
              <v-col>
                <RhythmBuilder :rhythm="selectedRhythms[index]" :play="isPlaying"/>
              </v-col>
            </v-row>
          </v-list-item>
        </v-list>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Rhythm } from '@/types/riTypes'

import { rhythmsService } from '@/api/rhythmsService'

// components
import RhythmBuilder from '@/components/RhythmBuilder.vue'
import RhythmsList from '@/components/RhythmsList.vue'

const rhythmsList = ref<Rhythm[]>([])
const selectedRhythms = ref<Rhythm[]>([])

const isPlaying = ref(false)

onMounted(async () => {
  rhythmsList.value = await rhythmsService.getRhythmsList()
  selectedRhythms.value = []
})

const openRhythm = async (rhythm: Rhythm) => {
  selectedRhythms.value = [rhythm]
}

const openSeveral = async (rhythms: Rhythm[]) => {
  console.log(rhythms)
  selectedRhythms.value = rhythms
}

const startPlayback = () => {
  if (isPlaying.value) {
    stopPlayback()
  } else {
    isPlaying.value = true
  }
}

const stopPlayback = () => {
  isPlaying.value = false
}

</script>
