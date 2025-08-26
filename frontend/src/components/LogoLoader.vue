<template>
  <div v-if="imageUrl" class="d-flex align-center">
    <img :src="imageUrl" :alt="altText" :width="width" :height="height"/>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps } from 'vue'
import { CacheService } from '@/api/cacheService'

interface Props {
  imageId: string | number
  altText?: string
  width?: string
  height?: string
}

const props = defineProps<Props>()

const API_URL = process.env.VUE_APP_API_URL

const imageUrl = ref<string | null>(null)
const cacheService = new CacheService()

onMounted(async () => {
  // Try to load from cache first
  imageUrl.value = await cacheService.retrieveImage(props.imageId)

  // If not in cache, download and store
  if (!imageUrl.value) {
    try {
      const response = await fetch(`${API_URL}/static-data/${props.imageId}`)

      if (!response.ok) {
        throw new Error('Image not found')
      }

      const blob = await response.blob()
      imageUrl.value = URL.createObjectURL(blob)

      // Store in cache for future use
      await cacheService.storeImage(props.imageId, blob)
    } catch (error) {
      console.error('Error loading image:', error)
    }
  }
})
</script>
