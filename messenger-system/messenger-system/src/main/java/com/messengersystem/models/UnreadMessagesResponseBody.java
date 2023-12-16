package com.messengersystem.models;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@Builder
public class UnreadMessagesResponseBody {
  private String status;
  private String message;
  private List<String> data;
}
