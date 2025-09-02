<template>
  <v-row>
    <v-col>
      <v-switch @click="switchMode" label="Advanced view"></v-switch>
    </v-col>
  </v-row>
  <v-card>
    <v-card-title class="text-left">{{ rhythmObj ? rhythmObj.name : '' }}</v-card-title>
    <v-card-text class="text-left">
      <v-row>
        <v-col>
          <div class="mt-5">
            <p><strong>Tempo:</strong></p>

            <v-row>
              <v-col>
                <v-text-field
                  width="100"
                  variant="outlined"
                  density="compact"
                  v-model="tempo"
                />
              </v-col>
            </v-row>
          </div>

          <div class="mt-5">
            <p><strong>Rhytm:</strong></p>

            <v-row>
              <v-col class="d-flex flex-column" cols="auto" v-for="(el, index) in uiElements" :key="index">
                <v-btn
                  :icon="el.label"
                  @click="switchNote(index)"
                  :width="el.duration * 50"
                  :class=" {
                            'highlighted': currentIndex === index,
                            'violet-highlight': isPlaying && currentIndex === index
                          }"
                  :style="`border-radius: ${advancedView ? '0px' : '50%' }`"
                />
                <v-btn
                  class="mt-auto align-self-end mt-n2"
                  v-if="index === uiElements.length - 1"
                  density="compact"
                  size="small"
                  icon="mdi-trash-can"
                  @click="deleteLastNote"

                />
              </v-col>
              <v-col>
                <v-btn
                  icon="mdi-plus"
                  @click="appendNote"
                />
              </v-col>
            </v-row>
          </div>

        </v-col>
      </v-row>

      <v-btn
            class="mb-4"
            color="primary"
            @click="startPlayback"
          >
            <v-icon>mdi-play</v-icon>
            <span v-if="isPlaying">Stop</span>
            <span v-else>Play</span>

          </v-btn>

    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { rhythmsService } from '@/api/rhythmsService'
import { NoteIcon } from '@/types/uiTypes'
import { Rhythm, RhythmElementType, RhythmElementDuration } from '@/types/riTypes'

interface UiRhythmElement {
  label: NoteIcon,
  duration: number
}

const tempo = ref(140)
const noteOptions: RhythmElementDuration[] = ['WHOLE', 'HALF', 'QUARTER', 'EIGHT', 'SIXTEENTH', 'THIRTYSECOND']
const sizeOptions: number[] = [4, 2, 1, 0.5, 0.25, 0.125]
const subDivisionOptions: string[][] = [[], ['&'], ['&', 'a'], ['e', '&', 'a']]
const noteIconsOptions: NoteIcon[] = ['mdi-music-note-whole', 'mdi-music-note-half', 'mdi-music-note-quarter', 'mdi-music-note-eighth', 'mdi-music-note-sixteenth', 'mdi-music-note-sixteenth']
const rhythmObj = ref<Rhythm | null>()
const advancedView = ref(false)
const audio = new Audio('/default-click.mp3')

const uiElements = ref<UiRhythmElement[]>([])

onMounted(async () => {
  rhythmObj.value = await rhythmsService.getRhythm(1)
  rebuildRhythm()
})

const buildRhythm = () => {
  if (rhythmObj.value) {
    uiElements.value = []
    const reTypes = rhythmObj.value.aRhythm.flatMap(r => r.blockSubdivisions).map(s => s.type)

    for (let i = 0; i < rhythmObj.value.aRhythm.length; i++) {
      const label = reTypes[i] === 'PAUSE' ? '' : 'mdi-circle'

      uiElements.value.push({
        label: label,
        duration: sizeOptions[2]
      })
    }
  }
}

const buildRhythmAdvanced = () => {
  if (rhythmObj.value) {
    uiElements.value = []
    const reTypes = rhythmObj.value.aRhythm.flatMap(r => r.blockSubdivisions).map(s => s.type)
    const reDurations = rhythmObj.value.aRhythm.flatMap(r => r.blockSubdivisions).map(s => s.duration)

    for (let i = 0; i < rhythmObj.value.aRhythm.length; i++) {
      const label = reTypes[i] === 'PAUSE' ? '' : noteIconsOptions[noteOptions.indexOf(reDurations[i])]

      uiElements.value.push({
        label: label,
        duration: sizeOptions[noteOptions.indexOf(reDurations[i])]
      })
    }
  }
}

const rebuildRhythm = () => {
  if (advancedView.value) {
    buildRhythmAdvanced()
  } else {
    buildRhythm()
  }
}

const switchMode = () => {
  advancedView.value = !advancedView.value
  rebuildRhythm()
}

const appendNote = () => {
  rhythmObj.value?.aRhythm.push({
    label: '',
    isStrong: false,
    blockSubdivisions: [
      {
        type: 'PAUSE',
        duration: 'QUARTER'
      }
    ]
  })

  rebuildRhythm()
}

const switchNote = (index: number) => {
  if (!rhythmObj.value) {
    return
  }

  if (rhythmObj.value.aRhythm[index].blockSubdivisions[0].type === 'PAUSE') {
    rhythmObj.value.aRhythm[index].blockSubdivisions[0].type = 'NOTE'
  } else {
    rhythmObj.value.aRhythm[index].blockSubdivisions[0].type = 'PAUSE'
  }

  rebuildRhythm()
}

const deleteLastNote = () => {
  rhythmObj.value?.aRhythm.splice(rhythmObj.value.aRhythm.length - 1, 1)
  rebuildRhythm()
}

const isPlaying = ref(false)
const currentIndex = ref(0)
const interval = ref(0)

const startPlayback = () => {
  if (isPlaying.value) {
    stopPlayback()
    return
  }

  isPlaying.value = true
  currentIndex.value = -1

  interval.value = setInterval(() => {
    currentIndex.value = (currentIndex.value + 1) % uiElements.value.length
    if (uiElements.value[currentIndex.value].label !== '') {
      audio.play()
    }
  }, 60 / tempo.value * 1000)
}

const stopPlayback = () => {
  isPlaying.value = false
  currentIndex.value = 0
  if (interval.value) {
    clearInterval(interval.value)
  }
}

onBeforeUnmount(async () => {
  stopPlayback()
})

</script>

<style scoped>
.violet-highlight {
  background-color: #7B1FA2 !important;
  color: white !important;
}
</style>
