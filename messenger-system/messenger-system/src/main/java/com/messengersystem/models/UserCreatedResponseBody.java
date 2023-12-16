package com.messengersystem.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreatedResponseBody {
  private String status;
  private String message;
}
