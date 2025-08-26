<template>
  <v-card class="mt-5">
    <v-card-title class="text-left">Test result</v-card-title>
    <v-card-text class="text-left">
      <v-row>
        <v-col style="max-width: 70%;">
          <div class="mt-5">
            <p><strong>Comment:</strong></p>
            <v-textarea
              v-model="testCaseDetails.comment"
              placeholder="Comment"
              variant="plain"
              rows="1"
              auto-grow
            />
            </div>
        </v-col>
          <v-col style="max-width: 30%;">
            <v-select v-model="testCaseDetails.status" :items="statusOptions" density="compact" variant="plain"/>
          </v-col>
        </v-row>

          <v-btn @click="updateTestCase">Update Test Result</v-btn>
    </v-card-text>
    </v-card>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps } from 'vue'
import { launchService } from '@/api/launchService'
import { ExecutingTest, TestStatus } from '@/types/tmsTypes'

interface Props {
  testCaseId: string
}

const props = defineProps<Props>()

const statusOptions = ref<TestStatus[]>(['Passed', 'Failed', 'InProgress'])

const testCaseDetails = ref<ExecutingTest>({
  id: '',
  comment: '',
  testCaseId: 0,
  status: 'Passed'
})

onMounted(async () => {
  await fetchTestCaseDetails()
})

const fetchTestCaseDetails = async () => {
  try {
    const fetchedTestCaseDetails = await launchService.getLaunchTest(props.testCaseId)
    testCaseDetails.value = fetchedTestCaseDetails
  } catch (error) {
    console.error('Failed to fetch test cases:', error)
  }
}

const updateTestCase = async () => {
  try {
    await launchService.updateLaunchTest(props.testCaseId, testCaseDetails.value)
  } catch (error: any) {
    console.error('Failed to update test case:', error)
  }
}
</script>
