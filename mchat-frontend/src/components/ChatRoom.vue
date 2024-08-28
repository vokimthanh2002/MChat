<template>
  <div class="chat-app">
    <div class="chat-header">
      <h2 class="app-title">MChat</h2>
      <button class="logout-btn" @click="logout">Logout</button>
    </div>
    <div class="chat-body">
      <aside class="chat-sidebar">
        <ul class="user-list">
          <li
              class="user-item"
              v-for="user in users"
              :key="user.username"
              @click="openChatRoom(user.username)"
          >
            <img :src="user.avatar" alt="avatar" class="user-avatar" />
            <span class="user-name">{{ user.name }}</span>
          </li>
        </ul>
      </aside>
      <section class="chat-content" v-if="userReceiver">
        <div class="chat-header-mes">
          <img class="receiver-avatar" :src="userReceiver.avatar" />
          <h3 class="receiver-name">{{ userReceiver.name }}</h3>
        </div>
        <div class="messages-container" ref="messagesContainer">
          <div
              v-for="msg in messages"
              :key="msg.timestamp"
              :class="['message', { 'sent': msg.sender === username, 'received': msg.sender !== username }]"
          >
            <div class="message-content">
              <p class="message-text">{{ msg.content }}</p>
              <span class="message-timestamp">{{ formatTimestamp(msg.timestamp) }}</span>
            </div>
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
      </section>
      <div class="no-chat-selected" v-else>
        <h3>Chào mừng bạn đến với ứng dụng MChat</h3>
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
      users: [],
      username: localStorage.getItem('username'),
      userReceiver: null,
      currentChatRoom: null,
      chatRoom: null,
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
    async openChatRoom(userName) {
      const response = await axios.get('http://localhost:8080/room', {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        params: {
          user1Username: this.username,
          user2Username: userName
        }
      });
      this.currentChatRoom = response.data.chatRoomId;
      console.log(this.currentChatRoom);
      console.log(response.data);
      this.getChatroomById();
      this.getUserByUsername(userName);
      this.messages = response.data.messages;
      this.$nextTick(() => {
        const messagesContainer = this.$refs.messagesContainer;
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
      });
    },
    async fetchUsers() {
      try {
        const response = await axios.get('http://localhost:8080/user/get-users', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });

        // Lấy danh sách người dùng từ API
        this.users = response.data;

        // Lấy thông tin người dùng từ localStorage
        const storedUsername = localStorage.getItem('username');

        // Nếu có thông tin người dùng trong localStorage, loại bỏ người đó khỏi danh sách
        if (storedUsername) {
          this.users = this.users.filter(user => user.username !== storedUsername);
        }

      } catch (error) {
        console.error('Error fetching users:', error);
      }
    },
    async getChatroomById() {
      try {
        const response = await axios.get('http://localhost:8080/get-chatroom', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          params: {
            idRoom: this.currentChatRoom,
          }
        });
        this.chatRoom = response.data;
      } catch (error) {
        console.error('Error chatroom:', error);
      }
    },
    async getUserByUsername(userName) {
      try {
        const response = await axios.get('http://localhost:8080/user/get-user', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          params: {
            username: userName,
          }
        });
        this.userReceiver = response.data;
      } catch (error) {
        console.error('Error chatroom:', error);
      }
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
          messagesContainer.scrollTop = messagesContainer.scrollHeight;
        });
      } catch (error) {
        console.error('Error fetching messages:', error);
      }
    },
    logout() {
      this.$router.push('/');
      localStorage.removeItem('token');
      localStorage.removeItem('username');
    },
    sendMessage() {
      if (this.message.trim() !== '') {
        this.stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({
          timestamp: new Date().toISOString(),
          sender: this.username,
          content: this.message,
          type: 'CHAT',
          chatRoom: this.chatRoom,
        }));
        this.message = '';
      }
    },
    onMessageReceived(message) {
      this.messages.push(message);
      if (message.sender !== this.username) {
        const audio = new Audio('/notification.mp3');
        audio.play();
      }
      this.$nextTick(() => {
        const messagesContainer = this.$refs.messagesContainer;
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
      });
    },
    formatTimestamp(timestamp) {
      const date = new Date(timestamp);
      return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
    }
  },
  mounted() {
    this.fetchUsers();
    this.connect();
  },
};
</script>

<style scoped>
.chat-app {
  display: flex;
  flex-direction: column;
  height: 100vh;
  font-family: Arial, sans-serif;
  background-color: #1d1d1d;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #95aabd;
  color: white;
  padding: 10px 20px;
}
.chat-header-mes {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  color: white;
  padding: 10px 20px;
}

.app-title {
  font-size: 1.5rem;
  margin: 0;
}

.logout-btn {
  background-color: #5ca853;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
}

.chat-body {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.chat-sidebar {
  width: 250px;
  background-color: #333;
  color: white;
  padding: 20px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
}

.user-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.user-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  cursor: pointer;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.user-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 15px;
}

.user-name {
  font-size: 1rem;
}

.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #1d1d1d;
  padding: 20px;
  overflow: hidden;
}

.chat-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.receiver-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.receiver-name {
  font-size: 1.2rem;
  font-weight: bold;
  margin: 0;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background-color: #2c2c2c;
  border-radius: 8px;
  margin-bottom: 15px;
}

.message {
  display: flex;
  align-items: flex-end;
  margin-bottom: 10px;
}

.message.sent {
  justify-content: flex-end;
}

.message.received {
  justify-content: flex-start;
}

.message-content {
  max-width: 70%;
  padding: 10px;
  border-radius: 10px;
  background-color: #5ca853;
  color: white;
}

.message.received .message-content {
  background-color: #444;
  color: white;
}

.message-text {
  margin: 0;
}

.message-timestamp {
  font-size: 0.75rem;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 5px;
  text-align: right;
}

.input-container {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.message-input {
  flex: 1;
  padding: 10px;
  border-radius: 20px;
  border: 1px solid #444;
  font-size: 1rem;
  margin-right: 10px;
  background-color: #333;
  color: white;
}

.send-button {
  background-color: #5ca853;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 20px;
  cursor: pointer;
}

.no-chat-selected {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
  font-size: 1.2rem;
  color: #888;
}
</style>
