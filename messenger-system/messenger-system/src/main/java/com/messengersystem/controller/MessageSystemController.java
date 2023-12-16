package com.messengersystem.controller;

import com.messengersystem.models.CreateLoginUserRequestBody;
import com.messengersystem.models.GetAllUsersResponseBody;
import com.messengersystem.models.LogoutRequestBody;
import com.messengersystem.models.SuccessResponseBody;
import com.messengersystem.models.UserCreatedResponseBody;
import com.messengersystem.services.MessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
