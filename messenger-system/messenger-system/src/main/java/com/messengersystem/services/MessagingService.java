package com.messengersystem.services;

import com.messengersystem.models.CreateLoginUserRequestBody;
import com.messengersystem.models.GetAllUsersResponseBody;
import com.messengersystem.models.LogoutRequestBody;
import com.messengersystem.models.SuccessResponseBody;
import com.messengersystem.models.UserCreatedResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.messengersystem.constants.MessagingServiceConstants.EXISTING_USER_MESSAGE;
import static com.messengersystem.constants.MessagingServiceConstants.FAILURE_STATUS;
import static com.messengersystem.constants.MessagingServiceConstants.LOGIN_SUCCESSFUL;
import static com.messengersystem.constants.MessagingServiceConstants.LOGOUT_FAILURE_MESSAGE;
import static com.messengersystem.constants.MessagingServiceConstants.NEW_USER_CREATED_MESSAGE;
import static com.messengersystem.constants.MessagingServiceConstants.SUCCESS_LOGOUT_MESSAGE;
import static com.messengersystem.constants.MessagingServiceConstants.SUCCESS_STATUS;
import static com.messengersystem.constants.MessagingServiceConstants.USER_NOT_FOUND;
import static com.messengersystem.constants.MessagingServiceConstants.WRONG_PASSWORD;

@Service
@Slf4j
public class MessagingService {

  private final Map<String, String> usersMap;
  private final Set<String> loginSessionsSet;

  public MessagingService() {
    this.usersMap = new HashMap<>();
    this.loginSessionsSet = new HashSet<>();
  }

  public UserCreatedResponseBody createUser(CreateLoginUserRequestBody createLoginUserRequestBody) {
    if (usersMap.containsKey(createLoginUserRequestBody.getUsername())) {
      return UserCreatedResponseBody.builder()
          .status(FAILURE_STATUS)
          .message(EXISTING_USER_MESSAGE)
          .build();
    }
    usersMap.put(createLoginUserRequestBody.getUsername(), createLoginUserRequestBody.getPassword());
    return UserCreatedResponseBody.builder()
        .status(SUCCESS_STATUS)
        .message(NEW_USER_CREATED_MESSAGE)
        .build();
  }

  public SuccessResponseBody loginUser(CreateLoginUserRequestBody createLoginUserRequestBody) {
    if (usersMap.containsKey(createLoginUserRequestBody.getUsername()) &&
        usersMap.get(createLoginUserRequestBody.getUsername()).equals(createLoginUserRequestBody.getPassword())) {
      loginSessionsSet.add(createLoginUserRequestBody.getUsername());
      return SuccessResponseBody.builder()
          .status(SUCCESS_STATUS)
          .message(LOGIN_SUCCESSFUL)
          .build();
    } else if (usersMap.containsKey(createLoginUserRequestBody.getUsername()) &&
        !usersMap.get(createLoginUserRequestBody.getUsername()).equals(createLoginUserRequestBody.getPassword())) {
      return SuccessResponseBody.builder()
          .status(FAILURE_STATUS)
          .message(WRONG_PASSWORD)
          .build();
    }
    return SuccessResponseBody.builder()
        .status(FAILURE_STATUS)
        .message(USER_NOT_FOUND)
        .build();
  }

  public GetAllUsersResponseBody getAllUsers() {
    return GetAllUsersResponseBody.builder()
        .status(SUCCESS_STATUS)
        .data(new ArrayList<>(usersMap.keySet()))
        .build();
  }

  public SuccessResponseBody logOut(LogoutRequestBody logoutRequestBody) {
    if (loginSessionsSet.contains(logoutRequestBody.getUsername())) {
      loginSessionsSet.remove(logoutRequestBody.getUsername());
      return SuccessResponseBody.builder()
          .status(SUCCESS_STATUS)
          .message(SUCCESS_LOGOUT_MESSAGE)
          .build();
    }
    return SuccessResponseBody.builder()
        .status(FAILURE_STATUS)
        .message(LOGOUT_FAILURE_MESSAGE)
        .build();
  }
}


