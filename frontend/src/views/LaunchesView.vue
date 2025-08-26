<template>
  <v-container>
    <v-progress-linear v-if="loading" indeterminate></v-progress-linear>
    <v-alert v-else-if="error" type="error">Error loading launches: {{ error }}</v-alert>
    <div v-else-if="launches.length === 0">No launches available.</div>

    <v-row>
      <v-text-field
        v-model="search"
        label="Search"
        prepend-inner-icon="mdi-magnify"
        variant="outlined"
        hide-details
        single-line
      />

      <v-data-table
        :items="launches"
        :headers="headers"
        :search="search"
      >
        <template v-slot:[`item.name`]="{ item }">
          <v-btn variant="text" density="compact" class="d-flex justify-start" @click="openLaunchDetails(item)"> {{item.name}}</v-btn>
        </template>

        <template v-slot:[`item.tags`]="{ item }">
          <div v-if="item.tags.length > 0" class="d-flex justify-start">
            <v-chip v-for="tag in item.tags" :key="tag" size="small" class="me-1">{{ tag }}</v-chip>
          </div>
        </template>

        <template v-slot:[`item.testCases`]="{ item }">
          <div class="d-flex justify-start">{{item.testCases.length}} tests</div>
        </template>

        <template v-slot:[`item.actions`]="{ item }">
          <div class="d-flex ga-2 justify-end">
            <v-icon
              color="medium-emphasis"
              icon="mdi-delete"
              size="small"
              @click="openLaunchDeletionConfirmation(item)"></v-icon>
          </div>
        </template>
      </v-data-table>
    </v-row>

    <ConfirmationModal
      ref="confirmationModal"
      title="Confirm Deletion"
      message="Are you sure you want to delete the launch?"
      @confirm="deleteConfirmed"
      @cancel="closeConfirmationModal"
    />

  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { launchService } from '@/api/launchService'

// components
import ConfirmationModal from '@/components/ConfirmationModal.vue'
import type { VDataTable } from 'vuetify/components'

// types
import { Launch } from '@/types/tmsTypes'
import { QafModal } from '@/types/uiTypes'
type ReadonlyHeaders = VDataTable['$props']['headers']

const headers : ReadonlyHeaders = [
  { title: 'Launch name', key: 'name', align: 'start' },
  { title: 'Tags', key: 'tags', align: 'start' },
  { title: 'Test cases', key: 'testCases', align: 'start' },
  { title: 'Actions', key: 'actions', align: 'end', sortable: false }
]

const launches = ref<Launch[]>([])
const loading = ref(true)
const error = ref<string | null>(null)
const launchInstance = ref<Launch | null>(null)
const confirmationModal = ref<QafModal | null>(null) // Type may need adjustment based on ConfirmationModal
const search = ref('')

const route = useRoute()
const vueRouter = useRouter()
const projectId = computed<string | undefined>(() => route.params.projectId as string | undefined)

onMounted(async () => {
  await fetchLaunches()
})

const fetchLaunches = async () => {
  try {
    loading.value = true
    error.value = null
    launches.value = await launchService.getLaunches(projectId.value)
  } catch (err: any) {
    console.error('Failed to fetch launches:', err)
    error.value = err.message || 'Unknown error'
  } finally {
    loading.value = false
  }
}

const openLaunchDetails = async (launch: Launch) => {
  if (!projectId.value) {
    console.error('Project ID is undefined')
    return
  }
  await vueRouter.push(`/projects/${projectId.value}/launches/${launch.id}`)
}

const openLaunchDeletionConfirmation = (launch: Launch) => {
  launchInstance.value = launch
  confirmationModal.value?.open()
}

const closeConfirmationModal = () => {
  clearLaunchUnderAction()
  confirmationModal.value?.close()
}

const deleteConfirmed = async () => {
  if (!launchInstance.value) {
    console.error('Launch ID is undefined')
    return
  }

  await deleteLaunch(launchInstance.value.id as string)
  clearLaunchUnderAction()
}

const deleteLaunch = async (id: string) => {
  try {
    await launchService.deleteLaunch(id)
    await fetchLaunches()
  } catch (error) {
    console.error('Failed to delete user:', error)
  }
}

const clearLaunchUnderAction = () => {
  launchInstance.value = null
}

</script>
