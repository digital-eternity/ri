<template>
  <v-form @submit.prevent="save">
    <v-text-field v-model="userInstance.username" label="Username" required></v-text-field>
    <v-text-field v-model="userInstance.password" label="Password" required></v-text-field>

    <v-card-actions>
      <v-spacer/>
      <v-btn type="submit">Save</v-btn>
      <v-btn @click="cancel">Cancel</v-btn>
    </v-card-actions>
  </v-form>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface NewUser {
  username: string
  password: string
}

interface ModalEmits {
  (e: 'save', user: NewUser): void
  (e: 'cancel'): void
}

const emit = defineEmits<ModalEmits>()

const userInstance = ref<NewUser>({
  username: '',
  password: ''
})

const save = () => {
  emit('save', userInstance.value)
}

const cancel = () => {
  emit('cancel')
}
</script>
