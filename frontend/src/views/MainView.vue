<template>
  <v-container>
    <v-row class="mb-10 mt-10">
      <v-col>
        <RhythmBuilder/>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'

// components
import RhythmBuilder from '@/components/RhythmBuilder.vue'

onMounted(async () => {
  console.info('MainView mounted')
})

</script>
