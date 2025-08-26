<template>
  <v-container>

      <v-row>
        <v-col>
          <apexchart type="bar" height="100" :options="chartOptions" :series="series"></apexchart>
        </v-col>
      </v-row>

    <v-row class="mt-10">
      <v-col cols="12" md="4" class="text-left">

        <v-alert v-if="tcCreateError" type="error">{{ tcCreateError }}</v-alert>

        <TestCaseList
          :test-cases="testCases"
          :editable=false
          @open-test-case="openTestCase"
        />
      </v-col>
      <v-col cols="12" md="8">
        <TestCaseDetails v-if="testId" :testCaseId="testCaseId" :editable=false :key="tcDetailsKey"/>
        <ExecutingTestDetails v-if="testId" :testCaseId="testId" :key="tcDetailsKey"/>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { testCaseService } from '@/api/testCaseService'
import { launchService } from '@/api/launchService'
import TestCaseList from '@/components/TestCaseList.vue'
import TestCaseDetails from '@/components/TestCaseDetails.vue'
import ExecutingTestDetails from '@/components/ExecutingTestDetails.vue'
import { TestCase, ExecutingTest } from '@/types/tmsTypes'
import { ApexOptions } from 'apexcharts'

const testCases = ref<TestCase[]>([])
const launchTests = ref<ExecutingTest[]>([])
const tcCreateError = ref<any>(null)
const route = useRoute()
const projectId = computed<string | undefined>(() => route.params.projectId as string | undefined)
const testId = computed<string | undefined>(() => route.params.testId as string | undefined)
const launchId = computed<string | undefined>(() => route.params.launchId as string | undefined)
const testCaseId = ref<number>(null)
const tcDetailsKey = ref(0)
const vueRouter = useRouter()

interface SeriesData {
  name: string;
  data: number[];
}

const series = ref<SeriesData[]>([])

const chartOptions = ref<ApexOptions>({
  chart: {
    type: 'bar',
    stacked: true,
    stackType: '100%',
    width: '500px',
    toolbar: {
      show: false
    }
  },
  plotOptions: {
    bar: {
      horizontal: true
    }
  },
  stroke: {
    width: 1,
    colors: ['#fff']
  },
  fill: {
    opacity: 1
  },
  legend: {
    position: 'top',
    horizontalAlign: 'right',
    offsetX: 40
  },
  yaxis: {
    show: false,
    labels: {
      show: false
    },
    axisBorder: {
      show: false
    },
    axisTicks: {
      show: false
    }
  },
  xaxis: {
    labels: {
      show: false
    },
    axisBorder: {
      show: false
    },
    axisTicks: {
      show: false
    }
  },
  colors: ['#00b140', '#cf2e2e', '#94a3b8']
})

onMounted(async () => {
  if (projectId.value) {
    await fetchTestCases()
    getLaunchStatsSeries()
  }
})

const fetchTestCases = async () => {
  if (projectId.value) {
    try {
      launchTests.value = await launchService.getLaunchTests(launchId.value)
      const targetIdsSet = new Set(launchTests.value.map(test => test.testCaseId))

      const allTestCases: TestCase[] = await testCaseService.getTestCases(projectId.value)
      testCases.value = allTestCases.filter(
        testCase => targetIdsSet.has(testCase.id)
      )

      const launchTestsMap = new Map<number, ExecutingTest>()
      launchTests.value.forEach(test => {
        launchTestsMap.set(test.testCaseId, test)
      })

      // super dirty hack!!!
      testCases.value.forEach(testCase => {
        const launchTest = launchTestsMap.get(testCase.id)
        if (launchTest) {
          testCase.status = launchTest.status
        }
      })
    } catch (error) {
      console.error('Failed to fetch test cases:', error)
    }
  } else {
    testCases.value = []
  }
}

function getLaunchStatsSeries () {
  const statsData: Map<string, number> = new Map<string, number>([['Passed', 0], ['Failed', 0], ['InProgress', 0]])

  const statsData1: Map<string, number> = launchTests.value.reduce((acc, testCase) => {
    const currentCount = acc.get(testCase.status) || 0
    acc.set(testCase.status, currentCount + 1)
    return acc
  }, new Map(statsData))

  const statsDt = Array.from(statsData1, ([key, value]) => ({
    name: key,
    data: [value]
  }))

  series.value = statsDt
}

const openTestCase = async (testCase: TestCase) => {
  if (!projectId.value) {
    console.error('Project ID is undefined')
    return
  }
  testCaseId.value = testCase.id
  const foundTest: ExecutingTest | undefined = launchTests.value.find(t => t.testCaseId === testCase.id)

  await vueRouter.push(`/projects/${projectId.value}/launches/${launchId.value}/tests/${foundTest.id}`)
  forceRerender()
}

const forceRerender = () => {
  tcDetailsKey.value += 1
}
</script>

<style>
.v-chip.Passed {
  color: #00b140;
}

.v-chip.Failed {
  color: #cf2e2e;
}
</style>
