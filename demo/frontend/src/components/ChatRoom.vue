<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2>CHAT ROOM</h2>
    </div>
    <div class="chat-box">
      <div class="messages">
        <div v-for="msg in messages" :key="msg.timestamp" class="message" :class="{ 'own-message': isOwnMessage(msg.sender) }">
          <div class="message-content">
            <strong class="sender">{{ msg.sender }}</strong>
            <span class="content">{{ msg.content }}</span>
            <span class="timestamp">{{ formatTime(msg.timestamp) }}</span>
          </div>
        </div>
      </div>
      <div class="input-container">
        <input
            v-model="message"
            @keyup.enter="sendMessage"
            placeholder="Type a message"
            class="message-input"
            :disabled="!isConnected"
        />
        <button @click="sendMessage" class="send-button" :disabled="!isConnected || message.trim() === ''">Gửi</button>
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
      socketUrl: 'http://localhost:8080/chat',
      message: '',
      messages: [],
      currentUser: '', // Replace with actual current user data
      isConnected: false
    };
  },
  methods: {
    connect() {
      const socket = new SockJS(this.socketUrl);
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({}, frame => {
        console.log('Connected: ' + frame);
        this.isConnected = true;
        this.stompClient.subscribe('/topic/public', messageOutput => {
          this.onMessageReceived(JSON.parse(messageOutput.body));
        });
      }, error => {
        console.log('STOMP error: ' + error);
        this.isConnected = false;
      });
    },
    sendMessage() {
      if (this.message.trim() !== '' && this.isConnected) {
        this.stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({
          sender: this.currentUser,
          content: this.message,
          type: 'CHAT'
        }));
        this.message = ''; // Clear the input field after sending
      }
    },
    onMessageReceived(message) {
      this.messages.push(message);
    },
    isOwnMessage(sender) {
      return sender === this.currentUser;
    },
    formatTime(timestamp) {
      const date = new Date(timestamp);
      return date.toLocaleString([], {hour: '2-digit', minute: '2-digit', day: '2-digit', month: 'short'});
    }
  },
  mounted() {
    this.currentUser = 'YourUsername'; // Replace with actual logic to get the current user
    this.connect();
  }
};
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f3f3f3;
}

.chat-header {
  padding: 10px 20px;
  background-color: #0088cc;
  color: #fff;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.chat-box {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: #e5ddd5;
}

.messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.message {
  display: flex;
  max-width: 80%;
}

.own-message {
  align-self: flex-end;
  background-color: #dcf8c6;
}

.message-content {
  background-color: #fff;
  border-radius: 8px;
  padding: 10px;
  box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1);
  position: relative;
  display: flex;
  flex-direction: column;
}

.sender {
  font-weight: bold;
  color: #007bff;
  margin-bottom: 5px;
}

.content {
  color: #181818;
}

.timestamp {
  font-size: 0.75em;
  color: #888;
  margin-top: 5px;
  align-self: flex-end;
}

.input-container {
  display: flex;
  border-top: 1px solid #ddd;
  padding: 10px;
  background-color: #fff;
}

.message-input {
  flex: 1;
  border: 1px solid #ddd;
  border-radius: 20px;
  padding: 10px;
  margin-right: 10px;
}

.send-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 20px;
  padding: 10px 20px;
  cursor: pointer;
}

.send-button:hover {
  background-color: #0056b3;
}

@media (max-width: 768px) {

  .chat-container {
    padding: 5px;
  }

  .message-content {
    font-size: 0.9em;
  }

  .send-button {
    padding: 8px 16px;
  }
}
</style>
