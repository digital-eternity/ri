<template>
  <v-navigation-drawer
    v-model="$props.show"
    absolute
    location="right"
    temporary
    width="800"
  >
    <v-row class="mx-5">
      <v-btn icon @click="handleClose">
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-row>
    <v-card class="mt-5">
      <v-card-title class="d-flex justify-space-between align-center">
        <span>Generate Tests with AI</span>
      </v-card-title>
      <v-card-text>
        <v-expansion-panels class="mb-5">
          <v-expansion-panel
            title="System prompt"
          >
            <v-expansion-panel-text>
              <v-textarea
                label="System prompt"
                v-model="systemPrompt"
                auto-grow
                rows="1"
              />
            </v-expansion-panel-text>
          </v-expansion-panel>
        </v-expansion-panels>

        <v-btn color="primary" class="ml-5 mt-5 mb-5" @click="clear">Clear chat</v-btn>
        <v-list density="compact" >

          <v-list-item v-for="(chatMessage, index) in chatHistory" :key="index">
            <v-row>
              <v-col style="max-width: 100px" align="left">
                {{ chatMessage.role }}
              </v-col>
              <v-col align="left">
              <vue-markdown
              :source="chatHistory[index].content"
              append-inner-icon="mdi-content-copy"
              @click:append-inner="copyToClipboard(aiOutput)"
              />
              </v-col>
            </v-row>
          </v-list-item>

          <v-list-item>
            <v-textarea
              label="Prompt"
              v-model="prompt"
              auto-grow
              rows="1"
              append-inner-icon="mdi-send-variant-outline"
              @click:append-inner="generateTests"
            />
          </v-list-item>
        </v-list>

        <v-progress-circular
          v-if="loading"
          indeterminate
          color="white"
          size="40"
          class="mr-2"
        />

      </v-card-text>
    </v-card>
  </v-navigation-drawer>
</template>

<script lang="ts">

import VueMarkdown from 'vue-markdown-render'
import { defineProps, defineEmits, defineComponent, ref } from 'vue'
import { OpenAICompatibleRequest, AiChatMessage } from '@/types/aiTypes'

export default defineComponent({
  name: 'VueMarkdownComponent',
  components: {
    VueMarkdown
  },
  setup (props, ctx) {
    const src = ref('# header')

    return {
      src
    }
  }
})
</script>

<script setup lang="ts">

const props = defineProps({
  show: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['update:show'])

const loading = ref(false)
const prompt = ref('')
const systemPrompt = ref('/no_think act as a tester')
const aiOutput = ref('')
const chatHistory = ref<AiChatMessage[]>([])

const model = 'qwen3:30b-a3b-fp8'

const handleClose = () => {
  emit('update:show', false)
}

const clear = async () => {
  aiOutput.value = ''
  chatHistory.value.clear()
}

const generateTests = async () => {
  loading.value = true
  try {
    queryQwen3Stream(prompt.value, (content) => {
      aiOutput.value = content
      loading.value = false
    }).then(finalContent => {
      chatHistory.value.push({
        role: 'assistant',
        content: finalContent.replace(/^<think>\n\n<\/think>/, '')
      })
      prompt.value = ''
    })
  } catch (error) {
    loading.value = false
    aiOutput.value = 'Error occurred during API call'
    console.error('API Error:', error)
  }
}

async function queryQwen3Stream (prompt: string, onContentUpdate: (content: string) => void) {
  try {
    chatHistory.value.push({
      role: 'user',
      content: prompt
    })

    const requestData: OpenAICompatibleRequest = {
      messages: [
        {
          role: 'system',
          content: systemPrompt.value
        }
      ],
      model: model,
      max_tokens: 32000,
      stream: true
    }

    chatHistory.value.forEach((message) => {
      requestData.messages.push({
        role: message.role,
        content: message.content
      })
    })

    const response = await fetch(process.env.VUE_APP_API_URL + '/AI/chat', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestData)
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    if (!response.body) {
      throw new Error('ReadableStream not supported in this browser')
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let accumulatedContent = ''

    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      const chunk = decoder.decode(value)
      const lines = chunk.split('\n').filter(line => line.trim() !== '')

      for (const line of lines) {
        if (line.startsWith('data: ') && !line.includes('[DONE]')) {
          const data = line.replace('data: ', '')
          try {
            const parsed = JSON.parse(data)
            if (parsed.choices && parsed.choices[0]?.delta?.content) {
              accumulatedContent += parsed.choices[0].delta.content
              onContentUpdate(accumulatedContent) // Callback with updated content
            }
          } catch (e) {
            console.error('Error parsing JSON:', e)
          }
        }
      }
    }

    return accumulatedContent
  } catch (error) {
    console.error('Error querying LLM:', error)
    throw error
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
