<template>
  <div>
    <div>-----Tin nhắn------</div>
    <ul>
      <li v-for="(mess, index) in messages" :key="index">
        <span>{{ mess.nickname }}</span>
        <span>: </span>
        <span>{{ mess.content }}</span>
      </li>
    </ul>
    <div style="display: flex; align-items: center;">
      <input
        type="text"
        placeholder="Nhập nickname"
        v-model="nickname"
      />
      <input
        type="text"
        placeholder="Nhập tin nhắn"
        v-model="message"
      />
      <button type="submit" @click="sendMessage" :disabled="!message.trim()">Gửi</button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client/dist/sockjs';

export default {
  setup() {
    const messages = ref([]);
    const nickname = ref('');
    const message = ref('');
    const stompClient = ref(null);

    const sendMessage = () => {
      if (message.value.trim() && stompClient.value && stompClient.value.connected) {
        const mChat = {
          nickname: nickname.value,
          content: message.value
        };
        stompClient.value.send('/app/chat', {}, JSON.stringify(mChat));
        // Gửi message xuống server theo đường dẫn này
        message.value = '';
      }
    };

    const onMessageReceived = (message) => {
      const receivedMessage = JSON.parse(message.body);
      messages.value.push(receivedMessage);
    };
    // Hàm này sẽ đc gọi khi server nhận được message từ client

    onMounted(() => {
      const socket = new SockJS('http://localhost:8080/web-socket');
      const client = Stomp.over(socket);

      client.connect({}, () => {
        if (stompClient.value) {
          return;
        }
        stompClient.value = client;
        client.subscribe('/mchat/messages', onMessageReceived);
        // Server đẩy message lên đường dẫn này
      });
    });

    onUnmounted(() => {
      if (stompClient.value && stompClient.value.connected) {
        stompClient.value.disconnect();
        stompClient.value = null;
      }
    });

    

    return {
      messages,
      nickname,
      message,
      sendMessage
    };
  }
};
</script>