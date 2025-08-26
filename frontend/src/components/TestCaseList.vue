<template>

  <v-row>
    <v-col cols="12" md="4">
      <v-select density="compact" v-model="selectedTag" :items="availableTags" label="Filter by Tag"
      @change="applyFilters" clearable/>
    </v-col>
    <v-col cols="12" md="4">
      <v-select density="compact" v-model="selectedStatus" :items="availableStatuses" label="Filter by Status"
        @change="applyFilters" clearable/>
    </v-col>
    <v-col cols="12" md="4">
      <v-select density="compact" v-model="selectedPriority" :items="availablePriorities" label="Filter by Priority"
        @change="applyFilters" clearable/>
    </v-col>
  </v-row>

  <v-data-table
    :items="filteredTestCases"
    :headers="headers"
    :search="search"
    density="compact"
    items-per-page="20"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-menu location="bottom end">
          <template v-slot:activator="{ props }">
            <v-text-field
              v-model="search"
              label="Search"
              prepend-inner-icon="mdi-magnify"
              variant="outlined"
              hide-details
              single-line
              density="compact"
              clearable
              class="ml-5"
            />

            <v-spacer/>
            <v-btn v-if="editable" icon="mdi-menu" variant="text" v-bind="props"></v-btn>
          </template>

          <v-list v-if="editable" density="compact">
            <v-list-item @click="deleteSelected" :disabled="selectedTestCases.length === 0">Delete Selected</v-list-item>

            <ModalWrapper
              ref="launchModal"
              title="Create Test Launch"
              :component="NewLaunchForm"
              :componentProps="{ launch: launchInstance }"
              @component-save="saveLaunch"
              @component-cancel="clearLaunchInstance"
            >
              <template v-slot:activator="{ props }">
                <v-list-item v-bind="props" @click.stop="createLaunch" :disabled="selectedTestCases.length === 0">Create launch</v-list-item>
              </template>
            </ModalWrapper>

          </v-list>
        </v-menu>
      </v-toolbar>
    </template>

    <template v-slot:[`item.selected`]="{ item }">
      <v-checkbox v-if="editable" v-model="selectedTestCases" :value="item.id" density="compact" hide-details="auto"/>
    </template>

    <template v-slot:[`item.name`]="{ item }">
      <div class="d-flex justify-start cursor-pointer" @click="openTestCase(item)">{{item.name}}</div>
    </template>

    <template v-slot:[`item.status`]="{ item }">
      <v-chip :key="item.status" density="comfortable" variant="outlined" size="small" :class="`me-1 ${item.status}`">{{ item.status }}</v-chip>
    </template>
  </v-data-table>

</template>

<script setup lang="ts">
import { defineProps, defineEmits, ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { launchService } from '@/api/launchService'

// components
import NewLaunchForm from '@/components/NewLaunchForm.vue'
import ModalWrapper from '@/components/ModalWrapper.vue'
import type { VDataTable } from 'vuetify/components'

// types
import { TestCase, Launch } from '@/types/tmsTypes'
import { QafModal } from '@/types/uiTypes'
type ReadonlyHeaders = VDataTable['$props']['headers']

const search = ref('')
const selectedTag = ref<string | null>(null)
const selectedStatus = ref<string | null>(null)
const selectedPriority = ref<string | null>(null)

// List of available options for the filters
const availableTags = ref<string[]>([])
const availableStatuses = ref(['Draft', 'Review', 'Actual', 'Outdated'])
const availablePriorities = ref(['High', 'Medium', 'Low'])

const launchModal = ref<QafModal | null>(null) // Adjust type based on ModalWrapper component
const launchInstance = ref<Launch | null>(null)

const vueRouter = useRouter()
const route = useRoute()
const projectId = computed<string | undefined>(() => route.params.projectId as string | undefined)

interface Props {
  testCases: TestCase[]
  editable: boolean
}

const props = defineProps<Props>()

interface TestCaseEmits {
  (e: 'open-test-case', testCase: TestCase): void,
  (e: 'delete-selected', selectedTestCases: number[]): void
}

const emit = defineEmits<TestCaseEmits>()

const openTestCase = (testCase: TestCase) => {
  emit('open-test-case', testCase)
}

const headers: ReadonlyHeaders = [
  { title: '', key: 'selected', align: 'start', width: 'auto', sortable: false },
  { title: 'ID', key: 'id', align: 'start', width: 'auto' },
  { title: 'Test Case', key: 'name', align: 'start' },
  { title: '', key: 'status', align: 'end', sortable: false }
]

onMounted(() => {
  fetchAvailableTags()
})

const fetchAvailableTags = () => {
  availableTags.value = [...new Set(props.testCases.flatMap(testCase => testCase.tags))]
}

const filteredTestCases = computed(() => {
  // First filter by values and search
  return props.testCases
    .filter((testCase: TestCase) => {
      // Tag filter
      if (selectedTag.value && !testCase.tags.includes(selectedTag.value)) {
        return false
      }
      // Status filter
      if (selectedStatus.value && testCase.status !== selectedStatus.value) {
        return false
      }
      // Priority filter
      if (selectedPriority.value && testCase.priority !== selectedPriority.value) {
        return false
      }

      // Perform the search filter
      const searchTerm = search.value.toLowerCase()
      const searchString = `${testCase.name}`.toLowerCase() // Combine relevant fields for search
      return searchString.includes(searchTerm)
    })
})

const selectedTestCases = ref<number[]>([])

const deleteSelected = () => {
  emit('delete-selected', selectedTestCases.value)
}

const applyFilters = () => {
  // If all filters is ok and running, can do emit
}

const saveLaunch = async (launch: Launch) => {
  try {
    if (!projectId.value) {
      console.error('Project ID is undefined')
      return
    }
    launch.projectId = Number(projectId.value)
    launch.testCases = selectedTestCases.value
    const data = await launchService.createLaunch(launch)

    launchModal.value?.close()
    // tcCreateError.value = null
    clearLaunchInstance()

    await vueRouter.push(`/projects/${projectId.value}/launches/${data.id}`)
  } catch (error: any) {
    // tcCreateError.value = error
    console.error('Failed to save test case:', error)
  }
}

const createLaunch = () => {
  clearLaunchInstance()
  launchInstance.value = {
    name: '',
    tags: [],
    projectId: 0,
    startTime: new Date(),
    testCases: []
  }
  launchModal.value?.open()
}

const clearLaunchInstance = () => {
  launchInstance.value = null
}

</script>

<style>
.v-chip.Review {
  color: #8427F5;
}

.v-chip.Actual {
  color: #00b140;
}

.v-chip.Outdated {
  color: #969696;
}
</style>
