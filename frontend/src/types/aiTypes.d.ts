interface OpenAICompatibleRequest {
  messages: Array<{
    role: 'system' | 'user' | 'assistant';
    content: string;
  }>;
  model: string;
  // eslint-disable-next-line
  max_tokens: number;
  stream: boolean;
}

interface AiChatMessage {
  role: 'system' | 'user' | 'assistant';
  content: string;
}

export {
  OpenAICompatibleRequest,
  AiChatMessage
}
