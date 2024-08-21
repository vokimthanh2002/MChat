<template>
  <div class="chat-wrapper">
    <div class="header">
      <h1 class="greeting">Chat with {{ username }}</h1>
      <button class="logout" @click="logout">Logout</button>
    </div>
    <div class="chat-container">
      <div class="chat-box">
        <div class="messages" ref="messagesContainer">
          <div
              v-for="msg in messages"
              :key="msg.timestamp"
              :class="['message', { 'sent': msg.sender === username, 'received': msg.sender !== username }]"
          >
            <img
                v-if="msg.sender !== username"
                src="https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg"
                alt="avatar"
                class="avatar"
            />
            <div class="message-content">
              <strong class="sender">{{ msg.sender }}</strong>
              <span class="content">{{ msg.content }}</span>
              <div class="timestamp">{{ formatTimestamp(msg.timestamp) }}</div>
            </div>
            <img
                v-if="msg.sender === username"
                src="https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg"
                alt="avatar"
                class="avatar"
            />
          </div>
        </div>
        <div class="input-container">
          <input
              v-model="message"
              @keyup.enter="sendMessage"
              placeholder="Type a message..."
              class="message-input"
          />
          <button @click="sendMessage" class="send-button">Send</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import axios from "axios";

export default {
  data() {
    return {
      stompClient: null,
      socketUrl: `http://localhost:8080/chat?token=${localStorage.getItem('token')}`,
      message: '',
      messages: [],
      username: localStorage.getItem('username'),
    };
  },
  methods: {
    async connect() {
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
    async fetchMessages() {
      try {
        const response = await axios.get('http://localhost:8080/messages', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        this.messages = response.data;
        this.$nextTick(() => {
          const messagesContainer = this.$refs.messagesContainer;
          messagesContainer.scrollTop = messagesContainer.scrollHeight; // Cuộn thanh cuộn đến cuối cùng
        });
      } catch (error) {
        console.error('Error fetching messages:', error);
      }
    },
    logout() {
      this.$router.push('/');
      localStorage.removeItem('token');
    },
    sendMessage() {
      if (this.message.trim() !== '') {
        this.stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({
          timestamp: new Date().toISOString(),
          sender: this.username,
          content: this.message,
          avatar: 'https://scontent.fdad1-4.fna.fbcdn.net/v/t39.30808-6/455260886_1943578062751604_7333532100857127975_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=aa7b47&_nc_eui2=AeFu6jAodcMmaNzFDFzbI6V0qCDvpiMJvj6oIO-mIwm-Psi-3VMvi32BKwIb85ieGtrg3qSjSn1l-ui7DSk6amCj&_nc_ohc=E4Rfx1p_qToQ7kNvgGFITy0&_nc_ht=scontent.fdad1-4.fna&oh=00_AYDi9H4nfGJ-SAaLF244nT7C-FhoZgjrcI5sqmhSSxy9Bg&oe=66C25B9A',
          type: 'CHAT'
        }));
        this.message = ''; // Clear the input field after sending
      }
    },
    onMessageReceived(message) {
      this.messages.push(message);
      this.$nextTick(() => {
        const messagesContainer = this.$refs.messagesContainer;
        messagesContainer.scrollTop = messagesContainer.scrollHeight; // Cuộn thanh cuộn đến tin nhắn mới nhất
      });
    },
    formatTimestamp(timestamp) {
      const date = new Date(timestamp);
      return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
    }
  },
  mounted() {
    this.fetchMessages();
    this.connect();
    this.$nextTick(() => {
      const messagesContainer = this.$refs.messagesContainer;
      messagesContainer.scrollTop = messagesContainer.scrollHeight; // Cuộn thanh cuộn đến cuối cùng khi trang tải
    });
  },
};
</script>

<style scoped>
.chat-wrapper {
  height: 100vh;
  background-color: #f0f2f5;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding: 20px;
}

.header {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.greeting {
  font-size: 1.5rem;
  color: #333;
}

.logout {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
}

.logout:hover {
  background-color: #c82333;
}

.chat-container {
  width: 100%;
  max-width: 800px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chat-box {
  width: 100%;
  height: 70vh;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto; /* Đảm bảo thanh cuộn tự động hiển thị khi cần */
  background-image: url("https://gcs.tripi.vn/public-tripi/tripi-feed/img/474064zjH/background-2d-dep-cho-photoshop_023229230.jpg");
  display: flex;
  flex-direction: column;
}

.message {
  display: flex;
  margin-bottom: 10px;
  align-items: flex-end;
}
.sent {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.received {
  align-self: flex-start;
}

.message-content {
  padding: 10px;
  border-radius: 20px;
  background-color: #00ccf1;
  color: #ffffff;
  max-width: 70%;
  display: flex;
  flex-direction: column;
  word-wrap: break-word;
}

.received .message-content {
  background-color: #e4e6eb;
  color: black;
}

.timestamp {
  font-size: 0.75rem;
  color: #888;
  margin-top: 5px;
  text-align: right;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 10px;
}

.input-container {
  display: flex;
  padding: 15px;
  background-color: #ffffff;
  border-top: 1px solid #ddd;
}

.message-input {
  flex: 1;
  border: 2px solid #ddd;
  border-radius: 25px;
  padding: 10px 20px;
  font-size: 1rem;
  outline: none;
  margin-right: 10px;
}

.send-button {
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 25px;
  padding: 10px 20px;
  font-size: 1rem;
  cursor: pointer;
}

.send-button:hover {
  background-color: #0056b3;
}
</style>
