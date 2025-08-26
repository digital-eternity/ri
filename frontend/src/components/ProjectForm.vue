<template>
  <v-form @submit.prevent="save">
    <v-text-field v-model="projectInstance.name" label="Project Name" required></v-text-field>
    <v-textarea v-model="projectInstance.description" label="Description"></v-textarea>

    <v-card-actions>
      <v-spacer/>
      <v-btn type="submit">Save</v-btn>
      <v-btn @click="cancel">Cancel</v-btn>
    </v-card-actions>
  </v-form>
</template>

<script setup lang="ts">
import { defineProps, ref, watch, defineEmits } from 'vue'

import { Project } from '@/types/tmsTypes'

interface Props {
  project: Project
}

interface ModalEmits {
  (e: 'save', project: Project): void
  (e: 'cancel'): void
}

const props = defineProps<Props>()
const emit = defineEmits<ModalEmits>()

const projectInstance = ref<Project>({
  id: 0,
  name: '',
  description: ''
})

watch(
  () => props.project,
  (newProject: Project) => {
    projectInstance.value = { ...newProject }
  },
  { immediate: true }
)

const save = () => {
  emit('save', projectInstance.value)
}

const cancel = () => {
  emit('cancel')
}
</script>
