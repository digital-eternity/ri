type Note = '' | '1' | '2' | '4' | '8' | '16';

interface User {
  id?: number
  username: string
  globalRole: string
}

interface Rhythm {
  id?: number,
  name: string,
  description?: string,
  rhythm: boolean[],
  length: number,
  subDivision: number
}

interface RhythmBlock {
  duration: number,
  blockSubDivisions: RhythmBlock[],
  subDivision: number,
  isStrong: boolean
}

export {
  User,
  Rhythm
}
