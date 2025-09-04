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
        <v-col cols="2">
          <p><strong>Tempo:</strong></p>
          <v-text-field
            width="100"
            variant="outlined"
            density="compact"
            v-model="tempo"
          />
        </v-col>
        <v-col cols="2">
          <p><strong>Base note:</strong></p>
          <v-select
            width="200"
            variant="outlined"
            density="compact"
            v-model="rhythmObj.baseNote"
          />
        </v-col>
      </v-row>

      <v-row>
        <v-col>
        <h3>Rhythm:</h3>
        </v-col>
      </v-row>

      <v-row class="mx-0">
        <v-col class="d-flex flex-column pl-0 pr-2" cols="auto" v-for="(el, index) in uiElements" :key="index">
          <v-btn
            :icon="el.label"
            @click="switchNote(index)"
            :width="el.duration * 40"
            :height="40"
            :class=" {
                      'highlighted': currentIndex === index,
                      'violet-highlight': isPlaying && currentIndex === index
                    }"
            :style="`border-radius: ${advancedView ? '0px' : '0px' }`"
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
      <v-row class="mx-0">
        <v-col class="d-flex flex-column pl-0 pr-2" cols="auto" v-for="(el, index) in uiElements" :key="index">
          <v-btn
            :icon="''"
            variant="text"
            :width="el.duration * 40"
            :class=" {
                      'highlighted': currentIndex === index,
                      'violet-highlight': isPlaying && currentIndex === index
                    }"
          >{{ index + 1 }}</v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
        <v-btn
          class="mb-4"
          color="primary"
          @click="startPlayback">
            <v-icon>mdi-play</v-icon>
            <span v-if="isPlaying">Stop</span>
            <span v-else>Play</span>
          </v-btn>
          <v-row>
            <v-col>
          <v-btn
            class="mb-4"
            color="#5dbb63"
            @click="updateRhythm">
              Save
            </v-btn>
            <v-btn
              color="#5dbb63"
              class="mb-4 ml-5"
              @click="addNewRhythm">
                Save As New
              </v-btn>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, onBeforeUnmount } from 'vue'
import { rhythmsService } from '@/api/rhythmsService'
import { NoteIcon } from '@/types/uiTypes'
import { Rhythm, RhythmElementType, RhythmElementDuration } from '@/types/riTypes'

interface UiRhythmElement {
  label: NoteIcon,
  duration: number
}

interface Props {
  rhythm: Rhythm
}

const props = defineProps<Props>()

const tempo = ref(140)
const noteOptions: RhythmElementDuration[] = ['WHOLE', 'HALF', 'QUARTER', 'EIGHT', 'SIXTEENTH', 'THIRTYSECOND']
const sizeOptions: number[] = [4, 2, 1, 0.5, 0.25, 0.125]
const subDivisionOptions: string[][] = [[], ['&'], ['&', 'a'], ['e', '&', 'a']]
const noteIconsOptions: NoteIcon[] = ['mdi-music-note-whole', 'mdi-music-note-half', 'mdi-music-note-quarter', 'mdi-music-note-eighth', 'mdi-music-note-sixteenth', 'mdi-music-note-sixteenth']
const rhythmObj = ref<Rhythm>({ name: '', baseNote: '' })
const advancedView = ref(false)
const audio = new Audio('/default-click.mp3')

const uiElements = ref<UiRhythmElement[]>([])

// onMounted(async () => {
//   rhythmObj.value = await rhythmsService.getRhythm(1)
//   rebuildRhythm()
// })

watch(() => props.rhythm, async (newRhythm) => {
  rhythmObj.value = newRhythm
  rebuildRhythm()
})

const buildRhythm = () => {
  if (rhythmObj.value) {
    uiElements.value = []
    const reTypes = rhythmObj.value.rhythm.flatMap(r => r.rhythmElements).map(s => s.type)

    for (let i = 0; i < rhythmObj.value.rhythm.length; i++) {
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
    const reTypes = rhythmObj.value.rhythm.flatMap(r => r.rhythmElements).map(s => s.type)
    const reDurations = rhythmObj.value.rhythm.flatMap(r => r.rhythmElements).map(s => s.duration)

    for (let i = 0; i < rhythmObj.value.rhythm.length; i++) {
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
  rhythmObj.value?.rhythm.push({
    label: '',
    isStrong: false,
    rhythmElements: [
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

  if (rhythmObj.value.rhythm[index].rhythmElements[0].type === 'PAUSE') {
    rhythmObj.value.rhythm[index].rhythmElements[0].type = 'NOTE'
  } else {
    rhythmObj.value.rhythm[index].rhythmElements[0].type = 'PAUSE'
  }

  rebuildRhythm()
}

const deleteLastNote = () => {
  rhythmObj.value?.rhythm.splice(rhythmObj.value.rhythm.length - 1, 1)
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

const updateRhythm = () => {
  if (rhythmObj.value.id) {
    rhythmsService.updateRhythm(rhythmObj.value.id, rhythmObj.value)
  }
}

const addNewRhythm = () => {
  rhythmsService.createRhythm(rhythmObj.value)
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
