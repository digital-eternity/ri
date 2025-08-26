<template>
  <v-container>
    <v-row>
      <v-col>
        <v-card>
          <v-card-title>Import Test Cases</v-card-title>
          <v-card-text>
            <v-file-input
              v-model="importFile"
              label="Select CSV or JSON File"
              accept=".csv, .json"
              @change="handleFileSelected"
            />

            <v-btn color="primary" @click="importTestCases" :disabled="!importFile">Import</v-btn>

            <v-alert v-if="importErrors.length" type="error">
              <h3>Import Errors:</h3>
              <ul>
                <li v-for="(error, index) in importErrors" :key="index">{{ error }}</li>
              </ul>
            </v-alert>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col>
        <v-card>
          <v-card-title>Export Test Cases</v-card-title>
          <v-card-text>
            <v-select v-model="exportFormat" :items="['csv', 'json']" label="Select Export Format"/>
            <v-btn color="primary" @click="exportTestCases" :disabled="!exportFormat">Export</v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <h1 class="mt-10">Assign Users to Project</h1>
    <v-row>
      <v-col cols="12" md="6">
          <h2>Assigned Users</h2>

          <v-text-field
            v-model="assignedSearch"
            label="Search"
            prepend-inner-icon="mdi-magnify"
            variant="outlined"
            hide-details
            single-line
            density="compact"
          />

          <v-data-table
            :items="assignedUsers"
            :search="assignedSearch"
            :headers="headers"
            density="compact"
          >
            <template v-slot:[`item.id`]="">
              <LogoLoader :image-id="'profile-icon.png'" alt-text="User Profile Picture" :width="'32'" :height="'32'"/>
            </template>
            <template v-slot:[`item.username`]="{ item }">
              <div class="d-flex justify-start">{{item.username}}</div>
            </template>
            <template v-slot:[`item.projectRole`]="{ item }">
              <div class="d-flex justify-start">{{item.projectRole}}</div>
            </template>
            <template v-slot:[`item.actions`]="{ item }">
              <div class="d-flex ga-2 justify-end">
                <v-icon color="medium-emphasis" icon="mdi-delete" size="small" @click="removeUser(item.id)"></v-icon>
              </div>
            </template>
          </v-data-table>
      </v-col>
      <v-spacer/>
      <v-col cols="12" md="4">
        <h2>Available Users</h2>
        <v-text-field
          v-model="availableSearch"
          label="Search"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          hide-details
          single-line
          density="compact"
        />

        <v-data-table
          :items="availableUsers"
          :search="availableSearch"
          :headers="availableHeaders"
          density="compact"
        >
          <template v-slot:[`item.id`]="">
            <LogoLoader :image-id="'profile-icon.png'" alt-text="User Profile Picture" :width="'32'" :height="'32'"/>
          </template>
          <template v-slot:[`item.username`]="{ item }">
            <div class="d-flex justify-start">{{item.username}}</div>
          </template>
          <template v-slot:[`item.actions`]="{ item }">
            <div class="d-flex ga-2 justify-end">
                <div>Add as: </div>
                <v-btn density="compact" @click="addUser(item.id, 'Member')">Member</v-btn>
                <v-btn density="compact" @click="addUser(item.id, 'Lead')">Lead</v-btn>
            </div>
          </template>
        </v-data-table>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { projectService } from '@/api/projectService'
import { userService } from '@/api/userService'
import { testCaseService } from '@/api/testCaseService'
import LogoLoader from '@/components/LogoLoader.vue'

import type User from '@/types/user'
import type ProjectUser from '@/types/projectUser'
import type { VDataTable } from 'vuetify/components'

const importFile = ref<File | null>(null)
const exportFormat = ref<string | null>(null)
const importErrors = ref<string[]>([])

type ReadonlyHeaders = VDataTable['$props']['headers']

const route = useRoute()
const projectId = computed<string | undefined>(() => route.params.projectId as string | undefined)
const assignedUsers = ref<User[]>([])
const availableUsers = ref<ProjectUser[]>([])
const assignedSearch = ref('')
const availableSearch = ref('')
const loading = ref(true)

onMounted(async () => {
  await Promise.all([fetchAssignedUsers(), fetchAvailableUsers()]) // Fetch in parallel
  loading.value = false
})

const fetchAvailableUsers = async () => {
  try {
    const users = await userService.getUsers()
    availableUsers.value = users
  } catch (error) {
    console.error('Failed to fetch available users:', error)
  }
}

const fetchAssignedUsers = async () => {
  try {
    if (!projectId.value) {
      console.error('Project ID is undefined')
      return
    }
    const users = await projectService.getUsers(projectId.value)
    assignedUsers.value = users
  } catch (error) {
    console.error('Failed to fetch assigned users:', error)
  }
}

const addUser = async (userId: number, role: string) => {
  try {
    if (!projectId.value) {
      console.error('Project ID is undefined')
      return
    }
    const user = { id: userId, projectRole: role }
    await projectService.addUser(user, projectId.value)
    await fetchAvailableUsers()
    await fetchAssignedUsers()
  } catch (error) {
    console.error('Failed to assign user:', error)
  }
}

const removeUser = async (userId: number) => {
  try {
    if (!projectId.value) {
      console.error('Project ID is undefined')
      return
    }
    await projectService.removeUser(userId, projectId.value)
    await fetchAvailableUsers()
    await fetchAssignedUsers()
  } catch (error) {
    console.error('Failed to remove user:', error)
  }
}

const headers: ReadonlyHeaders = [
  { title: '', key: 'id', align: 'start', width: '80px' },
  { title: 'Username', key: 'username', align: 'start' },
  { title: 'Role', key: 'projectRole', align: 'start' },
  { title: 'Actions', key: 'actions', align: 'end', sortable: false }
]

const availableHeaders: ReadonlyHeaders = [
  { title: '', key: 'id', align: 'start', width: '80px' },
  { title: 'Username', key: 'username', align: 'start' },
  { title: 'Actions', key: 'actions', align: 'end', sortable: false }
]

const handleFileSelected = (file: Event | null) => {
  importFile.value = file?.target.files[0]
  console.log('File:', importFile.value)
}

const importTestCases = async () => {
  if (!importFile.value) return

  importErrors.value = [] // Clear previous errors

  try {
    const result = await testCaseService.importTestCases(projectId.value, importFile.value)
    // Validate and Add errors to page (as before says)
  } catch (error: any) {
    importErrors.value = [error.message || 'An unexpected error occurred']
  }
}

const exportTestCases = async () => {
  if (!exportFormat.value) return

  try {
    const blob = await testCaseService.exportTestCases(projectId.value, exportFormat.value)

    // Create a download link and trigger the download
    const url = window.URL.createObjectURL(new Blob([blob]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', `testcases.${exportFormat.value}`)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  } catch (error: any) {
    importErrors.value = [error.message || 'An unexpected error occurred']
    console.error('Failed to export test cases:', error)
  }
}

</script>
