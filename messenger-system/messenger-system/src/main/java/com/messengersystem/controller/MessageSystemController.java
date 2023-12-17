package com.messengersystem.controller;

import com.messengersystem.models.ChatHistoryResponseBody;
import com.messengersystem.models.CreateLoginUserRequestBody;
import com.messengersystem.models.GetAllUsersResponseBody;
import com.messengersystem.models.LogoutRequestBody;
import com.messengersystem.models.SendMessageRequestBody;
import com.messengersystem.models.SuccessResponseBody;
import com.messengersystem.models.UnreadMessagesResponseBody;
import com.messengersystem.models.UserCreatedResponseBody;
import com.messengersystem.services.MessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class MessageSystemController {

  @Autowired
  private MessagingService messagingService;

  @PostMapping("user")
  public ResponseEntity<UserCreatedResponseBody> createUser(@RequestBody CreateLoginUserRequestBody createLoginUserRequestBody) {
    return ResponseEntity.ok(messagingService.createUser(createLoginUserRequestBody));
  }

  @PostMapping("login")
  public ResponseEntity<SuccessResponseBody> loginUser(@RequestBody CreateLoginUserRequestBody createLoginUserRequestBody) {
    return ResponseEntity.ok(messagingService.loginUser(createLoginUserRequestBody));
  }

  @GetMapping("user")
  public ResponseEntity<GetAllUsersResponseBody> getUsers() {
    return ResponseEntity.ok(messagingService.getAllUsers());
  }

  @PostMapping("logout")
  public ResponseEntity<SuccessResponseBody> logoutUser(@RequestBody LogoutRequestBody logoutRequestBody) {
    return ResponseEntity.ok(messagingService.logOut(logoutRequestBody));
  }

  @PostMapping("user/{username}/message")
  public ResponseEntity<SuccessResponseBody> sendMessage(@PathVariable String username, @RequestBody SendMessageRequestBody sendMessageRequestBody) {
    return ResponseEntity.ok(messagingService.sendMessage(username, sendMessageRequestBody));
  }

  @GetMapping("user/{username}/unread/messages")
  public ResponseEntity<UnreadMessagesResponseBody> getUnreadMessages(@PathVariable String username) {
    return ResponseEntity.ok(messagingService.getUnreadMessages(username));
  }

  @GetMapping("user/{username}/chat/history")
  public ResponseEntity<ChatHistoryResponseBody> getChatHistory(@PathVariable String username, @RequestParam(name = "friend") String recipient) {
    return ResponseEntity.ok(messagingService.getChatHistory(username, recipient));
  }
}
