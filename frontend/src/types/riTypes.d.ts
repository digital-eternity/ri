type Note = '' | '1' | '2' | '4' | '8' | '16';
type RhythmElementType = 'PAUSE' | 'NOTE';
type RhythmElementDuration = 'WHOLE' | 'HALF' | 'QUARTER' | 'EIGHT' | 'SIXTEENTH' | 'THIRTYSECOND';

interface User {
  id?: number
  username: string
  globalRole: string
}

interface RhythmElement {
  id?: number,
  type: RhythmElementType,
  duration: RhythmElementDuration,
}

interface RhythmBlock {
  id?: number,
  label: string,
  isStrong: boolean,
  rhythmElements: RhythmElement[]
}

interface Rhythm {
  id?: number,
  name: string,
  description?: string,
  size: number,
  baseNote: RhythmElementDuration
  rhythm: RhythmBlock[],
}

export {
  User,
  RhythmElementType,
  RhythmElementDuration,
  RhythmElement,
  RhythmBlock,
  Rhythm
}
