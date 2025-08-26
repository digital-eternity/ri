<template>
  <v-container>
    <v-progress-linear v-if="loading" indeterminate></v-progress-linear>
    <v-alert v-else-if="error" type="error">Error loading users: {{ error }}</v-alert>
    <div v-else-if="users.length === 0">No users available.</div>

    <v-row class="justify-end mb-10 mt-10">
      <ModalWrapper
        ref="userModal"
        title="Add User"
        :component="UserForm"
        @component-save="saveUser"
      >
        <template v-slot:activator="{ props }">
          <v-btn color="primary" v-bind="props" @click="openCreate">Add user</v-btn>
        </template>
      </ModalWrapper>
    </v-row>

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
        :items="users"
        :headers="headers"
        :search="search"
      >
        <template v-slot:[`item.id`]="">
          <LogoLoader :image-id="'profile-icon.png'" alt-text="User Profile Picture" :width="'64'" :height="'64'"/>
        </template>

        <template v-slot:[`item.username`]="{ item }">
          <div class="d-flex justify-start">{{item.username}}</div>
        </template>

        <template v-slot:[`item.globalRole`]="{ item }">
          <div class="d-flex justify-start">{{item.globalRole}}</div>
        </template>

        <template v-slot:[`item.actions`]="{ item }">
          <div class="d-flex ga-2 justify-end">
            <v-icon color="medium-emphasis" icon="mdi-pencil" size="small" @click="editUser(item)"></v-icon>
            <v-icon color="medium-emphasis" icon="mdi-delete" size="small" @click="openUserDeletionConfirmation(item)"></v-icon>
          </div>
        </template>
      </v-data-table>
    </v-row>

    <ConfirmationModal
      ref="confirmationModal"
      title="Confirm Deletion"
      message="Are you sure you want to delete the user?"
      @confirm="deleteConfirmed"
      @cancel="closeConfirmationModal"
    />

  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { userService } from '@/api/userService'

// components
import ModalWrapper from '@/components/ModalWrapper.vue'
import ConfirmationModal from '@/components/ConfirmationModal.vue'
import UserForm from '@/components/UserForm.vue'
import LogoLoader from '@/components/LogoLoader.vue'
import type { VDataTable } from 'vuetify/components'

// types
import { User } from '@/types/tmsTypes'
import { QafModal } from '@/types/uiTypes'
type ReadonlyHeaders = VDataTable['$props']['headers']

const headers : ReadonlyHeaders = [
  { title: '', key: 'id', align: 'start', width: '80px' },
  { title: 'Username', key: 'username', align: 'start' },
  { title: 'Role', key: 'globalRole', align: 'start' },
  { title: 'Actions', key: 'actions', align: 'end', sortable: false }
]

const loading = ref(true)
const error = ref<string | null>(null)
const users = ref<User[]>([])
const userInstance = ref<User | null>(null)
const userModal = ref<QafModal | null>(null) //  Type may need adjustment based on ModalWrapper
const confirmationModal = ref<QafModal | null>(null) // Type may need adjustment based on ConfirmationModal
const search = ref('')

onMounted(async () => {
  await fetchUsers()
})

const fetchUsers = async () => {
  try {
    loading.value = true
    error.value = null
    users.value = await userService.getUsers()
  } catch (err: any) {
    console.error('Failed to fetch users:', err)
    error.value = err.message || 'Unknown error'
  } finally {
    loading.value = false
  }
}

const saveUser = async (user: User) => {
  try {
    if (user.id) {
      await userService.updateUser(user.id, user)
    } else {
      await userService.createUser(user)
    }
    await fetchUsers()
    userModal.value?.close()
    clearUserInstance()
  } catch (error) {
    console.error('Failed to save user:', error)
  }
}

const openUserDeletionConfirmation = (user: User) => {
  userInstance.value = user
  confirmationModal.value?.open()
}

const closeConfirmationModal = () => {
  clearUserInstance()
  confirmationModal.value?.close()
}

const deleteConfirmed = async () => {
  if (!userInstance.value) {
    console.error('User ID is undefined')
    return
  }

  await deleteUser(userInstance.value.id as number)
  clearUserInstance()
}

const deleteUser = async (id: number) => {
  try {
    await userService.deleteUser(id)
    await fetchUsers()
  } catch (error) {
    console.error('Failed to delete user:', error)
  }
}

const editUser = (user: User) => {
  userModal.value?.open()
  userInstance.value = { ...user }
}

const openCreate = () => {
  clearUserInstance()
  userModal.value?.open()
}

const clearUserInstance = () => {
  userInstance.value = null
}

</script>
