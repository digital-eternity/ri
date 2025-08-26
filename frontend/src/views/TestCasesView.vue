<template>
  <v-container>
    <v-row class="mt-10">
      <v-col cols="12" md="4" class="text-left">
        <v-row class="mb-10 mr-0">
          <v-spacer/>
          <ModalWrapper
            ref="testCaseModal"
            title="Create Test Case"
            :component="NewTestCaseForm"
            :componentProps="{ testCase: testCaseUnderAction }"
            @component-save="saveTestCase"
            @component-cancel="clearTestCaseUnderAction"
          >
            <template v-slot:activator="{ props }">
              <v-btn class="mr-5" color="primary" v-bind="props" @click="openCreate">Create Test Case</v-btn>
              <v-btn color="secondary" @click="showAiPanel = !showAiPanel">Try with AI</v-btn>
            </template>
          </ModalWrapper>
        </v-row>

        <v-alert v-if="tcCreateError" type="error">{{ tcCreateError }}</v-alert>

        <TestCaseList
          :test-cases="testCases"
          :editable=true
          @open-test-case="openTestCase"
          @delete-selected="openTestCaseMassDeletionConfirmation"
        />

        <ConfirmationModal
          ref="confirmationModal"
          title="Confirm Deletion"
          message="Are you sure you want to delete selected test cases?"
          @confirm="deleteConfirmed"
          @cancel="closeConfirmationModal"
        />
      </v-col>
      <v-col cols="12" md="8">
        <TestCaseDetails v-if="testCaseId" :testCaseId="testCaseId" :editable=true :key="tcDetailsKey"/>
        <AiTestPanel class="ai-panel-container" :show="showAiPanel" @update:show="showAiPanel = $event" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { testCaseService } from '@/api/testCaseService'
import TestCaseList from '@/components/TestCaseList.vue'
import NewTestCaseForm from '@/components/NewTestCaseForm.vue'
import TestCaseDetails from '@/components/TestCaseDetails.vue'
import ModalWrapper from '@/components/ModalWrapper.vue'
import ConfirmationModal from '@/components/ConfirmationModal.vue'
import { TestCase } from '@/types/tmsTypes'
import AiTestPanel from '@/components/AiTestPanel.vue'

const testCases = ref<TestCase[]>([])
const testCaseModal = ref<any>(null) // Adjust type based on ModalWrapper component
const tcCreateError = ref<any>(null)
const testCaseUnderAction = ref<TestCase | null>(null)
const testCasesToDelete = ref<number[]>([])
const route = useRoute()
const projectId = computed<string | undefined>(() => route.params.projectId as string | undefined)
const testCaseId = computed<number>(() => Number(route.params.testCaseId))
const tcDetailsKey = ref(0)
const confirmationModal = ref<any>(null) // Adjust type based on ConfirmationModal component
const vueRouter = useRouter()

onMounted(async () => {
  if (projectId.value) {
    await fetchTestCases()
  }
})

const fetchTestCases = async () => {
  if (projectId.value) {
    try {
      testCases.value = await testCaseService.getTestCases(projectId.value)
    } catch (error) {
      console.error('Failed to fetch test cases:', error)
    }
  } else {
    testCases.value = []
  }
}

const saveTestCase = async (testCase: any) => {
  try {
    if (!projectId.value) {
      console.error('Project ID is undefined')
      return
    }
    testCase.projectId = Number(projectId.value)
    const data = await testCaseService.createTestCase(testCase)
    const details = { description: testCase.description, testCaseId: data.id } // Include id
    await testCaseService.updateTestCaseDetails(data.id, details)
    await fetchTestCases()
    testCaseModal.value?.close()
    tcCreateError.value = null
    clearTestCaseUnderAction()
  } catch (error: any) {
    tcCreateError.value = error
    console.error('Failed to save test case:', error)
  }
}

const openCreate = () => {
  clearTestCaseUnderAction()
  testCaseModal.value?.open()
}

const clearTestCaseUnderAction = () => {
  testCaseUnderAction.value = null
}

const clearTestCasesToDelete = () => {
  testCasesToDelete.value = []
}

const openTestCase = async (testCase: TestCase) => {
  if (!projectId.value) {
    console.error('Project ID is undefined')
    return
  }
  await vueRouter.push(`/projects/${projectId.value}/test-cases/${testCase.id}`)
  forceRerender()
}

const openTestCaseMassDeletionConfirmation = (testCasesIds: number[]) => {
  testCasesToDelete.value = testCasesIds
  confirmationModal.value?.open()
}

const closeConfirmationModal = () => {
  clearTestCasesToDelete()
  confirmationModal.value?.close()
}

const deleteConfirmed = async () => {
  if (testCasesToDelete.value) {
    await deleteTestCase(testCasesToDelete.value)
    clearTestCasesToDelete()
  }
}

const deleteTestCase = async (ids: number[]) => {
  try {
    await testCaseService.deleteTestCase(ids)
    await fetchTestCases()
  } catch (error) {
    console.error('Failed to delete test case:', error)
  }
}

const forceRerender = () => {
  tcDetailsKey.value += 1
}

const showAiPanel = ref(false)
</script>
