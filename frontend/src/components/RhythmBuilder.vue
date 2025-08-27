<template>
  <v-row>
    <v-col>
      <v-switch v-model="simpleView" @click="switchAdvancedSettings" label="Advanced view"></v-switch>
    </v-col>
  </v-row>
  <v-card>
    <v-card-title class="text-left">{{ rhythmObj.name }}</v-card-title>
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
              <v-col class="d-flex flex-column" cols="auto" v-for="(note, index) in rhythmObj.rhythm" :key="index">
                <v-btn
                  :icon="notesIcons[index]"
                  @click="nextNote(index)"
                  v-model="rhythmObj.rhythm[index]"
                  :class="{
                            'highlighted': currentIndex === index,
                            'violet-highlight': isPlaying && currentIndex === index
                          }"
                />
                <v-btn
                  class="mt-auto align-self-end mt-n2"
                  v-if="index === rhythmObj.rhythm.length - 1"
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
            <v-row class="mt-0">
              <v-col class="d-flex flex-column" cols="auto" v-for="(note, index) in rhythmObj.rhythm" :key="index">
                <v-btn
                  variant="plain"
                  :icon="notesIcons[index]"
                  @click="nextNote(index)"
                  v-model="rhythmObj.rhythm[index]"
                  :class="{
                            'highlighted': currentIndex === index,
                            'violet-highlight': isPlaying && currentIndex === index
                          }"
                >{{subDivisions[index]}}</v-btn>
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
import { Note, NoteIcon } from '@/types/uiTypes'
import { Rhythm } from '@/types/riTypes'

const tempo = ref(80)
const blankIcon = 'mdi-check-box-outline-blank'
const notes = ref<Note[]>([])
const notesIcons = ref<string[]>([])
const subDivisions = ref<string[]>([])
const noteOptions: Note[] = ['', '1', '2', '4', '8', '16']
const subDivisionOptions: string[][] = [[], ['&'], ['&', 'a'], ['e', '&', 'a']]
const noteIconsOptions: NoteIcon[] = [blankIcon, 'mdi-music-note-whole', 'mdi-music-note-half', 'mdi-music-note-quarter', 'mdi-music-note-eighth', 'mdi-music-note-sixteenth']
const rhythmObj = ref<Rhythm>({ name: 'la-bamba', rhythm: [false], length: 1, subDivision: 1 })
const subDivision = ref(1)
const simpleView = ref(false)
const audio = new Audio('/default-click.mp3')

onMounted(async () => {
  rhythmObj.value = await rhythmsService.getRhythm(1)
  buildRhythm()
})

const buildRhythm = () => {
  notes.value = rhythmObj.value.rhythm.map(r => r ? '4' : '')
  notesIcons.value = rhythmObj.value.rhythm.map(r => r ? noteIconsOptions[3] : blankIcon)

  let i = 0
  subDivisions.value = new Array(rhythmObj.value.length)

  while (i < rhythmObj.value.length) {
    subDivisions.value[i] = getSubdivisionValue(i)
    i++
  }
}

const switchAdvancedSettings = () => {
  subDivision.value = simpleView.value ? 1 : rhythmObj.value.subDivision
  buildRhythm()
}

const appendNote = () => {
  for (let i = 0; i < subDivision.value; i++) {
    rhythmObj.value.rhythm.push(false)
    notesIcons.value.push(blankIcon)
    subDivisions.value.push(getSubdivisionValue(rhythmObj.value.rhythm.length + i))
    rhythmObj.value.length++
  }
}

const getSubdivisionValue = (index: number): string => {
  if (index % subDivision.value === 0) {
    return index / subDivision.value + 1
  } else {
    return subDivisionOptions[subDivision.value - 1][index % subDivision.value - 1]
  }
}

const nextNote = (index: number) => {
  // const currentIndex = noteOptions.indexOf(notes.value[index])
  // const nextIndex = (currentIndex + 1) % noteOptions.length
  // notes.value[index] = noteOptions[nextIndex]
  // notesIcons.value[index] = noteIconsOptions[nextIndex]
  if (rhythmObj.value.rhythm[index]) {
    notesIcons.value[index] = noteIconsOptions[0]
  } else {
    notesIcons.value[index] = noteIconsOptions[3]
  }

  rhythmObj.value.rhythm[index] = !rhythmObj.value.rhythm[index]
}

const deleteLastNote = () => {
  for (let i = 0; i < subDivision.value; i++) {
    rhythmObj.value.rhythm.splice(rhythmObj.value.rhythm.length - 1, 1)
    rhythmObj.value.length--
  }
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
    currentIndex.value = (currentIndex.value + 1) % rhythmObj.value.length
    if (rhythmObj.value.rhythm[currentIndex.value]) {
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
  console.info('RhytmBuilder unmounted')
})

</script>

<style scoped>
.violet-highlight {
  background-color: #7B1FA2 !important;
  color: white !important;
}
</style>
