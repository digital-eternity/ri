<template>
  <v-container>
    <v-row class="mb-10 mt-10">
      <v-col cols="4">
        <RhythmsList :rhythms="rhythmsList" @open-rhythm="openRhythm"/>
      </v-col>
      <v-col cols="8">
        <RhythmBuilder v-if="currentRhythm" :rhythm="currentRhythm"/>
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
const currentRhythm = ref<Rhythm | null>(null)

onMounted(async () => {
  rhythmsList.value = await rhythmsService.getRhythmsList()
  currentRhythm.value = rhythmsList.value[0]
})

onMounted(async () => {
  rhythmsList.value = await rhythmsService.getRhythmsList()
  currentRhythm.value = rhythmsList.value[0]
})

const openRhythm = async (rhythm: Rhythm) => {
  currentRhythm.value = rhythm
}

</script>
