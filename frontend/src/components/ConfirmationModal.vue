<template>
  <v-dialog v-model="dialogVisible" width="400">
    <v-card>
      <v-card-title class="headline">{{ title }}</v-card-title>
      <v-card-text>{{ message }}</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue-grey" text @click="cancel">Cancel</v-btn>
        <v-btn color="red darken-1" text @click="confirm">Confirm</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface ConfirmationModalEmits {
  (e: 'confirm'): void
  (e: 'cancel'): void
}

interface Props {
  title: string,
  message: string
}

defineProps<Props>()

const emit = defineEmits<ConfirmationModalEmits>()

const dialogVisible = ref(false)

const open = () => {
  dialogVisible.value = true
}

const close = () => {
  dialogVisible.value = false
}

const confirm = () => {
  emit('confirm')
  close()
}

const cancel = () => {
  emit('cancel')
  close()
}

defineExpose({
  open,
  close
})
</script>
