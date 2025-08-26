<template>
  <div>
    <h2>Create launch</h2>
    <v-form @submit.prevent="save">
      <v-text-field v-model="launchInstance.name" label="Name" required></v-text-field>
      <v-combobox
        v-model="launchInstance.tags"
        :items="items"
        label="Tags"
        variant="solo"
        chips
        clearable
        closable-chips
        multiple
      >
        <template v-slot:chip="{ props, item }">
          <v-chip v-bind="props">
            <strong>{{ item.raw }}</strong>&nbsp;
            <span v-if="isBuiltIn(item.raw)">(built-in)</span>
            <span v-else>(custom)</span>
          </v-chip>
        </template>
      </v-combobox>

      <v-card-actions>
        <v-spacer/>
        <v-btn type="submit">Create</v-btn>
        <v-btn @click="cancel">Cancel</v-btn>
      </v-card-actions>
    </v-form>
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref, watch, defineEmits } from 'vue'
import { Launch } from '@/types/tmsTypes'

const items = ['manual']
const isBuiltIn = (item: string): boolean => {
  return items.includes(item)
}

interface Props {
  launch: Launch
}

interface ModalEmits {
  (e: 'save', launch: Launch): void
  (e: 'cancel'): void
}

const props = defineProps<Props>()

const emit = defineEmits<ModalEmits>()

const launchInstance = ref<Launch>({
  name: '',
  tags: ['test'],
  // projectId: 0,
  startTime: new Date(),
  endTime: new Date(),
  testCases: []
})

watch(
  () => props.launch,
  (newLaunch: Launch) => {
    launchInstance.value = { ...newLaunch }
    launchInstance.value.tags = ['manual']
  },
  { immediate: true }
)

const save = () => {
  emit('save', launchInstance.value)
}

const cancel = () => {
  emit('cancel')
}
</script>
