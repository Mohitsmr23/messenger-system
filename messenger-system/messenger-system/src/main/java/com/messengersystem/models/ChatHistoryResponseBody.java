package com.messengersystem.models;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@Builder
public class ChatHistoryResponseBody {
  private String status;
  private List<String> texts;
}
