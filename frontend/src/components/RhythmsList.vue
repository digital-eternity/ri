<template>
  <v-data-table
    :items="filteredRhythms"
    :headers="headers"
    :search="search"
    density="compact"
    show-select
    v-model="selectedRhythms"
    items-per-page="20"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-text-field
          v-model="search"
          label="Search"
          prepend-inner-icon="mdi-magnify"
          variant="solo"
          hide-details
          single-line
          density="compact"
          clearable
          class="ml-5"
          width="50"
        />
        <v-select
          density="compact"
          v-model="selectedBase"
          :items="availableBases"
          label="Filter by base note"
          @change="applyFilters"
          clearable
          class="ml-5 mt-5"
          width="50"
        />
        <v-spacer/>
        <v-icon v-if="selectedRhythms.length != 0" class="mr-5"  @click="deleteSelected" icon="mdi-delete"></v-icon>
      </v-toolbar>
    </template>

    <template v-slot:[`item.name`]="{ item }">
      <div class="d-flex justify-start cursor-pointer" @click="openRhythm(item)">{{ item.name }}</div>
    </template>

    <template v-slot:[`item.baseNote`]="{ item }">
      <v-chip :key="item.baseNote" label density="comfortable" variant="outlined" size="small" :class="`${item.baseNote}`">{{ item.baseNote }}</v-chip>
    </template>
  </v-data-table>

</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

import { Rhythm } from '@/types/riTypes'

// components
import ModalWrapper from '@/components/ModalWrapper.vue'
import type { VDataTable } from 'vuetify/components'

// types
import { QafModal } from '@/types/uiTypes'
type ReadonlyHeaders = VDataTable['$props']['headers']

const search = ref('')
const availableBases = ref(['QUARTER', 'EIGHTH', 'SIXTEENTH', 'THIRTYSECOND'])
const selectedBase = ref<string | null>(null)

// List of available options for the filters
const selectedRhythms = ref<number[]>([])

interface Props {
  rhythms: Rhythm[]
}

const props = defineProps<Props>()

interface RhythmEmits {
  (e: 'open-rhythm', rhythm: Rhythm): void,
  (e: 'delete-selected', selectedRhythms: number[]): void
}

const emit = defineEmits<RhythmEmits>()

const openRhythm = (rhythm: Rhythm) => {
  emit('open-rhythm', rhythm)
}

const headers: ReadonlyHeaders = [
  { title: 'ID', key: 'id', align: 'start', width: '55px' },
  { title: 'Name', key: 'name', align: 'start' },
  { title: 'Base', key: 'baseNote', align: 'center', width: '100px' }
]

const deleteSelected = () => {
  emit('delete-selected', selectedRhythms.value)
}

const applyFilters = () => {
  // If all filters is ok and running, can do emit
}

const filteredRhythms = computed(() => {
  // First filter by values and search
  return props.rhythms
    .filter((rhythm: Rhythm) => {
      // Status filter
      if (selectedBase.value && rhythm.baseNote !== selectedBase.value) {
        return false
      }

      // Perform the search filter
      const searchTerm = search.value.toLowerCase()
      const searchString = `${rhythm.name}`.toLowerCase() // Combine relevant fields for search
      return searchString.includes(searchTerm)
    })
})

</script>

<style>

.v-chip.QUARTER {
  color: #8427F5;
}

.v-chip.EIGHTH {
  color: #00b140;
}

.v-chip.SIXTEENTH {
  color: #969696;
}

.v-data-table {
  table {
    tr {
      th,
      td {
        padding: 0 0px !important; /* Adjust values as needed */
      }
    }
  }
}
</style>
