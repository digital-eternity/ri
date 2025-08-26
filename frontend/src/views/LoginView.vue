<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="4">
        <v-form ref="form" @submit.prevent="login">
          <v-card>
            <v-card-title class="headline">Login</v-card-title>
            <v-card-text>
                <v-text-field
                  v-model="username"
                  label="Username"
                  required
                  :rules="usernameRules"
                />
                <v-text-field
                  v-model="password"
                  label="Password"
                  type="password"
                  required
                  :rules="passwordRules"
                />
              <v-alert v-if="loginError" type="error">{{ loginError }}</v-alert>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="primary" type="submit" :disabled="loading">
                <v-progress-circular
                  v-if="loading"
                  indeterminate
                  color="white"
                  size="20"
                  class="mr-2"
                />
                Login
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-form>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '@/api/authService'
import { VForm } from 'vuetify/components'

const username = ref('')
const password = ref('')
const loginError = ref<string | null>(null)
const loading = ref(false)
const router = useRouter()
const form = ref<VForm | null>(null)

const usernameRules = [
  (value: string) => !!value || 'Username is required',
  (value: string) => (value && value.length >= 3) || 'Username must be at least 3 characters'
]

const passwordRules = [
  (value: string) => !!value || 'Password is required',
  (value: string) => (value && value.length >= 6) || 'Password must be at least 6 characters'
]

const login = async () => {
  if (form.value?.validate()) { // Use optional chaining
    loginError.value = null
    loading.value = true

    try {
      await authService.login(username.value, password.value)
      loading.value = false
      await router.push('/') // Redirect to main page
    } catch (error: any) { // Type the error
      loading.value = false
      console.error('Login failed:', error)
      loginError.value = error.message || 'Invalid username or password'
    }
  }
}
</script>
