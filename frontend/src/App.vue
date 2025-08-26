<template>
  <v-app>
    <v-app-bar app style="background: linear-gradient(to right, #135771, #5DABA6);">
      <div class="mx-10">
        <v-toolbar-title><a href="/"> Rhytms of Universe </a></v-toolbar-title>
      </div>

      <v-toolbar-items v-if="isLoggedIn">
        <v-btn to="/">Builder</v-btn>
      </v-toolbar-items>
      <v-spacer></v-spacer>
        <UserMenu v-if="isLoggedIn" class="mr-5"/>
    </v-app-bar>

    <v-main>
      <router-view style="max-width: 90%;"></router-view>
    </v-main>
  </v-app>
</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router'
import { computed, ref, watch } from 'vue'
import { isLoggedIn } from '@/api/authService'
import { projectService } from '@/api/projectService'

import UserMenu from '@/components/UserMenu.vue'

const route = useRoute()
const projectId = computed<string>(() => route.params.projectId as string)

const canEdit = ref(false)

watch(() => projectId.value, async (newProjectId) => {
  if (newProjectId) {
    try {
      canEdit.value = await projectService.canEdit(newProjectId)
    } catch (error) {
      console.error('Error refreshing edit permission:', error)
      canEdit.value = false
    }
  }
})

</script>

<style>
#app {
  font-family: Nunito, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}

.v-app-bar {
    .v-toolbar-title {
        color: white;
    }

    a {
      color: white;
      text-decoration: none;
    }

    .v-toolbar__content {
        overflow: visible;
    }
}
</style>
