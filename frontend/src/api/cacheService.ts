export class CacheService {
  private cacheName: string

  constructor () {
    this.cacheName = 'image-cache-v1'
  }

  async initializeCache (): Promise<Cache | null> {
    if (!('caches' in window)) {
      console.warn('Cache API not supported')
      return null
    }

    try {
      const cache = await caches.open(this.cacheName)
      return cache
    } catch (error) {
      console.error('Error initializing cache:', error)
      return null
    }
  }

  async storeImage (imageId: string | number, imageData: any): Promise<boolean> {
    const cache = await this.initializeCache()
    if (!cache) return false

    try {
      await cache.put(`/static-data/${imageId}`, new Response(imageData))
      return true
    } catch (error) {
      console.error('Error storing image in cache:', error)
      return false
    }
  }

  async retrieveImage (imageId: string | number): Promise<string | null> {
    const cache = await this.initializeCache()
    if (!cache) return null

    try {
      const response = await cache.match(`/static-data/${imageId}`)

      if (!response) return null

      const blob = await response.blob()
      return URL.createObjectURL(blob)
    } catch (error) {
      console.error('Error retrieving image from cache:', error)
      return null
    }
  }

  async clearCache (): Promise<void> {
    const cache = await this.initializeCache()
    if (!cache) return

    try {
      await caches.delete(this.cacheName)
      this.cacheName = `image-cache-v${parseInt(this.cacheName.split('-v')[1]) + 1}`
    } catch (error) {
      console.error('Error clearing cache:', error)
    }
  }
}
