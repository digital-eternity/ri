<template>
  <v-dialog v-model="dialog" width="600">
    <template v-slot:activator="{ props }">
      <slot name="activator" v-bind="props"></slot>
    </template>

    <v-card>
      <v-card-title class="headline">{{ title }}</v-card-title>

      <v-card-text>
        <component :is="component" v-bind="componentProps" @save="emitSave" @cancel="emitCancel"></component>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, PropType } from 'vue'

interface ModalEmits {
  (e: 'close'): void
  (e: 'component-save', payload: any): void
  (e: 'component-cancel'): void
}

defineProps({
  title: {
    type: String,
    required: true
  },
  component: {
    type: Object as PropType<any>,
    required: true
  },
  componentProps: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits<ModalEmits>()

const dialog = ref(false)

const open = () => {
  dialog.value = true
}

const close = () => {
  dialog.value = false
  emit('close') // Emit the close event
}

const emitSave = (payload: any) => {
  dialog.value = false
  emit('component-save', payload)
}

const emitCancel = () => {
  dialog.value = false
  emit('component-cancel')
}

defineExpose({
  open,
  close
})
</script>
