<template>
  <v-container>
    <v-card>
      <v-card-title>{{ user?.username }}</v-card-title>
      <LogoLoader :image-id="'profile-icon.png'" alt-text="" :width="'128'" :height="'128'"/>
      <v-card-text class="text-left">Role: {{ user?.globalRole }}</v-card-text>

      <v-card-actions>
        <v-expansion-panels>
          <v-expansion-panel
            title="Change password"
          >
            <v-expansion-panel-text>
              <v-row no-gutters>
                <v-text-field
                  v-model="oldPassword"
                  density="comfortable"
                  placeholder="Old password"
                  style="max-width: 300px;"
                  variant="solo"
                  class="mr-5"
                />
                <v-text-field
                  v-model="newPassword"
                  density="comfortable"
                  placeholder="New password"
                  style="max-width: 300px;"
                  variant="solo"
                  class="mr-5"
                />
                <v-btn
                  icon="mdi-floppy"
                  @click.stop="changePassword()"
                />
              </v-row>
            </v-expansion-panel-text>
          </v-expansion-panel>
          <v-expansion-panel
            title="Personal Access Token"
          >
            <v-expansion-panel-text>
              <div v-if="token">
                <v-text-field v-model="token" readonly>
                  <template v-slot:append>
                    <v-btn icon="mdi-content-copy" @click="copyToClipboard(token)"></v-btn>
                  </template>
                </v-text-field>
                <v-btn @click="deleteToken">Delete</v-btn>
              </div>
              <div v-else>
                <v-btn @click="generateToken">Generate Token</v-btn>
              </div>
            </v-expansion-panel-text>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { userService } from '@/api/userService'
import { getUserId } from '@/composables/userInfo'
import { User } from '@/types/tmsTypes'
import LogoLoader from '@/components/LogoLoader.vue'

const user = ref<User | null>(null)
const userId = ref <string>('')
const oldPassword = ref <string>('')
const newPassword = ref <string>('')

onMounted(async () => {
  userId.value = getUserId().userId.value as string
  await fetchUserProfile()
  await getToken()
})

const fetchUserProfile = async () => {
  try {
    user.value = await userService.getUser(userId.value)
  } catch (error) {
    console.error('Failed to fetch current user:', error)
  }
}

const changePassword = async () => {
  await userService.changePassword(userId.value, oldPassword.value, newPassword.value)
}

const token = ref<string>('')

const getToken = async () => {
  try {
    token.value = await userService.getToken()
  } catch (error) {
    console.error('Failed to get token:', error)
  }
}

const generateToken = async () => {
  try {
    const newToken = await userService.generateToken()
    token.value = newToken
  } catch (error) {
    console.error('Failed to generate token:', error)
  }
}

const deleteToken = async () => {
  try {
    await userService.deleteToken()
    await getToken()
  } catch (error) {
    console.error('Failed to delete token:', error)
  }
}

const copyToClipboard = (text: string) => {
  navigator.clipboard.writeText(text).then(() => {
    // Optional: Show a snackbar or toast notification
  }).catch(err => {
    console.error('Failed to copy text: ', err)
  })
}

</script>
