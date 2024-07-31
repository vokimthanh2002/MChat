<template>
  <h1>CHAT ROOM</h1>
  <div class="chat-container">
    <div class="chat-box">
      <div class="messages">
        <div v-for="msg in messages" :key="msg.timestamp" class="message">
          <strong class="sender">{{ msg.sender }}:</strong>
          <span class="content">{{ msg.content }}</span>
        </div>
      </div>
      <div class="input-container">
        <input
            v-model="message"
            @keyup.enter="sendMessage"
            placeholder="Type a message"
            class="message-input"
        />
        <button @click="sendMessage" class="send-button">Send</button>
      </div>
    </div>
  </div>
</template>

<script>
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

export default {
  data() {
    return {
      stompClient: null,
      socketUrl: 'http://localhost:8080/chat', // Địa chỉ WebSocket của bạn
      message: '',
      messages: []
    };
  },
  methods: {
    connect() {
      const socket = new SockJS(this.socketUrl);
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({}, frame => {
        console.log('Connected: ' + frame);
        this.stompClient.subscribe('/topic/public', messageOutput => {
          this.onMessageReceived(JSON.parse(messageOutput.body));
        });
      }, error => {
        console.log('STOMP error: ' + error);
      });
    },
    sendMessage() {
      if (this.message.trim() !== '') {
        this.stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({
          sender: 'anonymous',
          content: this.message,
          type: 'CHAT'
        }));
        this.message = ''; // Clear the input field after sending
      }
    },
    onMessageReceived(message) {
      this.messages.push(message);
    }
  },
  mounted() {
    this.connect();
  }
};
</script>

<style scoped>
.chat-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.chat-box {
  width: 400px;
  max-width: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  display: flex;
  flex-direction: column;
  height: 80vh;
}

.messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  border-bottom: 1px solid #ddd;
}
h1{
  text-align: center;
}

.message {
  margin-bottom: 10px;
  padding: 5px;
  border-radius: 4px;
  background-color: #e9e9e9;
}

.sender {
  font-weight: bold;
}

.input-container {
  display: flex;
  border-top: 1px solid #ddd;
  padding: 10px;
  background-color: #f9f9f9;
}

.message-input {
  flex: 1;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 8px;
  margin-right: 10px;
}

.send-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
}

.send-button:hover {
  background-color: #0056b3;
}
</style>
