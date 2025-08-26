import { Ref, onMounted, onBeforeUnmount } from 'vue'

export function useClickOutside<T extends HTMLElement> (elementRef: Ref<T | null>, callback: () => void) {
  const handleClick = (event: MouseEvent) => {
    if (elementRef.value && !elementRef.value.contains(event.target as Node)) {
      callback()
    }
  }

  onMounted(() => {
    window.addEventListener('click', handleClick)
  })

  onBeforeUnmount(() => {
    window.removeEventListener('click', handleClick)
  })
}
