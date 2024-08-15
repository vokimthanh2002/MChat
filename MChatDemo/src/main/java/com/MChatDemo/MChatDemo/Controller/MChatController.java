package com.MChatDemo.MChatDemo.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.MChatDemo.MChatDemo.Model.MChat;

@Controller
public class MChatController {
	@MessageMapping("/chat")
	@SendTo("/mchat/messages")
	public MChat sendMessage (@Payload MChat mchat) {
		System.out.println("Content message: "+mchat);
		return mchat;
	}
}
