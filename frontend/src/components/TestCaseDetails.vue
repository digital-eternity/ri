<template>
  <v-card>
    <v-card-title class="text-left">{{ testCase.id }}: {{ testCase.name }}</v-card-title>
    <v-card-text class="text-left">
      <v-row>
        <v-col style="max-width: 75%;">
          <div class="mt-5">
            <p><strong>Description:</strong></p>
            <v-textarea
              v-model="testCaseDetails.description"
              placeholder="Description"
              variant="plain"
              rows="1"
              auto-grow
              :readonly="!(editable)"
            />
            </div>

          <div class="mt-5">
            <p><strong>Preconditions:</strong></p>
            <v-textarea
              v-model="testCaseDetails.preconditions"
              placeholder="Preconditions"
              variant="plain"
              rows="1"
              auto-grow
              :readonly="!(editable)"
            />
          </div>

          <div class="mt-5">
            <p><strong>Steps</strong></p>

            <v-list density="compact" >
              <v-list-item>
                <v-row>
                  <v-col style="max-width: 5%;">#</v-col>
                  <v-col>Action</v-col>
                </v-row>
              </v-list-item>

              <v-list-item v-for="(step, index) in testCaseDetails.testSteps" :key="index">
                <v-row>
                  <v-col class="d-flex align-center" style="max-width: 5%;">{{ index + 1 }}</v-col>
                  <v-col style="max-width: 95%;">
                    <v-text-field
                      :readonly="!editable"
                      v-model="testCaseDetails.testSteps[index]"
                      variant="plain"
                      :append-inner-icon="editable ? 'mdi-close' : undefined"
                      @click:append-inner="deleteStep(index)"
                      density="compact"
                      hide-details
                    />
                  </v-col>
                </v-row>
              </v-list-item>
            </v-list>

            <v-text-field
              v-if="editable"
              clearable
              label="Step"
              append-inner-icon="mdi-check"
              single-line
              v-model="newStep"
              @click:append-inner="appendStep"
            />
          </div>

          <div class="mt-5">
            <p><strong>Expected result:</strong></p>
            <v-textarea
              v-model="testCaseDetails.expectedResult"
              placeholder="Expected result"
              variant="plain"
              rows="1"
              auto-grow
              :readonly="!(editable)"
            />
          </div>

        </v-col>
          <v-col style="max-width: 25%;">
            <v-btn v-if="editable" class="mb-10" @click="updateTestCase">Update Test Case</v-btn>
            <p><strong>Status:</strong></p>
            <v-select v-model="testCase.status" :items="statusOptions" :readonly="!(editable)" density="compact" variant="plain"/>
            <p><strong>Priority:</strong></p>
            <v-select v-model="testCase.priority" :items="priorityOptions" :readonly="!(editable)" density="compact" variant="plain"/>
            <p><strong>Requirement:</strong></p>
            <v-text-field v-model="testCase.requirementId" :readonly="!(editable)" density="compact" variant="plain"/>
            <p><strong>Tags:</strong></p>
            <v-combobox
              v-model="testCase.tags"
              :items="items"
              label="Tags"
              chips
              :clearable="editable"
              :closable-chips="editable"
              multiple
              variant="plain"
              :readonly="!(editable)"
            />
          </v-col>
        </v-row>

    </v-card-text>
    </v-card>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps } from 'vue'
import { testCaseService } from '@/api/testCaseService'
import { TestCase, TestCaseDetails, TestCasePriority, TestCaseStatus } from '@/types/tmsTypes'

interface Props {
  testCaseId: number
  editable: boolean
}

const props = defineProps<Props>()

const newStep = ref('')
const testCase = ref<TestCase>({
  id: 0,
  name: '',
  priority: 'Medium',
  status: 'Draft',
  tags: ['test']
})

const testCaseDetails = ref<TestCaseDetails>({
  description: '',
  preconditions: '',
  testSteps: [],
  expectedResult: ''
})

const priorityOptions = ref<TestCasePriority[]>(['High', 'Medium', 'Low'])
const statusOptions = ref<TestCaseStatus[]>(['Draft', 'Review', 'Actual', 'Outdated'])

onMounted(async () => {
  await Promise.all([fetchTestCase(), fetchTestCaseDetails()])
})

const fetchTestCase = async () => {
  try {
    const fetchedTestCase = await testCaseService.getTestCase(props.testCaseId)
    testCase.value = fetchedTestCase
  } catch (error) {
    console.error('Failed to fetch test cases:', error)
  }
}

const fetchTestCaseDetails = async () => {
  try {
    const fetchedTestCaseDetails = await testCaseService.getTestCaseDetails(props.testCaseId)
    testCaseDetails.value = fetchedTestCaseDetails
  } catch (error) {
    console.error('Failed to fetch test cases:', error)
  }
}

const appendStep = () => {
  if (newStep.value && newStep.value.trim().length > 0) {
    testCaseDetails.value.testSteps.push(newStep.value)
    newStep.value = ''
  } else {
    console.log('Step is empty')
  }
}

const deleteStep = (index: number) => {
  testCaseDetails.value.testSteps.splice(index, 1)
}

const updateTestCase = async () => {
  try {
    await testCaseService.updateTestCase(props.testCaseId, testCase.value)
    await testCaseService.updateTestCaseDetails(props.testCaseId, testCaseDetails.value)
  } catch (error: any) {
    console.error('Failed to update test case:', error)
  }
}
</script>
