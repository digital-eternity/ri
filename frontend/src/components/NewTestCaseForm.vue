<template>
  <div>
    <h2>Create/Edit Test Case</h2>
    <v-form @submit.prevent="save">
      <v-text-field v-model="form.name" label="Name" required></v-text-field>
      <v-textarea v-model="form.description" label="Description" required></v-textarea>
      <v-select
        v-model="form.priority"
        :items="priorityOptions"
        label="Priority"
        required
      />
      <v-select
        v-model="form.status"
        :items="statusOptions"
        label="Status"
        required
      />
      <v-combobox
        v-model="form.tags"
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
        <v-btn type="submit">Save</v-btn>
        <v-btn @click="cancel">Cancel</v-btn>
      </v-card-actions>
    </v-form>
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref, watch, defineEmits } from 'vue'

import { TestCase } from '@/types/tmsTypes'
import type { TestCasePriority, TestCaseStatus } from '@/types/tmsTypes'

const items = ['API', 'UI']
const isBuiltIn = (item: string): boolean => {
  return items.includes(item)
}

interface Props {
  testCase: TestCase
}

const props = defineProps<Props>()

const emit = defineEmits<{(e: 'save', testCase: TestCase): void, (e: 'cancel'): void}>()

const form = ref<TestCase>({
  name: '',
  priority: 'Medium',
  status: 'Draft',
  tags: ['test'],
  projectId: 0,
  description: ''
})

const priorityOptions = ref<TestCasePriority[]>(['High', 'Medium', 'Low'])
const statusOptions = ref<TestCaseStatus[]>(['Draft', 'Review', 'Actual', 'Outdated'])

watch(
  () => props.testCase,
  (newTestCase: TestCase) => {
    form.value = { ...newTestCase }
    form.value.status = 'Draft'
    form.value.priority = 'Medium'
    form.value.description = ''
  },
  { immediate: true }
)

const save = () => {
  emit('save', form.value)
}

const cancel = () => {
  emit('cancel')
}
</script>
