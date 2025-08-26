interface QafModal {
  open(): void,
  close(): void
}

type Note = '' | '1' | '2' | '4' | '8' | '16';
type NoteIcon = 'mdi-check-box-outline-blank' | 'mdi-music-note-whole' | 'mdi-music-note-half' | 'mdi-music-note-quarter' | 'mdi-music-note-eighth' | 'mdi-music-note-sixteenth';

export {
  QafModal,
  Note,
  NoteIcon
}
