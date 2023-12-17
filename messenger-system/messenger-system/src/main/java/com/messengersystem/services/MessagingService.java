package com.messengersystem.services;

import com.messengersystem.models.ChatHistoryResponseBody;
import com.messengersystem.models.CreateLoginUserRequestBody;
import com.messengersystem.models.GetAllUsersResponseBody;
import com.messengersystem.models.LogoutRequestBody;
import com.messengersystem.models.SendMessageRequestBody;
import com.messengersystem.models.SuccessResponseBody;
import com.messengersystem.models.UnreadMessagesResponseBody;
import com.messengersystem.models.UserCreatedResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.messengersystem.constants.MessagingServiceConstants.CLOSING_CURLY_BRACE;
import static com.messengersystem.constants.MessagingServiceConstants.COLON;
import static com.messengersystem.constants.MessagingServiceConstants.COMMA_DELIMETER;
import static com.messengersystem.constants.MessagingServiceConstants.EXISTING_USER_MESSAGE;
import static com.messengersystem.constants.MessagingServiceConstants.FAILURE_STATUS;
import static com.messengersystem.constants.MessagingServiceConstants.LOGIN_MESSAGE;
import static com.messengersystem.constants.MessagingServiceConstants.LOGIN_SUCCESSFUL;
import static com.messengersystem.constants.MessagingServiceConstants.LOGOUT_FAILURE_MESSAGE;
import static com.messengersystem.constants.MessagingServiceConstants.MESSAGE_SENT;
import static com.messengersystem.constants.MessagingServiceConstants.NEW_USER_CREATED_MESSAGE;
import static com.messengersystem.constants.MessagingServiceConstants.NO_UNREAD_MESSAGES;
import static com.messengersystem.constants.MessagingServiceConstants.OPENING_CURLY_BRACE;
import static com.messengersystem.constants.MessagingServiceConstants.SUCCESS_LOGOUT_MESSAGE;
import static com.messengersystem.constants.MessagingServiceConstants.SUCCESS_STATUS;
import static com.messengersystem.constants.MessagingServiceConstants.TEXTS_KEYWORD;
import static com.messengersystem.constants.MessagingServiceConstants.UNREAD_MESSAGES;
import static com.messengersystem.constants.MessagingServiceConstants.USERNAME_KEYWORD;
import static com.messengersystem.constants.MessagingServiceConstants.USER_NOT_FOUND;
import static com.messengersystem.constants.MessagingServiceConstants.WRONG_PASSWORD;

@Service
@Slf4j
public class MessagingService {

  private final Map<String, String> usersMap;
  private final Set<String> loginSessionsSet;
  private List<String> chatHistoryList = new ArrayList<>();
  private Map<String, List<String>> recipientSentMessageMap = new HashMap<>();
  private Map<String, List<String>> senderSentMessageMap = new HashMap<>();
  private final Map<String, List<Map<String, List<String>>>> chatHistoryMap;
  private final Map<String, List<Map<String, List<String>>>> unreadMessagesMap;
  private final Map<String, String> senderReceiverMap;
  private final Map<String, String> senderReceiverChatMap;

  public MessagingService() {
    this.usersMap = new HashMap<>();
    this.loginSessionsSet = new HashSet<>();
    this.chatHistoryMap = new HashMap<>();
    this.unreadMessagesMap = new HashMap<>();
    this.senderReceiverMap = new HashMap<>();
    this.senderReceiverChatMap = new HashMap<>();
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

  public SuccessResponseBody sendMessage(String senderUsername, SendMessageRequestBody sendMessageRequestBody) {
    if (loginSessionsSet.contains(senderUsername)) {
      if (usersMap.containsKey(senderUsername) || usersMap.containsKey(sendMessageRequestBody.getTo())) {
        persistMessages(senderUsername, sendMessageRequestBody, unreadMessagesMap, senderReceiverMap, false);
        persistMessages(senderUsername, sendMessageRequestBody, chatHistoryMap, senderReceiverChatMap , true);
        return SuccessResponseBody.builder()
            .status(SUCCESS_STATUS)
            .message(MESSAGE_SENT)
            .build();
      }
      return SuccessResponseBody.builder()
          .status(FAILURE_STATUS)
          .message(USER_NOT_FOUND)
          .build();
    }
    return SuccessResponseBody.builder()
        .status(FAILURE_STATUS)
        .message(LOGIN_MESSAGE)
        .build();
  }

  private void persistMessages(String senderUsername, SendMessageRequestBody sendMessageRequestBody, Map<String, List<Map<String,
      List<String>>>> allMessageMap, Map<String, String> senderReceiverHashMap, Boolean isChatHistory) {
    Map<String, List<String>> recipientMap = new HashMap<>();
    Map<String, List<String>> previousMessageMap = new HashMap<>();
    List<String> previousUnreadMessage;
    if (senderReceiverHashMap.containsKey(senderUsername)) {
      if (isChatHistory) {
        previousMessageMap = getExistingChats(sendMessageRequestBody, allMessageMap);
        previousUnreadMessage = new ArrayList<>(getExistingMessages(senderUsername, sendMessageRequestBody, allMessageMap));
      } else {
        previousUnreadMessage = new ArrayList<>(getExistingMessages(senderUsername, sendMessageRequestBody, allMessageMap));
      }
      previousUnreadMessage.add(sendMessageRequestBody.getText());
      previousMessageMap.put(senderUsername, previousUnreadMessage);
      allMessageMap.put(sendMessageRequestBody.getTo(), List.of(previousMessageMap));
    } else {
      if (allMessageMap.containsKey(sendMessageRequestBody.getTo())) {
        List<Map<String, List<String>>> prevAllMsgList = new ArrayList<>(allMessageMap.get(sendMessageRequestBody.getTo()));
        recipientMap.put(senderUsername, List.of(sendMessageRequestBody.getText()));
        prevAllMsgList.add(recipientMap);
        allMessageMap.put(sendMessageRequestBody.getTo(), prevAllMsgList);
        senderReceiverHashMap.put(senderUsername, sendMessageRequestBody.getTo());
      } else {
        recipientMap.put(senderUsername, List.of(sendMessageRequestBody.getText()));
        allMessageMap.put(sendMessageRequestBody.getTo(),List.of(recipientMap));
        senderReceiverHashMap.put(senderUsername, sendMessageRequestBody.getTo());
      }
    }
  }

  public UnreadMessagesResponseBody getUnreadMessages(String user) {
    List<String> unreadMessagesList = new ArrayList<>();
    if (loginSessionsSet.contains(user)) {
      if (!ObjectUtils.isEmpty(unreadMessagesMap.get(user))) {
        for (Map<String, List<String>> senderMap : unreadMessagesMap.get(user)) {
          for (String sender : senderMap.keySet()) {
            unreadMessagesList.add(OPENING_CURLY_BRACE + USERNAME_KEYWORD + sender + COMMA_DELIMETER + TEXTS_KEYWORD + senderMap.get(sender) + CLOSING_CURLY_BRACE);
            senderReceiverMap.remove(sender);
          }
        }
        List<Map<String, List<String>>> readMessages = new ArrayList<>(unreadMessagesMap.get(user));
        readMessages.clear();
        unreadMessagesMap.put(user, readMessages);
      } else {
        return UnreadMessagesResponseBody.builder()
            .status(SUCCESS_STATUS)
            .message(NO_UNREAD_MESSAGES)
            .data(new ArrayList<>())
            .build();
      }
      return UnreadMessagesResponseBody.builder()
          .status(SUCCESS_STATUS)
          .message(UNREAD_MESSAGES)
          .data(unreadMessagesList)
          .build();
    }
    return UnreadMessagesResponseBody.builder()
        .status(FAILURE_STATUS)
        .message(LOGIN_MESSAGE)
        .data(unreadMessagesList)
        .build();
  }

  public ChatHistoryResponseBody getChatHistory(String user, String recipient) {
    if (loginSessionsSet.contains(user)) {
      if (!ObjectUtils.isEmpty(chatHistoryMap.get(user))) {
        recipientSentMessageMap = getSenderRecipientChatMessageList(recipient, user, chatHistoryMap.get(user));
      } if (!ObjectUtils.isEmpty(chatHistoryMap.get(recipient))) {
        senderSentMessageMap = getSenderRecipientChatMessageList(user, recipient, chatHistoryMap.get(recipient));
      }
      if (!ObjectUtils.isEmpty(senderSentMessageMap) || !ObjectUtils.isEmpty(recipientSentMessageMap)) {
        chatHistoryList = getSenderAndRecipientChatList(user, recipient, recipientSentMessageMap, senderSentMessageMap);
      }
      return ChatHistoryResponseBody.builder()
          .status(SUCCESS_STATUS)
          .texts(chatHistoryList)
          .build();
    }
    return ChatHistoryResponseBody.builder()
        .status(FAILURE_STATUS)
        .texts(List.of(LOGIN_MESSAGE))
        .build();
  }

  private Map<String, List<String>> getExistingChats(SendMessageRequestBody sendMessageRequestBody,
                                           Map<String, List<Map<String, List<String>>>> messageMap) {
    Map<String, List<String>> previousChatMap = new HashMap<>();
    if (!ObjectUtils.isEmpty(messageMap.get(sendMessageRequestBody.getTo()))) {
      for (Map<String, List<String>> map : messageMap.get(sendMessageRequestBody.getTo())) {
        for (String sender : map.keySet())
          previousChatMap.put(sender, map.get(sender));
      }
    }
    return previousChatMap;
  }

  private List<String> getExistingMessages(String senderUsername, SendMessageRequestBody sendMessageRequestBody,
                                           Map<String, List<Map<String, List<String>>>> messageMap) {
    List<String> previousUnreadMessageList = new ArrayList<>();
    if (!ObjectUtils.isEmpty(messageMap.get(sendMessageRequestBody.getTo()))) {
      for (Map<String, List<String>> map : messageMap.get(sendMessageRequestBody.getTo())) {
        if (map.containsKey(senderUsername)) {
          previousUnreadMessageList = map.get(senderUsername);
        }
      }
    }
    return previousUnreadMessageList;
  }


  private Map<String, List<String>> getSenderRecipientChatMessageList(String user, String recipient, List<Map<String, List<String>>> chatList) {
    Map<String,List<String>> messageMap = new HashMap<>();
    List<String> messageList = new ArrayList<>();
    for (Map<String, List<String>> chatMap : chatList) {
      for (String sender : chatMap.keySet()) {
        if (sender.equals(user)) {
          messageList.addAll(chatMap.get(sender));
        }
      }
    }
    messageMap.put(user + recipient, messageList);
    return messageMap;
  }

  private List<String> getSenderAndRecipientChatList(String user, String recipient, Map<String, List<String>> recipientSentMessageMap, Map<String, List<String>> senderSentMessageMap) {
    List<String> chatHistoryList = new ArrayList<>();
    List<String> senderSentMessageList = new ArrayList<>();
    List<String> recipientSentMessageList = new ArrayList<>();
    if (senderSentMessageMap.containsKey(user + recipient)) {
      senderSentMessageList = senderSentMessageMap.get(user + recipient);
    } if (recipientSentMessageMap.containsKey(recipient + user)) {
      recipientSentMessageList = recipientSentMessageMap.get(recipient + user);
    }
    if (senderSentMessageList.size() > recipientSentMessageList.size()) {
      for (int i = 0; i < recipientSentMessageList.size(); i++) {
        chatHistoryList.add(
            OPENING_CURLY_BRACE + user + COLON + senderSentMessageList.get(i) + CLOSING_CURLY_BRACE
                +
                COMMA_DELIMETER + OPENING_CURLY_BRACE + recipient + COLON + recipientSentMessageList.get(i) + CLOSING_CURLY_BRACE
        );
      }
      chatHistoryList.add(
          OPENING_CURLY_BRACE + user + COLON + senderSentMessageList.get(senderSentMessageList.size() - 1) + CLOSING_CURLY_BRACE
              +
              COMMA_DELIMETER + OPENING_CURLY_BRACE + recipient + COLON + CLOSING_CURLY_BRACE
      );
    } if (recipientSentMessageList.size() > senderSentMessageList.size()) {
      for (int i = 0; i < senderSentMessageList.size(); i++) {
        chatHistoryList.add(
            OPENING_CURLY_BRACE + user + COLON + senderSentMessageList.get(i) + CLOSING_CURLY_BRACE
                +
                COMMA_DELIMETER + OPENING_CURLY_BRACE + recipient + COLON + recipientSentMessageList.get(i) + CLOSING_CURLY_BRACE
        );
      }
      chatHistoryList.add(
          OPENING_CURLY_BRACE + user + COLON + CLOSING_CURLY_BRACE
              +
              COMMA_DELIMETER + OPENING_CURLY_BRACE + recipient + COLON
              + recipientSentMessageList.get(recipientSentMessageList.size() - 1) + CLOSING_CURLY_BRACE);
    } if (recipientSentMessageList.size() == senderSentMessageList.size() && recipientSentMessageList.size() != 0 && senderSentMessageList.size() != 0) {
      for (int i = 0; i < senderSentMessageList.size(); i++) {
        chatHistoryList.add(
            OPENING_CURLY_BRACE + user + COLON + senderSentMessageList.get(i) + CLOSING_CURLY_BRACE
                +
                COMMA_DELIMETER + OPENING_CURLY_BRACE + recipient + COLON + recipientSentMessageList.get(i) + CLOSING_CURLY_BRACE
        );
      }
    }
    return chatHistoryList;
  }
}


