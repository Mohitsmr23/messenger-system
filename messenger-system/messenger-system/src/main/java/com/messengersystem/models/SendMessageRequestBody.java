package com.messengersystem.models;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class SendMessageRequestBody {
  private String to;
  private String text;
}
