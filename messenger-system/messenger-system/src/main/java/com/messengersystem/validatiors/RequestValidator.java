package com.messengersystem.validators;

import com.messengersystem.exceptions.InvalidRequestException;
import com.messengersystem.models.CreateLoginUserRequestBody;
import com.messengersystem.models.LogoutRequestBody;
import com.messengersystem.models.SendMessageRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import static com.messengersystem.constants.MessagingServiceConstants.PASSWORD_MISSING;
import static com.messengersystem.constants.MessagingServiceConstants.USERNAME_MISSING;
import static com.messengersystem.constants.MessagingServiceConstants.USERNAME_PASSWORD_MISSING;
import static com.messengersystem.constants.MessagingServiceConstants.VALID_MESSAGE;
import static com.messengersystem.constants.MessagingServiceConstants.VALID_USERNAME_MESSAGE;

public class RequestValidator {
  public static void validateCreateAndLoginUserRequest(CreateLoginUserRequestBody createLoginUserRequestBody) {
    if (ObjectUtils.isEmpty(createLoginUserRequestBody.getUsername())) {
      throw new InvalidRequestException(USERNAME_MISSING, HttpStatus.BAD_REQUEST.toString());
    } else if (ObjectUtils.isEmpty(createLoginUserRequestBody.getPassword())) {
      throw new InvalidRequestException(PASSWORD_MISSING, HttpStatus.BAD_REQUEST.toString());
    } else {
      throw new InvalidRequestException(USERNAME_PASSWORD_MISSING, HttpStatus.BAD_REQUEST.toString());
    }
  }

  public static void validateSendMessageRequest(SendMessageRequestBody sendMessageRequestBody) {
    if (ObjectUtils.isEmpty(sendMessageRequestBody.getTo())) {
      throw new InvalidRequestException(USERNAME_MISSING, HttpStatus.BAD_REQUEST.toString());
    } else if (ObjectUtils.isEmpty(sendMessageRequestBody.getText())) {
      throw new InvalidRequestException(VALID_MESSAGE, HttpStatus.BAD_REQUEST.toString());
    } else {
      throw new InvalidRequestException(VALID_USERNAME_MESSAGE, HttpStatus.BAD_REQUEST.toString());
    }
  }

  public static void validateLogoutRequestBody(LogoutRequestBody logoutRequestBody) {
    if (ObjectUtils.isEmpty(logoutRequestBody.getUsername())) {
      throw new InvalidRequestException(USERNAME_MISSING, HttpStatus.BAD_REQUEST.toString());
    }
  }
}
