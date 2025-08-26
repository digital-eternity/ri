<template>
  <div class="user-menu-container" ref="element">
    <LogoLoader :image-id="'profile-icon.png'" alt-text="User Profile Picture" :width="'35'" :height="'35'" class="user-avatar" @click="toggleMenu"/>

    <transition name="fade">
      <v-list v-if="isMenuOpen" class="user-menu">
        <v-list-item @click="profile">Profile</v-list-item>
        <v-list-item @click="settings">Settings</v-list-item>
        <v-list-item @click="switchTheme">Switch theme</v-list-item>
        <v-list-item @click="logout">Logout</v-list-item>
      </v-list>
    </transition>
  </div>
</template>

<script setup lang="ts">
import LogoLoader from '@/components/LogoLoader.vue'
import { useTheme } from 'vuetify'
import { ref } from 'vue'
import { authService } from '../api/authService'
import { useClickOutside } from '../composables/useClickOutside'
import router from '@/router'

const theme = useTheme()
const isMenuOpen = ref(false)
const element = ref<HTMLElement | null>(null)

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}

const profile = () => {
  isMenuOpen.value = false
  router.push('/profile')
}

const settings = () => {
  console.log('Navigating to settings...')
  isMenuOpen.value = false
}

const switchTheme = () => {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
  isMenuOpen.value = false
}

const logout = () => {
  authService.logout()
  isMenuOpen.value = false
}

const handleClickOutside = () => {
  isMenuOpen.value = false
}

useClickOutside(element, handleClickOutside)

</script>

<style scoped>
.user-menu-container {
  position: relative;
  display: inline-block;
}

.user-avatar {
  cursor: pointer;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid #ccc;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover; /*  Ensures the image covers the entire area, cropping if necessary */
}

.user-menu {
  position: absolute;
  top: 100%; /* Position below the avatar */
  right: 0; /* Align to the right */
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  z-index: 9999; /* Ensure it's above other elements */
  min-width: 150px;  /* Minimum width of the menu */
}

.user-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.user-menu li {
  padding: 10px 15px;
  cursor: pointer;
}

/* Transition Styles */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Optional:  Styles to position the container in your header/navbar */
/* For example: */
/* .user-menu-container {
  margin-left: auto;  // Push to the right if in a header with flexbox
} */
</style>
