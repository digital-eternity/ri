<template>
  <v-card>
    <v-card-title class="text-left">{{ rhytmName }}</v-card-title>
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
              <v-col class="d-flex flex-column" cols="auto" v-for="(note, index) in notes" :key="index">
                <v-btn
                  :icon="notesIcons[index]"
                  @click="nextNote(index)"
                  v-model="notes[index]"
                  :class="{
                            'highlighted': currentIndex === index,
                            'violet-highlight': isPlaying && currentIndex === index
                          }"
                />
                <v-btn
                  class="mt-auto align-self-end mt-n2"
                  v-if="index === notes.length - 1"
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
import { Note, NoteIcon } from '@/types/uiTypes'

const tempo = ref(80)
const rhytmName = ref('la-bamba')
const blankIcon = 'mdi-check-box-outline-blank'
const notes = ref<Note[]>(['', '', '', '', '', '', '', ''])
const notesIcons = ref<string[]>([blankIcon, blankIcon, blankIcon, blankIcon, blankIcon, blankIcon, blankIcon, blankIcon])
const noteOptions: Note[] = ['', '1', '2', '4', '8', '16']
const noteIconsOptions: NoteIcon[] = [blankIcon, 'mdi-music-note-whole', 'mdi-music-note-half', 'mdi-music-note-quarter', 'mdi-music-note-eighth', 'mdi-music-note-sixteenth']

onMounted(async () => {
  console.info('RhytmBuilder mounted')
})

const appendNote = () => {
  notes.value.push('')
  notesIcons.value.push(blankIcon)
}

const nextNote = (index: number) => {
  const currentIndex = noteOptions.indexOf(notes.value[index])
  const nextIndex = (currentIndex + 1) % noteOptions.length
  notes.value[index] = noteOptions[nextIndex]
  notesIcons.value[index] = noteIconsOptions[nextIndex]
}

const deleteLastNote = () => {
  notes.value.splice(notes.value.length - 1, 1)
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
  currentIndex.value = 0

  interval.value = setInterval(() => {
    currentIndex.value = (currentIndex.value + 1) % notes.value.length
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
