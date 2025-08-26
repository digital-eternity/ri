type TestCasePriority = 'High' | 'Medium' | 'Low'
type TestCaseStatus = 'Draft' | 'Review' | 'Actual' | 'Outdated'
type TestStatus = 'Passed' | 'Failed' | 'InProgress'

interface Project {
  id?: number
  name: string,
  description: string
}

interface User {
  id?: number
  username: string
  globalRole: string
}

interface ProjectMember {
  id: number
  username: string
  projectRole: string
}

interface TestCase {
  id: number
  name: string
  priority: TestCasePriority
  status: TestCaseStatus
  tags: string[],
  projectId?: number
  assignedToUserId? : number,
  requirementId? : string,
  version? : string
}

interface TestCaseDetails {
  id?: number
  description?: string
  preconditions?: string
  testSteps?: string[]
  expectedResult?: string
}

interface Launch {
  id?: string
  projectId?: number
  name: string
  startTime: Date
  endTime?: Date
  tags: string[]
  testCases: number[]
}

interface ExecutingTest {
  id: string
  testCaseId: number
  status: TestStatus
  comment: string
}

export {
  Project,
  User,
  ProjectMember,
  TestCase,
  TestCaseDetails,
  Launch,
  ExecutingTest,
  TestStatus,
  TestCaseStatus,
  TestCasePriority
}
