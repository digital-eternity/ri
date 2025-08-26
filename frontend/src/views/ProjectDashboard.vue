<template>
  <v-container>
    <h1>Project Dashboard</h1>
    <v-row>
      <v-col>
        <v-card>
          <v-card-text>
            <apexchart type="donut" height="350" :options="priorityChartOptions" :series="prioritySeries"></apexchart>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col>
        <v-card>
          <v-card-text>
            <apexchart type="donut" height="350" :options="statusChartOptions" :series="statusSeries"></apexchart>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
      <v-card>
        <v-card-text>
          <apexchart type="treemap" height="500" :options="tagsChartOptions" :series="tagSeries"></apexchart>
        </v-card-text>
      </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang='ts'>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'

import { testCaseService } from '../api/testCaseService'

// types
import { TestCase } from '@/types/tmsTypes'
import { ApexOptions } from 'apexcharts'

const route = useRoute()
const projectId = computed<string | undefined>(() => route.params.projectId as string | undefined)

interface SeriesData {
  x: string;
  y: number;
}

const tagSeries = ref<[{ data: SeriesData[] }]>([])
const prioritySeries = ref<number[]>([])
const statusSeries = ref<number[]>([])

const tagsChartOptions = ref<ApexOptions>({
  legend: {
    show: true
  },
  chart: {
    height: 500,
    type: 'treemap'
  },
  title: {
    text: 'Test Case Distribution by Tag'
  }
})

const priorityChartOptions = ref<ApexOptions>({
  legend: {
    show: true
  },
  chart: {
    height: 150,
    type: 'donut'
  },
  title: {
    text: 'Test Cases by Priority'
  },
  labels: []
})

const statusChartOptions = ref<ApexOptions>({
  legend: {
    show: true
  },
  chart: {
    height: 150,
    type: 'donut'
  },
  title: {
    text: 'Test Cases by Status'
  },
  labels: []
})

onMounted(async () => {
  await fetchTestCaseTags()
})

const fetchTestCaseTags = async () => {
  try {
    const testCasesData: TestCase[] = await testCaseService.getTestCases(projectId.value) as TestCase[]
    createTagFrequencyMap(testCasesData)
    createPriorityFrequencyDonut(testCasesData)
    createStatusFrequencyDonut(testCasesData)
  } catch (error) {
    console.error('Failed to fetch test cases data:', error)
  }
}

function createTagFrequencyMap (testCases: TestCase[]) {
  const tagsData: Map<string, number> = testCases.reduce((acc, testCase) => {
    testCase.tags.forEach((tag: string) => {
      const currentCount = acc.get(tag) || 0
      acc.set(tag, currentCount + 1)
    })
    return acc
  }, new Map<string, number>())

  const sortedTags = new Map(
    [...tagsData.entries()]
      .sort(([, a], [, b]) => b - a)
  )

  const tagsDt = Array.from(sortedTags, ([key, value]) => ({
    x: key,
    y: value
  }))

  tagSeries.value = [{ data: tagsDt }]
}

function createPriorityFrequencyDonut (testCases: TestCase[]) {
  const priorityData: Map<string, number> = testCases.reduce((acc, testCase) => {
    const currentCount = acc.get(testCase.priority) || 0
    acc.set(testCase.priority, currentCount + 1)
    return acc
  }, new Map<string, number>())

  priorityChartOptions.value.labels?.push(...priorityData.keys())
  prioritySeries.value = [...priorityData.values()]
}

function createStatusFrequencyDonut (testCases: TestCase[]) {
  const statusData: Map<string, number> = testCases.reduce((acc, testCase) => {
    const currentCount = acc.get(testCase.status) || 0
    acc.set(testCase.status, currentCount + 1)
    return acc
  }, new Map<string, number>())

  statusChartOptions.value.labels?.push(...statusData.keys())
  statusSeries.value = [...statusData.values()]
}
</script>
