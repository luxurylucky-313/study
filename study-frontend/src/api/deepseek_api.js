// DeepSeek API 配置
const DEEPSEEK_API_KEY = 'sk-2835cf3920df4aeb90b9aa2161aee1ac';
const DEEPSEEK_API_URL = 'https://api.deepseek.com/v1/chat/completions';

/**
 * 调用 DeepSeek API 进行对话
 * @param {string} prompt - 用户输入的问题
 * @returns {Promise<string>} AI 的回答
 */
export const chatWithDeepseek = async (prompt) => {
  try {
    const response = await fetch(DEEPSEEK_API_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${DEEPSEEK_API_KEY}`
      },
      body: JSON.stringify({
        model: 'deepseek-chat',
        messages: [
          {
            role: 'user',
            content: prompt
          }
        ],
        temperature: 0.7,
        max_tokens: 1000
      })
    });

    const data = await response.json();
    return data.choices[0].message.content;
  } catch (error) {
    console.error('DeepSeek API 调用失败:', error);
    throw new Error('AI 服务暂时不可用,请稍后重试');
  }
};